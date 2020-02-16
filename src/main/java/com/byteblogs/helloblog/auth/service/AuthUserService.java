package com.byteblogs.helloblog.auth.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
public interface AuthUserService extends BaseService<AuthUser> {

    /**
     * 获取用户信息
     *
     * @param authUserVO
     * @return
     */
    Result getUserInfo(AuthUserVO authUserVO);

    /**
     * 获取作者信息
     *
     * @return
     */
    Result getMasterUserInfo();

    /**
     * 获取用户列表
     *
     * @param authUserVO
     * @return
     */
    Result getUserList(AuthUserVO authUserVO);

    /**
     * 退出登录
     *
     * @return
     */
    Result logout();

    /**
     * 更新管理员个人资料
     *
     * @param authUserVO
     * @return
     */
    Result updateAdmin(AuthUserVO authUserVO);

    /**
     * 更新个人资料
     *
     * @param authUserVO
     * @return
     */
    Result updateUser(AuthUserVO authUserVO);

    Result saveAuthUserStatus(AuthUserVO authUserVO);

    Result deleteUsers(Long id);

    String getAvatar();
}
