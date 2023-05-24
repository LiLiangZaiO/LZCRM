package com.lzl.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("saleservice")
public interface SaleClient {

    @GetMapping("/product/getProductNameById/{productId}")
    String getProductNameById(@PathVariable("productId") String productId);


}
