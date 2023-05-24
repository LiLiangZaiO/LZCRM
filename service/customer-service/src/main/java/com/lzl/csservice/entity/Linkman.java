package com.lzl.csservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-26
 */
@Data
public class Linkman implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "联系人编号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @NotBlank(message = "联系人名称不能为空!")
    @ApiModelProperty(value = "联系人名称")
    private String name;

    @ApiModelProperty(value = "联系人职位")
    private String position;

    @NotBlank(message = "办公司电话不能为空!")
    @ApiModelProperty(value = "办公司电话")
    private String officePhone;

    @NotBlank(message = "手机电话不能为空!")
    @ApiModelProperty(value = "手机电话")
    private String mobilePhone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    @NotBlank(message = "性别不能为空!")
    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "联系等级：0 为主要联系人 1为普通联系人")
    private String level;

    @ApiModelProperty(value = "所属客户")
    private String customerId;


}
