package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2021-01-04
 */

@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    OrderService orderService;

    //删除订单
    @DeleteMapping("/auth/delete/{orderId}")
    public R delete(@PathVariable String orderId, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Boolean b = orderService.removeByIdAndMemberId(orderId,memberId);
        if (b)
            return R.ok().message("删除成功！");
        else
            return R.ok().message("删除成功！");
    }

    //订单列表
    @GetMapping("/auth/list")
    public R list(HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        List<Order> list = orderService.getOrderListByMemberId(memberId);
        //List<Order> list = orderService.list();
        return R.ok().data("items",list);
    }

    @GetMapping("/query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        boolean result = orderService.queryPayStatus(orderNo);
        if (result) {//支付成功
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);//支付中
    }

    @GetMapping("/auth/get-order-status/{courseId}")
    public R getOrderStatus(@PathVariable String courseId,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Boolean flag = orderService.getOrderStatus(memberId,courseId);
        return R.ok().data("status",flag);
    }

    @GetMapping("/auth/get-order/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(queryWrapper);
        if(order!=null)
            return R.ok().data("item",order);
        else
            return R.error().message("请刷新后重试");
    }

    @GetMapping("/auth/create-order/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        String orderNo =orderService.createOrderByCorseId(courseId,jwtInfo);
        return R.ok().data("orderNo",orderNo);
    }

}

