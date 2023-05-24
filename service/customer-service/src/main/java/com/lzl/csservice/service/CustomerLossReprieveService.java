package com.lzl.csservice.service;

import com.lzl.csservice.entity.customerLoss.CustomerLossReprieve;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
public interface CustomerLossReprieveService extends IService<CustomerLossReprieve> {

    String getCustomerLossId(String customerLossReprieveId);
}
