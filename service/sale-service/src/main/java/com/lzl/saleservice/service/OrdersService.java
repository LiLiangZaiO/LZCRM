package com.lzl.saleservice.service;

import com.lzl.saleservice.entity.OrderItemVo;
import com.lzl.saleservice.entity.OrderVo;
import com.lzl.saleservice.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-10
 */
public interface OrdersService extends IService<Orders> {

    List<OrderVo> getMyOrdersList();

    OrderVo getOrderItemInfo(String orderId);

    void addOrderInfo(OrderItemVo orderItemVo);

    void deleteOrderById(String orderId);

    Map<String, Integer> getPerformanceByMonth();
}
