package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.OrderNoUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    UcenterMemberService ucenterMemberService;

    @Override
    public String createOrderByCorseId(String courseId, JwtInfo jwtInfo) {

        //查询课程订单是否已存在
        QueryWrapper<Order> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",jwtInfo.getId());
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if(orderExist!=null){
            return orderExist.getOrderNo();
            //throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }
        //查询课程信息
        R rCourseDto = eduCourseService.getCourseDtoById(courseId);
//        Object courseDtoObj = rCourseDto.getData().get("courseDto");
        ObjectMapper mapper = new ObjectMapper();
        CourseDto courseDto = mapper.convertValue(rCourseDto.getData().get("courseDto"), CourseDto.class);
        if(courseDto==null){
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }
        //查询用户信息
        R rMember = ucenterMemberService.getMemberDtoById(jwtInfo.getId());
        MemberDto memberDto = mapper.convertValue(rMember.getData().get("memberDto"), MemberDto.class);
        if(memberDto==null){
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }
        //创建订单

        String orderNo = OrderNoUtils.getOrderNo();
        Order order=new Order();
        order.setStatus(0);//支付状态，未支付
        order.setPayType(1); //微信支付
        order.setOrderNo(orderNo);//订单号
        //课程信息
        order.setCourseId(courseDto.getId());
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)).longValue());
        //用户信息
        order.setMemberId(memberDto.getId());
        order.setNickname(memberDto.getNickname());
        order.setMobile(memberDto.getMobile());

        baseMapper.insert(order);

        return orderNo;
    }

    @Override
    public Boolean getOrderStatus(String memberId, String courseId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId)
                .eq("course_id",courseId)
                .eq("status",1);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count.intValue()>0;
    }

    @Override
    public Order getOrderByOrderNo(String memberId, String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo)
                .eq("member_id",memberId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        if(order==null)
            return false;
        return order.getStatus() == 1;
    }

    @Override
    public List<Order> getOrderListByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper =new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id",memberId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean removeByIdAndMemberId(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        queryWrapper.eq("member_id",memberId);
        return baseMapper.delete(queryWrapper)>0;
    }

}
