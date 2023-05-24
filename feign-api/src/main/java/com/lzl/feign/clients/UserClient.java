package com.lzl.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("userservice")
public interface UserClient {

    @GetMapping("/user/getUserIdByUsername/{username}")
    String getUserIdByUsername(@PathVariable("username") String username);

    @GetMapping("/user/getUsernameByUserId/{userId}")
    String getUsernameByUserId(@PathVariable("userId") String userId);

}
