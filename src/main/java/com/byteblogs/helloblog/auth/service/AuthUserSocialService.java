package com.byteblogs.helloblog.auth.service;


import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.helloblog.auth.domain.po.AuthUserSocial;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;

/**
 * 用户表社交信息表:业务接口类
 * @author nosum
 */
public interface AuthUserSocialService extends BaseService<AuthUserSocial> {

    Result saveAuthUserSocial(AuthUserSocialVO authUserSocialVO);

    Result editAuthUserSocial(AuthUserSocialVO authUserSocialVO);

    Result getSocial(Long id);

    Result getSocialList(AuthUserSocialVO authUserSocialVO);

    Result getSocialInfo();

    Result delSocial(Long id);
}