package com.byteblogs.plumemo.posts.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 19:26
 * @modified:
 */
@Data
@Accessors(chain = true)
public class BlogMoveVO {

    @NotBlank(message = "Mysql用户名不能为空")
    private String username;

    @NotBlank(message = "Mysql密码不能为空")
    private String password;

    @NotNull(message = "端口不能为空")
    private Integer port;

    @NotBlank(message = "数据库不能为空")
    private String database;

    @NotBlank
    private String ip;

    @NotBlank
    private String blogType;
}
