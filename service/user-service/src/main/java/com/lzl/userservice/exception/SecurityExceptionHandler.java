package com.lzl.userservice.exception;

import com.lzl.common_utils.domain.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class SecurityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public R accessDeniedException() {
        return R.error().code(403).message("用户权限不足！");
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public R serverException() {
        return R.error().code(500).message("服务出现异常！");
    }


}
