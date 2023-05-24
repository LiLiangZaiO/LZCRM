package com.lzl.saleservice.entity.sale;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2023-04-15
 */
@Data
public class SalesTarget implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "目标ID")
    @TableId(value = "target_id", type = IdType.ID_WORKER_STR)
    private String targetId;

    @NotBlank(message = "目标任务不能为空!")
    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @ApiModelProperty(value = "目标设定者ID")
    private String setterId;

    @NotBlank(message = "目标执行者不能为空!")
    @ApiModelProperty(value = "目标执行者ID")
    private String executorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "目标开始时间")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "目标结束时间")
    private Date endDate;

    @NotBlank(message = "销售目标状态不能为空!")
    @ApiModelProperty(value = "销售目标状态，0-未完成，1-已完成")
    private String status;


}
