package com.byteblogs.common.enums;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public enum PostsStatusEnum {

    /**
     * 草稿
     */
    DRAFT(1, "草稿"),

    /**
     * 发布
     */
    PUBLISH(2, "发布");

    PostsStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    private final Integer status;
    private final String name;

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}