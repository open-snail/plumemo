package com.byteblogs.common.handler;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.ResultConstants;
import com.byteblogs.common.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 400 统一异常处理
 * @author: byteblogs
 * @date: 2019/09/30 17:02
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * 400错误
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Result requestNotReadable(HttpMessageNotReadableException ex){
        log.error("异常类 HttpMessageNotReadableException {},",ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 400错误
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public Result requestTypeMismatch(TypeMismatchException ex){
        log.error("异常类 TypeMismatchException {},",ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 400错误
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex){
        log.error("异常类 MissingServletRequestParameterException {},",ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 405错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result request405(){
        log.error("异常类 HttpRequestMethodNotSupportedException ");
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 415错误
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Result request415(HttpMediaTypeNotSupportedException ex){
        log.error("异常类 HttpMediaTypeNotSupportedException {}",ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }
}
