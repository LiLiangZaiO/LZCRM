package com.lzl.saleservice.controller;


import com.lzl.common_utils.domain.R;
import com.lzl.saleservice.entity.OrderItemVo;
import com.lzl.saleservice.entity.OrderVo;
import com.lzl.saleservice.service.OrdersService;
import org.apache.catalina.webresources.AbstractArchiveResource;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    /**
     * 首页--获取本月的业绩
     * @return
     */
    @GetMapping("/getPerformanceByMonth")
    public R getPerformanceByMonth(){
        Map<String,Integer> performanceByMonth = ordersService.getPerformanceByMonth();
        return R.ok().data("performanceByMonth",performanceByMonth);
    }

    /**
     * 删除订单，不能简单删除
     * @param orderId
     * @return
     */
    @Transactional
    @GetMapping("/deleteOrderById/{orderId}")
    public R deleteOrderById(@PathVariable String orderId){
        ordersService.deleteOrderById(orderId);
        return R.ok();
    }

    @Transactional
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/addOrderInfo")
    public R addOrderInfo(@Validated @RequestBody OrderItemVo orderItemVo){
        ordersService.addOrderInfo(orderItemVo);
        return R.ok();
    }


    /**
     * 查询我的订单
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getMyOrdersList")
    public R getMyOrdersList(){
        List<OrderVo> orderVoList =  ordersService.getMyOrdersList();
        return R.ok().data("myOrderVList",orderVoList);
    }

    /**
     * 根据订单号 查询 订单的详细信息
     * @param orderId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getOrderItemInfo/{orderId}")
    public R getOrderItemInfo(@PathVariable String orderId){
        OrderVo orderItemInfo = ordersService.getOrderItemInfo(orderId);
        return R.ok().data("orderItemInfo",orderItemInfo);
    }

}

