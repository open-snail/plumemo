package com.byteblogs.system.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: byteblogs
 * @date: 2019/09/02 16:35
 */
public enum ArticleStatusEnum {

    /**
     * 草稿
     */
    DRAFT(1, "draft"),

    /**
     * 发布
     */
    PUBLISH(2, "publish");

    private static final Map<Integer, ArticleStatusEnum> enumTypeMap = new HashMap<>();

    static {
        for (ArticleStatusEnum roleEnum : ArticleStatusEnum.values()) {
            enumTypeMap.put(roleEnum.getStatus(), roleEnum);
        }
    }

    private final Integer status;

    private final String name;

    ArticleStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static Map<Integer, ArticleStatusEnum> getEnumTypeMap() {
        return enumTypeMap;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
