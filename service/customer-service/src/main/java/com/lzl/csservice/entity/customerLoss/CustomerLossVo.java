package com.lzl.csservice.entity.customerLoss;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class CustomerLossVo {

    @ApiModelProperty(value = "客户流失管理id")
    private String id;

    @ApiModelProperty(value = "流失用户的id")
    private String customerId;

    @ApiModelProperty(value = "流失用户的名称")
    private String customerName;

    @ApiModelProperty(value = "确认流失时间")
    private Date lossDate;

    @ApiModelProperty(value = "流失原因")
    private String reason;

    @ApiModelProperty(value = "流失状态 0 将要流失  1挽回客户 2确认流失")
    private String status;

}
