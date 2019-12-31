package com.byteblogs.helloblog.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.constant.ErrorConstants;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.JwtUtil;
import com.byteblogs.helloblog.auth.dao.AuthTokenDao;
import com.byteblogs.helloblog.auth.dao.AuthUserDao;
import com.byteblogs.helloblog.auth.domain.po.AuthToken;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.OauthService;
import com.byteblogs.system.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;

/**
 * @author: byteblogs
 * @date: 2019/09/04 08:58
 */
@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    @Autowired
    private AuthUserDao authUserDao;

    @Autowired
    private AuthTokenDao authTokenDao;

    @Override
    public String oauthLoginByGithub() {
        String result = HttpUtil.get(MessageFormat.format(Constants.BYTE_BLOGS_OAUTH_LOGIN, Base64.encode("scope=helloblog")));
        log.debug("oauthLoginByGithub {}", result);
        return result;
    }

    @Override
    public Result saveUserByGithub(AuthUserVO authUserVO) {
        log.debug("saveUserByGithub {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getSocialId())) {
            ExceptionUtil.rollback("参数异常", ErrorConstants.PARAM_INCORRECT);
        }

        AuthUser authUser = this.authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getSocialId, authUserVO.getSocialId()));
        if (authUser == null) {
            authUser = new AuthUser();
            authUser.setSocialId(authUserVO.getSocialId());
            authUser.setAvatar(authUserVO.getAvatar());
            authUser.setName(authUserVO.getName());
            authUser.setRoleId(RoleEnum.USER.getRoleId());
            authUser.setPassword(SecureUtil.hmacMd5(RandomStringUtils.random(32)).digestHex(authUserVO.getSocialId()));
            authUser.setHtmlUrl(authUserVO.getHtmlUrl());
            authUser.setCreateTime(LocalDateTime.now());
            this.authUserDao.insert(authUser);
        }else{
            if (authUser.getStatus() == Constants.ONE){
                ExceptionUtil.rollback("账户已被禁用,请联系管理员解除限制", ErrorConstants.PARAM_INCORRECT);
            }
        }
        authUserVO.setCreateTime(LocalDateTime.now());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setName(authUser.getName()).setId(authUser.getId()));

        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));
        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result saveAdminByGithub(AuthUserVO authUserVO) {
        log.debug("saveAdminByGithub {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getSocialId())) {
            ExceptionUtil.rollback("参数异常", ErrorConstants.DATA_NO_EXIST);
        }

        AuthUser authUser;
        Integer count = this.authUserDao.selectCount(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        if (count.equals(Constants.ZERO)) {
            authUser = new AuthUser();
            authUser.setSocialId(authUserVO.getSocialId());
            authUser.setAvatar(authUserVO.getAvatar());
            authUser.setName(authUserVO.getName());
            authUser.setRoleId(RoleEnum.ADMIN.getRoleId());
            authUser.setPassword(SecureUtil.hmacMd5(RandomStringUtils.random(32)).digestHex(authUserVO.getSocialId()));
            authUser.setHtmlUrl(authUserVO.getHtmlUrl());
            authUser.setCreateTime(LocalDateTime.now());
            this.authUserDao.insert(authUser);
        } else {
            authUser = this.authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()).eq(AuthUser::getSocialId, authUserVO.getSocialId()));
            if (authUser == null) {
                ExceptionUtil.rollback("无权限", ErrorConstants.ACCESS_NO_PRIVILEGE);
            }
        }

        authUserVO.setRoles(Collections.singletonList(RoleEnum.getEnumTypeMap().get(authUser.getRoleId()).getRoleName()));
        authUserVO.setCreateTime(authUser.getCreateTime());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setName(authUser.getName()).setId(authUser.getId()));
        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));

        return Result.createWithModel(authUserVO);
    }
}
