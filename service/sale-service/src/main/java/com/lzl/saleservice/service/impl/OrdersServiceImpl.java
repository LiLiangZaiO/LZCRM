package com.lzl.saleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.common_utils.domain.ResultCode;
import com.lzl.feign.clients.CustomerClient;
import com.lzl.feign.clients.UserClient;
import com.lzl.saleservice.entity.*;
import com.lzl.saleservice.mapper.OrdersMapper;
import com.lzl.saleservice.service.OrderItemService;
import com.lzl.saleservice.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzl.saleservice.service.ProductService;
import com.lzl.saleservice.utils.OrderNoUtil;
import com.lzl.servicebase.exceptionHandler.LZLException;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-10
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Override
    public List<OrderVo> getMyOrdersList() {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        List<Orders> ordersList = baseMapper.selectList(new QueryWrapper<Orders>().eq("manager_id", userId).orderByDesc("create_time").eq("status",0));

        ArrayList<OrderVo> orderVoList = new ArrayList<>();

        for (Orders orders : ordersList) {

            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(orders,orderVo);

            //设置客户名称
            String customerName = customerClient.getCustomerNameById(orders.getCustomerId());
            orderVo.setCustomerName(customerName);

            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    @Override
    public OrderVo getOrderItemInfo(String orderId) {
        OrderVo orderVo = new OrderVo();
        //根据订单Id查询 订单表
        Orders orders = baseMapper.selectById(orderId);
        BeanUtils.copyProperties(orders,orderVo);
        //根据订单Id查询 订单详情表
        OrderItem orderItem = orderItemService.getOne(new QueryWrapper<OrderItem>().eq("orders_id", orderId));
        //直接copyProperties 需要 重新设置一次订单Id
        BeanUtils.copyProperties(orderItem,orderVo);
        orderVo.setId(orderId);

        //设置产品名称
        String productName = productService.getProductNameById(orderItem.getProductId());
        orderVo.setProductName(productName);
        //设置客户名称
        String customerName = customerClient.getCustomerNameById(orders.getCustomerId());
        orderVo.setCustomerName(customerName);
        //设置 跟进人名称
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        orderVo.setManagerName(username);
        return orderVo;
    }

    /**
     * 创建订单
     *  1、需要查看 产品库存 是否 满足
     *      满足-则减库存
     *  2、订单创建完成时，客户的状态需要更新为 成交客户
     *  3、把信息插入订单表
     *  4.把信息插入订单详情表
     *
     * @param orderItemVo
     */
    @Override
    public void addOrderInfo(OrderItemVo orderItemVo) {
        //查询得到产品的库存
        Product product = productService.getOne(new QueryWrapper<Product>().eq("id", orderItemVo.getProductId()));

        if ((product.getRepertory()-orderItemVo.getNum())<0){
            throw new LZLException(ResultCode.ERROR, "产品库存不足！请等待产品管理员更新！");
        }
        //减少库存
        product.setRepertory(product.getRepertory()-orderItemVo.getNum());
        productService.updateById(product);

        //修改客户状态----为成交客户--0
        Integer i = customerClient.updateCustomerStatus(orderItemVo.getCustomerId(), 0);
        if (i<=0){
            throw new LZLException(ResultCode.ERROR,"更改客户状态失败!");
        }

        //插入 订单表
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderItemVo,orders);
        //需要手动设置 订单编号
        String orderNo = OrderNoUtil.getOrderNo();
        orders.setId(orderNo);
        //设置跟进人
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);
        orders.setManagerId(userId);
        //
        int insert = baseMapper.insert(orders);
        if (insert<=0){
            throw new LZLException(ResultCode.ERROR,"插入订单表失败!");
        }

        //插入 订单详情表--需要把订单编号插入其中
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemVo,orderItem);
        orderItem.setOrdersId(orderNo);

        boolean save = orderItemService.save(orderItem);
        if (!save){
            throw new LZLException(ResultCode.ERROR,"插入订单详情表失败!");
        }
    }

    /**
     * 删除订单，不能简单删除
     *  需要根据创建时间来判断，若超过两天，则不能再修改
     * @param orderId
     * @return
     */
    @Override
    public void deleteOrderById(String orderId) {

        //查询订单创建时间
        Orders orders = baseMapper.selectById(orderId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createTime = orders.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(createTime, now);
        long diff = duration.toDays();
        if (diff > 7) { // 如果订单创建时间距今超过7天，则不能删除订单
            throw new LZLException(ResultCode.ERROR,"订单已经超过7天，无法删除");
        }

        // 如果订单创建时间距今小于等于7天，则可以删除订单
        //  1.产品的库存需要回升
        OrderItem orderItem = orderItemService.getOne(new QueryWrapper<OrderItem>().eq("orders_id", orderId));
        productService.updateProductRepertory(orderItem.getProductId(),orderItem.getNum());
        //  2.更改客户的状态-原本是成交客户状态，需要更改为意向各户
        customerClient.updateCustomerStatus(orders.getCustomerId(),1);
        //  3.删除订单详情表中的记录
        boolean a = orderItemService.remove(new QueryWrapper<OrderItem>().eq("orders_id", orderId));
        if (!a){
            throw new LZLException(ResultCode.ERROR,"删除订单详情失败!");
        }
        //  4.删除订单表中的记录
        int i = baseMapper.deleteOrderById(orderId);
        if (i<=0){
            throw new LZLException(ResultCode.ERROR,"删除订单失败!");
        }


    }

    /**
     * 首页--获取本月的业绩
     * @return
     */
    @Override
    public Map<String, Integer> getPerformanceByMonth() {
        //需要获取当前登录的Id---从当前线程取之前保存的用户信息Id
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名字---远程调用用户模块得到当前的用户Id
        String userId = userClient.getUserIdByUsername(username);

        Map<String, Integer> performanceByMonth = baseMapper.getPerformanceByMonth(userId);

        return performanceByMonth;
    }

}
