package com.byteblogs.common.exception;

import com.byteblogs.common.enums.ErrorEnum;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4214098630082643472L;
    // 异常编码
    private String code;

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg(errorEnum.getCode()));
        this.code = errorEnum.getCode();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
