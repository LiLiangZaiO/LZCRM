package com.lzl.saleservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderVo {

    @ApiModelProperty(value = "订单编号")
    private String id;

    @ApiModelProperty(value = "日期")
    private Date createTime;

    @ApiModelProperty(value = "送货地址")
    private String address;

    @ApiModelProperty(value = "总金额")
    private Double price;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "跟进人")
    private String managerName;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "单价")
    private Double unitPrice;

    @ApiModelProperty(value = "产品")
    private String productName;
}
