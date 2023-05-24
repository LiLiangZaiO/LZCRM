package com.lzl.csservice;

import com.lzl.feign.clients.SaleClient;
import com.lzl.feign.clients.UserClient;
import com.lzl.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan("com.lzl.csservice.mapper")
@ComponentScan("com.lzl")
@EnableFeignClients(clients = {UserClient.class, SaleClient.class},defaultConfiguration = DefaultFeignConfiguration.class)
@SpringBootApplication
@EnableScheduling //启用定时任务
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class);
    }
}
