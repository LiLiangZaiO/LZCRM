package com.lzl.csservice.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.customerLoss.CustomerLoss;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieve;
import com.lzl.csservice.entity.customerLoss.CustomerLossReprieveVo;
import com.lzl.csservice.entity.customerLoss.CustomerLossVo;
import com.lzl.csservice.service.CustomerLossService;
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
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/customerLoss")
public class CustomerLossController {


    @Autowired
    private CustomerLossService customerLossService;

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getCustomerLossReprieveById/{customerLossReprieveId}")
    public R getCustomerLossReprieveById(@PathVariable String customerLossReprieveId){
        CustomerLossReprieve customerLossReprieve = customerLossService.getCustomerLossReprieveById(customerLossReprieveId);
        return R.ok().data("customerLossReprieveInfo",customerLossReprieve);
    }

    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @Transactional
    @GetMapping("/deleteCustomerLossReprieve/{customerLossReprieveId}")
    public R deleteCustomerLossReprieve(@PathVariable String customerLossReprieveId){
        customerLossService.deleteCustomerLossReprieve(customerLossReprieveId);
        return R.ok();
    }


    /**
     * 修改流失客户的 暂缓措施 和添加逻辑差不多
     * @param customerLossReprieve
     * @return
     */
    @Transactional
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/updateCustomerLossReprieve")
    public R updateCustomerLossReprieve(@Validated @RequestBody CustomerLossReprieve customerLossReprieve){
        customerLossService.updateCustomerLossReprieve(customerLossReprieve);
        return R.ok();
    }

    /**
     * 添加 流失客户的 暂缓操作
     *  需要更改 客户流失表中的客户状态！
     *  若 流失状态的 确认流失或者挽回客户，则还需要更改 客户表中的状态--让其重新出现在公海池中！！！！
     * @param customerLossReprieve
     * @return
     */
    @Transactional
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/addCustomerLossReprieve")
    public R addCustomerLossReprieve(@Validated @RequestBody CustomerLossReprieve customerLossReprieve){
        customerLossService.addCustomerLossReprieve(customerLossReprieve);
        return R.ok();
    }


    /**
     * 根据流失记录的ID，查询对流失客户的暂缓措施
     * @param lossId
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @GetMapping("/getCustomerLossReprieveList/{lossId}")
    public R getCustomerLossReprieveList(@PathVariable String lossId){
        List<CustomerLossReprieveVo> customerLossReprieveList = customerLossService.getCustomerLossReprieveList(lossId);
        return R.ok().data("customerLossReprieveList",customerLossReprieveList);
    }

    /**
     * 分页条件查询 客户流失
     * @param current
     * @param limit
     * @param customerLoss
     * @return
     */
    @Secured({"ROLE_CUSTOMER","ROLE_ADMIN"})
    @PostMapping("/pageCustomerLossCondition/{current}/{limit}")
    public R pageCustomerLossCondition(@PathVariable Long current,
                                       @PathVariable Long limit,
                                       @RequestBody(required = false)CustomerLoss customerLoss){
        Page<CustomerLoss> page = new Page<>(current, limit);
        IPage<CustomerLossVo> customerLossVoIPage = customerLossService.pageCustomerLossCondition(page,customerLoss);

        return R.ok().data("customerLossVoList",customerLossVoIPage);
    }













}

