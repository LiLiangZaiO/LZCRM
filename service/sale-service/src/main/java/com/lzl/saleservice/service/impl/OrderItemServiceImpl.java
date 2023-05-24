package com.lzl.saleservice.service.impl;

import com.lzl.saleservice.entity.OrderItem;
import com.lzl.saleservice.mapper.OrderItemMapper;
import com.lzl.saleservice.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-10
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
