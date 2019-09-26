package com.byteblogs.system.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: byteblogs
 * @date: 2019/09/02 16:35
 */
public enum RoleEnum {

    /**
     * 普通用户
     */
    USER(1L, "user"),

    /**
     * 管理员
     */
    ADMIN(2L, "admin");

    private static final Map<Long, RoleEnum> enumTypeMap = new HashMap<>();

    static {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            enumTypeMap.put(roleEnum.getRoleId(), roleEnum);
        }
    }

    private final Long roleId;

    private final String roleName;

    RoleEnum(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public static Map<Long, RoleEnum> getEnumTypeMap() {
        return enumTypeMap;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

}
