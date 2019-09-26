package com.byteblogs.common.base.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author:byteblogs
 * @Date:2018/09/20 16:58
 */
@Data
@Accessors(chain = true)
public class UserSessionVO {

    private Long id;

    private String socialId;

    private String name;

    private String avatar;

    private Integer gender;

    private Long roleId;

    private String token;

}
