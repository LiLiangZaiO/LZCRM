package com.lzl.servicebase.exceptionHandler;

import com.lzl.common_utils.domain.R;
import com.lzl.common_utils.domain.ResultCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobaExceptionHandler {

    /**
     * LZLException   自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(LZLException.class)
    @ResponseBody
    public R error(LZLException e){
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

    /**
     * 当对带有@Valid注释的参数的验证失败时抛出异常。从 5.3 开始扩展BindException 。
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handler(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().stream().findFirst().get();
        return R.error().code(ResultCode.ERROR).message(error.getDefaultMessage());

    }



}
