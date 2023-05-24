package com.lzl.csservice.entity.customerLoss;

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
 * <p>
 * 
 * </p>
 *
 * @author LiZeLin
 * @since 2023-04-16
 */
@Data
public class CustomerLossReprieve implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "流失暂缓操作ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @NotBlank(message = "流失记录ID不能为空！")
    @ApiModelProperty(value = "流失记录ID")
    private String lossId;

    @NotBlank(message = "流失客户ID不能为空！")
    @ApiModelProperty(value = "流失客户ID")
    private String lossCustomerId;

    @ApiModelProperty(value = "跟进人")
    private String managerId;

    @NotBlank(message = "流失暂缓操作不能为空！")
    @ApiModelProperty(value = "流失暂缓操作")
    private String measure;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @NotBlank(message = "流失状态不能为空！")
    @ApiModelProperty(value = "流失状态")
    private String lossStatus;


}
