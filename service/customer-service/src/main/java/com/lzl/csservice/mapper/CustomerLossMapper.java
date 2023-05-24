package com.lzl.csservice.mapper;

import com.lzl.csservice.entity.customerLoss.CustomerLoss;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
public interface CustomerLossMapper extends BaseMapper<CustomerLoss> {

    Integer updateCustomerLossStatus(String lossId, String lossStatus);

    void insertCustomerLossList(@Param("customerLossList") List<CustomerLoss> customerLossList);
}
