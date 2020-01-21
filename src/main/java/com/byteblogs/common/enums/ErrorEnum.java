package com.byteblogs.common.enums;

import com.byteblogs.common.constant.ResultConstants;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum ErrorEnum {

    SUCCESS(ResultConstants.OPERATION_SUCCESS, ResultConstants.SUCCESS_MESSAGE, ""),
    ERROR(ResultConstants.OPERATION_FAIL, ResultConstants.ERROR_MESSAGE, ""),

    DATA_NO_EXIST("00002", "该数据不存在", "data not exists"),
    PARAM_ERROR("00003", "参数错误", ""),
    LOGIN_DISABLE("00004", "账户已被禁用,请联系管理员解除限制", ""),
    LOGIN_ERROR("00005", "登录失败，用户名或密码错误", ""),
    ACCESS_NO_PRIVILEGE("00006", "不具备访问权限", ""),
    PARAM_INCORRECT("00007", "传入参数有误", ""),
    INVALID_TOKEN("00008", "token解析失败", "");

    private final static Map<String, ErrorEnum> errorEnumMap = new HashMap<>();

    static {
        for (ErrorEnum errorEnum : ErrorEnum.values()) {
            errorEnumMap.put(errorEnum.code, errorEnum);
        }
    }

    private final String code;
    private final String zhMsg;
    private final String enMsg;

    ErrorEnum(String code, String zhMsg, String enMsg) {
        this.code = code;
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    public String getCode() {
        return code;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public String getZhMsg() {
        return zhMsg;
    }

    public String getMsg(@NotBlank String code) {
        if (SystemLanguageEnum.EN.getCode().equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            return errorEnumMap.get(code).enMsg;
        } else {
            return errorEnumMap.get(code).zhMsg;
        }
    }

    public static ErrorEnum getErrorEnumMap(@NotBlank String code) {
        return errorEnumMap.get(code);
    }
}
