package com.lzl.saleservice.entity.sale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SalesTargetVo {

    @ApiModelProperty(value = "目标ID")
    private String targetId;

    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @ApiModelProperty(value = "目标设定者名称")
    private String setterName;

    @ApiModelProperty(value = "目标执行者名称")
    private String executorName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "目标开始时间")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "目标结束时间")
    private Date endDate;

    @ApiModelProperty(value = "销售目标状态，0-未完成，1-已完成")
    private String status;


}
