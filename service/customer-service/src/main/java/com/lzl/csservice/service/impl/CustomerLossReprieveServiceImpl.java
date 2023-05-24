package com.lzl.csservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieve;
import com.lzl.csservice.mapper.CustomerLossReprieveMapper;
import com.lzl.csservice.service.CustomerLossReprieveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
@Service
public class CustomerLossReprieveServiceImpl extends ServiceImpl<CustomerLossReprieveMapper, CustomerLossReprieve> implements CustomerLossReprieveService {

    @Override
    public String getCustomerLossId(String customerLossReprieveId) {
        CustomerLossReprieve customerLossReprieve = baseMapper.selectOne(new QueryWrapper<CustomerLossReprieve>().select("loss_customer_id").eq("id", customerLossReprieveId));
        return customerLossReprieve.getLossId();
    }
}
