package com.lzl.csservice.entity.customerLoss;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
@Data
public class CustomerLoss implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "客户流失管理id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "流失用户的id")
    private String customerId;

    @ApiModelProperty(value = "确认流失时间")
    private Date lossDate;

    @ApiModelProperty(value = "流失原因")
    private String reason;

    @ApiModelProperty(value = "流失状态 0 将要流失  1挽回客户 2确认流失")
    private String status;


}
