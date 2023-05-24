package com.lzl.csservice.service;

import com.lzl.csservice.entity.customerTransfer.CustomerTransfer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.csservice.entity.customerTransfer.CustomerTransferVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
public interface CustomerTransferService extends IService<CustomerTransfer> {

    List<CustomerTransferVo> getCustomerTransferById(String customerId);

    Boolean addCustomerTransfer(CustomerTransfer customerTransfer);
}
