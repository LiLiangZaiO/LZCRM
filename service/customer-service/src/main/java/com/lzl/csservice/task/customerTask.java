package com.lzl.csservice.task;

import com.lzl.csservice.service.CustomerLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务
 */
@Component
public class customerTask {

    @Autowired
    private CustomerLossService customerLossService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void enteringCustomerLoss(){
        System.out.println("客户流失定时任务开始执行 --> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        customerLossService.enteringCustomerLoss();
    }

}
