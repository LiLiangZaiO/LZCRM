package com.lzl.saleservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiZeLin
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Product对象", description="")
public class Product implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "序号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @NotBlank(message = "产品名称不能为空!")
    @ApiModelProperty(value = "产品名称")
    private String name;

    @NotBlank(message = "产品仓库地址不能为空!")
    @ApiModelProperty(value = "仓库地址")
    private String location;

    @NotBlank(message = "产品型号不能为空!")
    @ApiModelProperty(value = "型号")
    private String version;

    @NotNull(message = "产品单价不能为空!")
    @ApiModelProperty(value = "单价")
    private Double price;

    @NotNull(message = "产品库存不能为空!")
    @ApiModelProperty(value = "库存")
    private Integer repertory;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @NotBlank(message = "产品分类不能为空!")
    @ApiModelProperty(value = "分类Id")
    private String categoryId;

    @ApiModelProperty(value = "删除状态 0为未删除 1为已删除")
    private Integer deleteStatus;

}
