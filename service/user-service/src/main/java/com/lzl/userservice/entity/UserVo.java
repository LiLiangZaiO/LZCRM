package com.lzl.userservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserVo {

    @ApiModelProperty(value = "用户id")
    private String id;

    @NotBlank(message = "用户账号不能为空!")
    @ApiModelProperty(value = "用户账号")
    private String username;

    @NotBlank(message = "密码不能为空!")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "真实姓名不能为空!")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @NotBlank(message = "邮箱不能为空!")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @NotBlank(message = "手机号不能为空!")
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "上一次登陆时间开始")
    private String lastLoginTimeStart;

    @ApiModelProperty(value = "上一次登陆时间结束")
    private String lastLoginTimeEnd;

    @ApiModelProperty(value = "职位列表Id")
    private String roleId;

    @ApiModelProperty(value = "账号锁定状态 0 为未锁定 1为1锁定")
    private String status;

}
