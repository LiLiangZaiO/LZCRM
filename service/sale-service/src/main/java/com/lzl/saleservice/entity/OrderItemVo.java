package com.lzl.saleservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemVo {

    @NotBlank(message = "订单送货地址不能为空!")
    @ApiModelProperty(value = "送货地址")
    private String address;

    @NotNull(message = "订单总金额不能为空!")
    @ApiModelProperty(value = "总金额")
    private Double price;

    @NotBlank(message = "客户编号不能为空!")
    @ApiModelProperty(value = "客户")
    private String customerId;

    @NotNull(message = "产品购买数量不能为空!")
    @ApiModelProperty(value = "数量")
    private Integer num;

    @NotNull(message = "产品单价不能为空!")
    @ApiModelProperty(value = "单价")
    private Double unitPrice;

    @NotBlank(message = "产品编号不能为空!")
    @ApiModelProperty(value = "产品")
    private String productId;

}
