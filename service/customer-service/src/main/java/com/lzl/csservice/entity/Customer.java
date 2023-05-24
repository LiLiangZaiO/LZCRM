package com.lzl.csservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author LiZeLin
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Customer对象", description="")
public class Customer implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "客户编号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @NotBlank(message = "客户名称不能为空!")
    @ApiModelProperty(value = "客户名称（唯一性）")
    private String name;

    @NotBlank(message = "客户类别不能为空!")
    @ApiModelProperty(value = "客户类别")
    private String type;

    @NotBlank(message = "客户等级不能为空!")
    @ApiModelProperty(value = "客户等级")
    private String level;

    @NotBlank(message = "客户状态不能为空!")
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

    @ApiModelProperty(value = "客户描述")
    private String description;

    @NotBlank(message = "客户成熟度不能为空!")
    @ApiModelProperty(value = "客户成熟度")
    private String maturity;

    @ApiModelProperty(value = "删除状态 0为未删除 1为已删除")
    private Integer deleteStatus;

    @ApiModelProperty(value = "客户所属的客户经理（外键）")
    private String managerId;

    @NotBlank(message = "客户主要意向产品不能为空!")
    @ApiModelProperty(value = "客户主要意向产品（外键）")
    private String productId;

    @ApiModelProperty(value = "创建人（经理）")
    private String createrId;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
