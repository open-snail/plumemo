package com.byteblogs.helloblog.auth.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.dto.HttpResult;
import com.byteblogs.helloblog.integration.dto.UserDTO;

/**
 * @description:
 * @author: byteblogs
 * @date: 2019/09/04 08:58
 */
public interface OauthService {

    /**
     * 获取授权链接
     */
    HttpResult oauthLoginByGithub();

    /**
     * 保存用户信息
     */
    Result saveUserByGithub(AuthUserVO authUserVO);

    /**
     * 保存管理员
     */
    Result login(AuthUserVO authUserVO);

    /**
     * 注册管理员
     *
     * @param userDTO
     * @return
     */
    Result registerAdmin(UserDTO userDTO);

    /**
     * 保存管理员
     */
    Result updatePassword(AuthUserVO authUserVO);
}
