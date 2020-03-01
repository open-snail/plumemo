package com.byteblogs.helloblog.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.JwtUtil;
import com.byteblogs.common.util.SessionUtil;
import com.byteblogs.common.util.ToolUtil;
import com.byteblogs.helloblog.auth.dao.AuthTokenDao;
import com.byteblogs.helloblog.auth.dao.AuthUserDao;
import com.byteblogs.helloblog.auth.domain.po.AuthToken;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.OauthService;
import com.byteblogs.helloblog.bean.SystemPropertyBean;
import com.byteblogs.helloblog.dto.HttpResult;
import com.byteblogs.helloblog.integration.ByteBlogsClient;
import com.byteblogs.helloblog.integration.dto.UserDTO;
import com.byteblogs.system.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public HttpResult oauthLoginByGithub() {

        HttpResult httpResult = BeanTool.getBean(ByteBlogsClient.class).githubAuthorize(Base64.encode("scope=helloblog"));
        log.debug("oauthLoginByGithub {}", httpResult);
        return httpResult;
    }

    @Override
    public Result saveUserByGithub(AuthUserVO authUserVO) {
        log.debug("saveUserByGithub {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getSocialId())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getSocialId, authUserVO.getSocialId()));
        if (authUser == null) {
            authUser = new AuthUser();
            authUser.setSocialId(authUserVO.getSocialId());
            authUser.setAvatar(authUserVO.getAvatar());
            authUser.setName(authUserVO.getName());
            authUser.setRoleId(RoleEnum.USER.getRoleId());
            authUser.setPassword(SecureUtil.hmacMd5(RandomStringUtils.random(32)).digestHex(authUserVO.getSocialId()));
            authUser.setCreateTime(LocalDateTime.now());
            authUserDao.insert(authUser);
        } else {
            if (Constants.ONE == ToolUtil.getInteger(authUser.getStatus())) {
                ExceptionUtil.rollback(ErrorEnum.LOGIN_DISABLE);
            }
        }
        authUserVO.setCreateTime(LocalDateTime.now());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setName(authUser.getName()).setId(authUser.getId()));

        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));
        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result login(AuthUserVO authUserVO) {
        log.debug("login {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getEmail()) || StringUtils.isBlank(authUserVO.getPassword())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId())
                .eq(AuthUser::getEmail, authUserVO.getEmail()));
        ExceptionUtil.isRollback(authUser == null, ErrorEnum.ACCOUNT_NOT_EXIST);

        String psw = SecureUtil.md5(authUserVO.getPassword());
        ExceptionUtil.isRollback(!authUser.getPassword().equals(psw), ErrorEnum.PASSWORD_ERROR);

        authUserVO.setRoles(Collections.singletonList(RoleEnum.getEnumTypeMap().get(authUser.getRoleId()).getRoleName()));
        authUserVO.setCreateTime(authUser.getCreateTime());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setName(authUser.getName()).setId(authUser.getId()));
        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));

        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result registerAdmin(UserDTO userDTO) {

        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        if (authUser == null) {

            AuthUserVO authUserVO = fetchRegister(userDTO);
            authUser = new AuthUser();
            authUser.setName(userDTO.getEmail());
            authUser.setEmail(userDTO.getEmail());
            authUser.setRoleId(RoleEnum.ADMIN.getRoleId());
            authUser.setPassword(SecureUtil.md5(userDTO.getPassword()));
            authUser.setCreateTime(LocalDateTime.now());
            authUser.setAccessKey(authUserVO.getAccessKey()).setSecretKey(authUserVO.getSecretKey());
            authUserDao.insert(authUser);

            // 初始化社交ID
            SystemPropertyBean systemPropertyBean = BeanTool.getBean(SystemPropertyBean.class);
            systemPropertyBean.setAccessKey(authUserVO.getAccessKey());
            systemPropertyBean.setSecretKey(authUserVO.getSecretKey());
        } else {
            if (StringUtils.isBlank(authUser.getAccessKey()) || StringUtils.isBlank(authUser.getSecretKey())) {
                AuthUserVO authUserVO = fetchRegister(userDTO);

                authUserDao.update(authUser
                                .setEmail(userDTO.getEmail())
                                .setPassword(SecureUtil.md5(userDTO.getPassword()))
                                .setAccessKey(authUserVO.getAccessKey())
                                .setSecretKey(authUserVO.getSecretKey()),
                        new LambdaUpdateWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
            } else {
                ExceptionUtil.isRollback(true, ErrorEnum.ACCOUNT_EXIST);
            }
        }

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result updatePassword(AuthUserVO authUserVO) {

        if (StringUtils.isBlank(authUserVO.getPassword()) || StringUtils.isBlank(authUserVO.getPasswordOld())) {
            ExceptionUtil.isRollback(true, ErrorEnum.PARAM_ERROR);
        }

        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();

        AuthUser authUser = authUserDao.selectById(userSessionInfo.getId());
        if (!SecureUtil.md5(authUserVO.getPasswordOld()).equals(authUser.getPassword())) {
            ExceptionUtil.isRollback(true, ErrorEnum.UPDATE_PASSWORD_ERROR);
        }

        authUserDao.updateById(new AuthUser().setId(userSessionInfo.getId()).setPassword(SecureUtil.md5(authUserVO.getPassword())));

        return Result.createWithSuccessMessage();
    }

    private AuthUserVO fetchRegister(UserDTO userDTO) {
        HttpResult httpResult = BeanTool.getBean(ByteBlogsClient.class).registerUsers(userDTO);

        if (httpResult.getSuccess() == Constants.NO) {
            ExceptionUtil.rollback(httpResult.getMessage(), httpResult.getResultCode());
        }

        return JSONObject.parseObject(JSONObject.toJSONString(httpResult.getModel()), AuthUserVO.class);
    }
}
