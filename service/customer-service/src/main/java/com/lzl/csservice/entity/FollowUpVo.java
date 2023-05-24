package com.lzl.csservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FollowUpVo {

    @ApiModelProperty(value = "追踪记录编号")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "客户成熟度")
    private String maturity;

    @ApiModelProperty(value = "详细信息")
    private String content;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "跟进人名称")
    private String managerName;

}
