package com.lzl.csservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.csservice.entity.customerLoss.CustomerLoss;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieve;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieveVo;
import com.lzl.csservice.entity.customerLoss.CustomerLossVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
public interface CustomerLossService extends IService<CustomerLoss> {

    IPage<CustomerLossVo> pageCustomerLossCondition(Page<CustomerLoss> page, CustomerLoss customerLoss);

    List<CustomerLossReprieveVo> getCustomerLossReprieveList(String lossId);

    void addCustomerLossReprieve(CustomerLossReprieve customerLossReprieve);

    void updateCustomerLossReprieve(CustomerLossReprieve customerLossReprieve);

    void deleteCustomerLossReprieve(String customerLossReprieveId);

    CustomerLossReprieve getCustomerLossReprieveById(String customerLossReprieveId);

    void enteringCustomerLoss();
}
