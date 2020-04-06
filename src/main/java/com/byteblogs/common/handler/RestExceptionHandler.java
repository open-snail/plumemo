package com.byteblogs.common.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.exception.ApiInvalidParamException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        log.error("异常类 HttpMessageNotReadableException {},", ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * validation 异常处理
     *
     * @param request 请求体
     * @param e       ConstraintViolationException
     * @return HttpResult
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result onConstraintViolationException(HttpServletRequest request,
                                                 ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            String errorMessage = constraintViolations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";"));
            return Result.createWithErrorMessage(errorMessage, ErrorEnum.PARAM_ERROR.getCode());
        }
        return Result.createWithErrorMessage(e.getMessage(), ErrorEnum.PARAM_ERROR.getCode());
    }

    /**
     * validation 异常处理
     *
     * @param request 请求体
     * @param e       MethodArgumentNotValidException
     * @return HttpResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result onMethodArgumentNotValidException(HttpServletRequest request,
                                                    MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        if (result != null && result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            if(CollectionUtil.isNotEmpty(errors)){
                FieldError error = errors.get(0);
                String rejectedValue = Objects.toString(error.getRejectedValue(), "");
                String defMsg = error.getDefaultMessage();
                // 排除类上面的注解提示
                if(rejectedValue.contains(Constants.DELIMITER_TO)){
                    // 自己去确定错误字段
                    sb.append(defMsg);
                }else{
                    if(Constants.DELIMITER_COLON.contains(defMsg)){
                        sb.append(error.getField()).append(" ").append(defMsg);
                    }else{
                        sb.append(error.getField()).append(" ").append(defMsg).append(":").append(rejectedValue);
                    }
                }
            } else {
                String msg = result.getAllErrors().get(0).getDefaultMessage();
                sb.append(msg);
            }

            return Result.createWithErrorMessage(sb.toString(), ErrorEnum.PARAM_ERROR.getCode());
        }

        return null;
    }

    /**
     * 400错误
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public Result requestTypeMismatch(TypeMismatchException ex) {
        log.error("异常类 TypeMismatchException {},", ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 400错误
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
        log.error("异常类 MissingServletRequestParameterException {},", ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 405错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result request405() {
        log.error("异常类 HttpRequestMethodNotSupportedException ");
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }

    /**
     * 415错误
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Result request415(HttpMediaTypeNotSupportedException ex) {
        log.error("异常类 HttpMediaTypeNotSupportedException {}", ex.getMessage());
        return Result.createWithErrorMessage(ErrorEnum.PARAM_INCORRECT);
    }
}
