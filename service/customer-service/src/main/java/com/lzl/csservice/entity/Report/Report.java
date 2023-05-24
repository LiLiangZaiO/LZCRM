package com.lzl.csservice.entity.Report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Report {

    @JsonFormat(pattern = "yyyy-MM")
    private Date monthDate;


    @JsonFormat(pattern = "yyyy")
    private Date yearDate;
}
