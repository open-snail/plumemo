package com.byteblogs.common.enums;

public enum OperateEnum {


    /**
     * 默认类型
     */
    GET_POSTS_DEFAULT("000"),

    /**
     * 查询文章列表
     */
    GET_POSTS_LIST("001"),

    /**
     * 查询文章详情
     */
    GET_POSTS_DETAIL("002");

    private final String code;

    OperateEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}