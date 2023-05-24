package com.lzl.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("csservice")
public interface CustomerClient {

    @GetMapping("/customer/getCustomerNameById/{customerId}")
    String getCustomerNameById(@PathVariable("customerId") String customerId);

    @GetMapping("/customer/updateCustomerStatus/{customerId}/{status}")
    Integer updateCustomerStatus(@PathVariable("customerId") String customerId,
                                 @PathVariable("status") Integer status);

    @GetMapping("/customer/countMyCustomerNumber/{userId}")
    Integer countMyCustomerNumber(@PathVariable("userId") String userId);
}
