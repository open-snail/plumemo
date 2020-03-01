package com.byteblogs.helloblog.auth.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("hello_blog_auth_user")
public class AuthUser extends Model<AuthUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String socialId;

    /**
     * 别名
     */
    private String name;

    /**
     * 角色主键 1 普通用户 2 admin
     */
    private Long roleId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 密码
     */
    private String password;

    /**
     * 个人介绍
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String introduction;

    /**
     * 用户状态 0 正常 1 锁定
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    private String accessKey;

    private String secretKey;

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
