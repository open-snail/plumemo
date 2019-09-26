package com.byteblogs.helloblog.auth.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;

/**
 * @description:
 * @author: byteblogs
 * @date: 2019/09/04 08:58
 */
public interface OauthService {

    /**
     * 获取授权链接
     * @return
     */
    String oauthLoginByGithub();

    /**
     * 保存用户信息
     * @param authUserVO
     * @return
     */
    Result saveUserByGithub(AuthUserVO authUserVO);

    /**
     * 保存管理员
     * @param authUserVO
     * @return
     */
    Result saveAdminByGithub(AuthUserVO authUserVO);
}
