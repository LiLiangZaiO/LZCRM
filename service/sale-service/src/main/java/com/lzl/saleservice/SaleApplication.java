package com.lzl.saleservice;

import com.lzl.feign.clients.CustomerClient;
import com.lzl.feign.clients.SaleClient;
import com.lzl.feign.clients.UserClient;
import com.lzl.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.lzl.saleservice.mapper")
@ComponentScan("com.lzl")
@EnableFeignClients(clients = {UserClient.class, CustomerClient.class},defaultConfiguration = DefaultFeignConfiguration.class)
@SpringBootApplication()
public class SaleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaleApplication.class);
    }
}
