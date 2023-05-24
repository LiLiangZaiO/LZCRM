package com.lzl.csservice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CustomerLossNoUtil {

    /**
     * 获取 客户流失编号
     * @return
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }


}
