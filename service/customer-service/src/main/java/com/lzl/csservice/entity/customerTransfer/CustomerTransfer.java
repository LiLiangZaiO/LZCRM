package com.lzl.csservice.entity.customerTransfer;

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
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomerTransfer对象", description="")
public class CustomerTransfer implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "客户转移记录id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @NotBlank(message = "被转移的客户id不能为空!")
    @ApiModelProperty(value = "被转移的客户id")
    private String customerId;

    @ApiModelProperty(value = "转移前的客户经理")
    private String oldManagerId;

    @NotBlank(message = "转移后的客户经理不能为空!")
    @ApiModelProperty(value = "转移后的客户经理")
    private String newManagerId;

    @NotBlank(message = "客户转移原因不能为空!")
    @ApiModelProperty(value = "转移原因")
    private String reason;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "转移时间")
    private Date createTime;


}
