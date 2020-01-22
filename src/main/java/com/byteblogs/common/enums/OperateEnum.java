package com.byteblogs.common.enums;


import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public enum OperateEnum {
    GET_POSTS_DEFAULT("000","默认类型"),
    GET_POSTS_LIST("001","查询文章列表"),
    GET_POSTS_DETAIL("002","查询文章详情");

    private final String code;
    private final String name;

    private final static Map<String, OperateEnum> codeMap = new HashMap<>();

    static {
        for (OperateEnum operateEnum : OperateEnum.values()) {
            codeMap.put(operateEnum.code, operateEnum);
        }
    }

    OperateEnum(String code,String name) {
        this.code = code;
        this.name=name;
    }

    public String getCode() {
        return code;
    }

    public static String getName(@NotBlank String code) {
        OperateEnum operateEnum=codeMap.get(code);
        if (operateEnum!=null){
            return operateEnum.name;
        }
        return "";
    }

    public static OperateEnum getOperateEnum(@NotBlank String code){
        return codeMap.get(code);
    }
}