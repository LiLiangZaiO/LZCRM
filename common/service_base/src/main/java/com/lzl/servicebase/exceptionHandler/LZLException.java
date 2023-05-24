package com.lzl.servicebase.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LZLException extends RuntimeException{

    private Integer code;  //状态码

    private String msg;  //异常信息

}
