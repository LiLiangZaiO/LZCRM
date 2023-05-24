package com.lzl.csservice.controller;


import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.customerTransfer.CustomerTransfer;
import com.lzl.csservice.entity.customerTransfer.CustomerTransferVo;
import com.lzl.csservice.service.CustomerTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-05
 */
@RestController
@RequestMapping("/customerTransfer")
public class CustomerTransferController {

    @Autowired
    private CustomerTransferService customerTransferService;


    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getCustomerTransferById/{customerId}")
    public R getCustomerTransferById(@PathVariable String customerId){
        List<CustomerTransferVo> ctList = customerTransferService.getCustomerTransferById(customerId);
        return R.ok().data("CustomerTransferList",ctList);
    }

    /**
     *  添加 客户转移记录
     *  需要 表单校验-- 转移后的客户经理不能为空、原因也不能为空！！
     *      需要这样设置---默认 设置 转移前的客户经理为 0---不存在 （在公海池中设置的所属客户经理也是0）
     *  Transactional  事务注解---两个必须同时成功！
     * @param customerTransfer
     * @return
     */
    @Transactional
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/addCustomerTransfer")
    public R addCustomerTransfer(@Validated @RequestBody CustomerTransfer customerTransfer){
        Boolean result = customerTransferService.addCustomerTransfer(customerTransfer);
        if (!result){
            return R.error().message("客户转移失败!");
        }
        return R.ok();

    }




}

