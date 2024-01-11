package com.xuecheng.base.handler;

/*
   @Class:GlobalExceptionHandler
   @Date:2024/1/11  22:55
*/

import com.xuecheng.base.exception.BaseException;
import com.xuecheng.base.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常捕获
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse customException(BaseException e) {
        log.error("【系统异常】{}",e.getMessage(),e);
        return new ExceptionResponse(e.getMessage());
    }

    /**
     * 系统内部未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse globalException(Exception e) {
        log.error("【系统异常】{}",e.getMessage(),e);
        return new ExceptionResponse(e.getMessage());
    }



}
