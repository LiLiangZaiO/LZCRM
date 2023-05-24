package com.lzl.csservice.entity.customerLoss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerLossReprieveVo {

    @ApiModelProperty(value = "流失暂缓操作ID")
    private String id;

    @ApiModelProperty(value = "流失客户名称")
    private String lossName;

    @ApiModelProperty(value = "跟进人名称")
    private String managerName;

    @ApiModelProperty(value = "流失暂缓操作")
    private String measure;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "流失状态")
    private String lossStatus;

}
