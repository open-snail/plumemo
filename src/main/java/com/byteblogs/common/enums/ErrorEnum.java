package com.byteblogs.common.enums;

import com.byteblogs.common.constant.ResultConstants;

public enum ErrorEnum {
    SUCCESS(ResultConstants.OPERATION_SUCCESS, ResultConstants.SUCCESS_MESSAGE),
    ERROR(ResultConstants.OPERATION_FAIL, ResultConstants.ERROR_MESSAGE),

    DATA_NO_EXIST("00002", "该数据不存在"),
    PARAM_ERROR("00003", "参数错误"),
    LOGIN_DISABLE("00004", "账户已被禁用,请联系管理员解除限制"),
    LOGIN_ERROR("00005", "登录失败，用户名或密码错误"),
    ACCESS_NO_PRIVILEGE("00006", "不具备访问权限"),
    PARAM_INCORRECT("00007", "传入参数有误"),
    INVALID_TOKEN("00008", "token解析失败"),
    ;

    private String code;
    private String msg;

    ErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }
}
