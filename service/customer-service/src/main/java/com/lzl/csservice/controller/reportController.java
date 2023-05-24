package com.lzl.csservice.controller;

import com.lzl.common_utils.domain.R;
import com.lzl.csservice.entity.Report.Report;
import com.lzl.csservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class reportController {

    @Autowired
    private CustomerService customerService;


    /**
     * 统计 客户的组成类型
     * @return
     */
    @GetMapping("/countCustomerType")
    public R countCustomerType(){
        List<Map<String, Object>> result = customerService.countCustomerType();
        return R.ok().data("customerType",result);
    }

    /**
     * 按 年份 来 查询 公司的当月的整体业绩
     * @param report
     * @return
     */
    @PostMapping("/countCompanyPerformance")
    public R countCompanyPerformance(@RequestBody(required = false) Report report){
        List<Map<String, Object>> result = customerService.countCompanyPerformance(report);
        return R.ok().data("companyPerformance",result);
    }

    /**
     * 按月份统计 该月销售经理的营业额排名
     * @param report
     * @return
     */
    @PostMapping("/countManagerCustomerRank")
    public R countManagerCustomerRank(@RequestBody(required = false) Report report){

        List<Map<String, Object>> result = customerService.countManagerCustomerRank(report);

        return R.ok().data("managerCustomerRank",result);
    }

    /**
     * 统计客户的来源
     * @return
     */
    @GetMapping("/countCustomerSource")
    public R countCustomerSource(){
        List<Map<String, Object>> result = customerService.countCustomerSource();
        return R.ok().data("customerSource",result);
    }




}
