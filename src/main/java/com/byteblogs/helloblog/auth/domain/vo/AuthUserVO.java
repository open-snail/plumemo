package com.byteblogs.helloblog.auth.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.IntegerNotNull;
import com.byteblogs.helloblog.auth.domain.validator.UpdateUsers;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@Accessors(chain = true)
public class AuthUserVO extends BaseVO<AuthUserVO> {

    /**
     * 主键
     */
    @IntegerNotNull(groups = {UpdateUsers.class})
    private Long id;

    /**
     * 社交账户ID
     */
    private String socialId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    private String passwordOld;

    private String verifyCode;

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

    private String token;

    private List<String> roles;

    private String introduction;

    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    private String accessKey;

    private String secretKey;

}
