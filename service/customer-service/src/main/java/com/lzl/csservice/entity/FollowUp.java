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
@ApiModel(value="FollowUp对象", description="")
public class FollowUp implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "追踪记录编号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @NotBlank(message = "客户成熟度为空!")
    @ApiModelProperty(value = "客户成熟度")
    private String maturity;

    @NotBlank(message = "详细信息为空!")
    @ApiModelProperty(value = "详细信息")
    private String content;

    @ApiModelProperty(value = "客户编号")
    private String customerId;

    @ApiModelProperty(value = "跟进人")
    private String managerId;

    @ApiModelProperty(value = "删除状态")
    private Integer deleteStatus;


}
