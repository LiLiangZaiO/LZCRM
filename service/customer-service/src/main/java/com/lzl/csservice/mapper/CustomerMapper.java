package com.lzl.csservice.mapper;

import com.lzl.csservice.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-23
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    int updateCustomerId(String customerId, String userId);

    String getCreaterIdByCustomerId(String customerId);

    String getCustomerNameById(String customerId);

    Integer updateCustomerStatus(String customerId, String status);

    Integer updateCustomerMaturity(String customerId, String maturity);

    List<Customer> getEnteringCustomerLossList();

    void updateCustomerStatusByIds(@Param("customerIdList") List<String> customerIdList);

    List<Map<String, Object>> countManagerCustomerRank(Date monthDate);

    List<Map<String, Object>> countCustomerSource();

    List<Map<String, Object>> countCompanyPerformance(Date yearDate);

    List<Map<String, Object>> countCustomerType();

    void failedMyCustomer(String customerId);

    Integer deleteCustomerById(String customerId);
}
