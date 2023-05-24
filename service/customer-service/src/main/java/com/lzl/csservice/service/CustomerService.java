package com.lzl.csservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzl.csservice.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzl.csservice.entity.Report.Report;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-23
 */
public interface CustomerService extends IService<Customer> {

    IPage<Customer> pageCustomerCondition(Page<Customer> page, Customer customer);

    Boolean addMyCustomer(String customerId);

    Boolean addCustomerAndLinkman( CustomerLinkManVo customerLinkManVo);

    Boolean updateCustomerInfo(CustomerLinkManVo customerLinkManVo);

    IPage<Customer> pageMyCustomerCondition(Page<Customer> page, Customer customer);

    Boolean deleteMyCustomer(String customerId);

    MyCustomerVo getMyCustomerInfoById(String customerId);

    String getCustomerNameById(String customerId);

    Boolean updateCustomerManagerId(String customerId, String newManagerId);

    Integer updateCustomerStatus(String customerId, Integer status);

    IPage<Customer> pageTdCustomerList(Page<Customer> page);

    void updateCustomerMaturity(String customerId, String maturity);

    List<Customer> getEnteringCustomerLossList();

    void updateCustomerStatusByIds(List<String> customerIdList);

    IPage<Customer> pageFailedCustomerList(Page<Customer> page);

    List<Map<String, Object>> countManagerCustomerRank(Report report);

    List<Map<String, Object>> countCustomerSource();

    List<Map<String, Object>> countCompanyPerformance(Report report);

    List<Map<String, Object>> countCustomerType();

    void failedMyCustomer(String customerId);

    void deleteCustomerById(String customerId);

    void updateMyCustomerInfo(Customer customer);

    Integer countMyCustomerNumber(String userId);

    IPage<Customer> pageMyFailedCustomerList(Page<Customer> page);

    IPage<Customer> pageMyTdCustomerList(Page<Customer> page);
}
