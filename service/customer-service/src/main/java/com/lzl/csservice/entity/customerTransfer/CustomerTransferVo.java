package com.lzl.csservice.entity.customerTransfer;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerTransferVo {

    @ApiModelProperty(value = "客户转移记录id")
    private String id;

    @ApiModelProperty(value = "被转移的客户id")
    private String customerName;

    @ApiModelProperty(value = "转移前的客户经理")
    private String oldManagerName;

    @ApiModelProperty(value = "转移后的客户经理")
    private String newManagerName;

    @ApiModelProperty(value = "转移原因")
    private String reason;

    @ApiModelProperty(value = "转移时间")
    private Date createTime;

}
