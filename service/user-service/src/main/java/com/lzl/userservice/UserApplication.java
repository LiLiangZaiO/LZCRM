package com.lzl.userservice;

import com.lzl.feign.clients.CustomerClient;
import com.lzl.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.lzl.userservice.mapper")
@ComponentScan("com.lzl")
@EnableFeignClients(clients = CustomerClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}
