package com.lzl.saleservice.mapper;

import com.lzl.saleservice.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-10
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    int deleteOrderById(String orderId);

    Map<String, Integer> getPerformanceByMonth(String userId);
}
