package com.lzl.csservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CustomerLinkManVo {

    @ApiModelProperty(value = "客户编号")
    private String id;

    @NotBlank(message = "客户名称不能为空!")
    @ApiModelProperty(value = "客户名称（唯一性）")
    private String name;

    @ApiModelProperty(value = "客户描述")
    private String description;

    @NotBlank(message = "客户类别不能为空!")
    @ApiModelProperty(value = "客户类别")
    private String type;

    @NotBlank(message = "客户等级不能为空!")
    @ApiModelProperty(value = "客户等级")
    private String level;

    @ApiModelProperty(value = "客户状态")
    private String status;

    @NotBlank(message = "客户信用度不能为空!")
    @ApiModelProperty(value = "客户信用度")
    private String credit;

    @NotBlank(message = "客户所在地区不能为空!")
    @ApiModelProperty(value = "客户所在地区")
    private String area;

    @NotBlank(message = "公司详细地址不能为空!")
    @ApiModelProperty(value = "公司详细地址")
    private String companyAddress;

    @NotBlank(message = "公司电话不能为空!")
    @ApiModelProperty(value = "公司电话")
    private String companyPhone;

    @NotBlank(message = "邮政编码不能为空!")
    @ApiModelProperty(value = "邮政编码")
    private String postCode;

    @NotBlank(message = "传真地址不能为空!")
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

    @NotBlank(message = "客户来源不能为空!")
    @ApiModelProperty(value = "客户来源")
    private String source;


    @NotBlank(message = "客户成熟度不能为空!")
    @ApiModelProperty(value = "客户成熟度")
    private String maturity;

    @NotBlank(message = "客户主要意向产品不能为空!")
    @ApiModelProperty(value = "客户主要意向产品（外键）")
    private String productId;

    @NotNull(message = "主要联系人不能为空")
    @Valid
    private Linkman linkman;
}

