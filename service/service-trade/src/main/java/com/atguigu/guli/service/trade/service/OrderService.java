package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-04
 */
public interface OrderService extends IService<Order> {

    String createOrderByCorseId(String courseId, JwtInfo jwtInfo);

    Boolean getOrderStatus(String memberId, String courseId);

    Order getOrderByOrderNo(String memberId, String orderNo);

    Order getOrderByOrderNo( String orderNo);

    boolean queryPayStatus(String orderNo);

    List<Order> getOrderListByMemberId(String memberId);

    Boolean removeByIdAndMemberId(String orderId, String memberId);
}
