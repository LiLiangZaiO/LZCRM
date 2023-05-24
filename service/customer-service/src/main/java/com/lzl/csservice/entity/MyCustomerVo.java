package com.lzl.csservice.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MyCustomerVo {

    @ApiModelProperty(value = "客户名称（唯一性）")
    private String name;

    @ApiModelProperty(value = "客户类别")
    private String type;

    @ApiModelProperty(value = "客户等级")
    private String level;

    @ApiModelProperty(value = "客户状态")
    private String status;

    @ApiModelProperty(value = "客户信用度")
    private String credit;

    @ApiModelProperty(value = "客户所在地区")
    private String area;

    @ApiModelProperty(value = "公司详细地址")
    private String companyAddress;

    @ApiModelProperty(value = "公司电话")
    private String companyPhone;

    @ApiModelProperty(value = "邮政编码")
    private String postCode;

    @ApiModelProperty(value = "传真地址")
    private String faxAddress;

    @ApiModelProperty(value = "公司网站")
    private String companyWebsite;

    @ApiModelProperty(value = "营业执照注册号")
    private String licenseNumber;

    @ApiModelProperty(value = "法人")
    private String corporation;

    @ApiModelProperty(value = "开户银行")
    private String depositBank;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "地税登记号")
    private String landTaxNumber;

    @ApiModelProperty(value = "国税登记号")
    private String nationalTaxNumber;

    @ApiModelProperty(value = "客户来源")
    private String source;

    @ApiModelProperty(value = "客户描述")
    private String description;

    @ApiModelProperty(value = "客户成熟度")
    private String maturity;

    @ApiModelProperty(value = "客户所属的客户经理（外键）")
    private String managerName;

    @ApiModelProperty(value = "客户主要意向产品名称（外键）")
    private String productName;

    @ApiModelProperty(value = "客户主要意向产品（外键）")
    private String productId;

    @ApiModelProperty(value = "创建人（经理）")
    private String createrName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
