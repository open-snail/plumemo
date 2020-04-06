package com.byteblogs.plumemo.auth.dao;

import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.plumemo.auth.domain.po.AuthUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byteblogs
 * @since 2019-08-28
 */
public interface AuthUserDao extends BaseDao<AuthUser> {

    String selectAvatar();
    AuthUser selectAdmin();

}
