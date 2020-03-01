package com.byteblogs.helloblog.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.common.util.SessionUtil;
import com.byteblogs.helloblog.auth.dao.AuthTokenDao;
import com.byteblogs.helloblog.auth.dao.AuthUserDao;
import com.byteblogs.helloblog.auth.domain.po.AuthToken;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.AuthUserService;
import com.byteblogs.system.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserDao, AuthUser> implements AuthUserService {

    @Autowired
    private AuthUserDao authUserDao;

    @Autowired
    private AuthTokenDao authTokenDao;

    @Override
    public Result getUserInfo(AuthUserVO authUserVO) {
        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        AuthUser authUser = authUserDao.selectById(userSessionInfo.getId());
        return Result.createWithModel(new AuthUserVO()
                .setStatus(authUser.getStatus())
                .setRoles(Collections.singletonList(RoleEnum.getEnumTypeMap().get(authUser.getRoleId()).getRoleName()))
                .setName(authUser.getName())
                .setIntroduction(authUser.getIntroduction())
                .setAvatar(authUser.getAvatar())
                .setEmail(authUser.getEmail())
        );
    }

    @Override
    public Result getMasterUserInfo() {
        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        AuthUserVO authUserVO = new AuthUserVO();
        if (authUser != null) {
            authUserVO.setName(authUser.getName())
                    .setIntroduction(authUser.getIntroduction())
                    .setEmail(authUser.getEmail())
                    .setAvatar(authUser.getAvatar());
        }

        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result getUserList(AuthUserVO authUserVO) {
        Page page = Optional.ofNullable(PageUtil.checkAndInitPage(authUserVO)).orElse(PageUtil.initPage());
        LambdaQueryWrapper<AuthUser> authUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(authUserVO.getKeywords())) {
            authUserLambdaQueryWrapper.like(AuthUser::getName, authUserVO.getKeywords());
        }
        if (StringUtils.isNotBlank(authUserVO.getName())) {
            authUserLambdaQueryWrapper.eq(AuthUser::getName, authUserVO.getName());
        }
        if (authUserVO.getStatus() != null) {
            authUserLambdaQueryWrapper.eq(AuthUser::getStatus, authUserVO.getStatus());
        }

        IPage<AuthUser> authUserIPage = authUserDao.selectPage(page, authUserLambdaQueryWrapper.orderByDesc(AuthUser::getRoleId).orderByDesc(AuthUser::getCreateTime));
        List<AuthUser> records = authUserIPage.getRecords();

        List<AuthUserVO> authUserVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(authUser -> {
                authUserVOList.add(new AuthUserVO()
                        .setId(authUser.getId())
                        .setStatus(authUser.getStatus())
                        .setName(authUser.getName())
                        .setRoleId(authUser.getRoleId())
                        .setIntroduction(authUser.getIntroduction())
                        .setStatus(authUser.getStatus())
                );
            });
        }

        return Result.createWithPaging(authUserVOList, PageUtil.initPageInfo(page));
    }

    @Override
    public Result logout() {
        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        authTokenDao.delete(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId, userSessionInfo.getId()));
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result updateAdmin(AuthUserVO authUserVO) {

        if (authUserVO == null) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        authUserDao.updateById(new AuthUser()
                .setId(userSessionInfo.getId())
                .setEmail(authUserVO.getEmail())
                .setAvatar(authUserVO.getAvatar())
                .setName(authUserVO.getName())
                .setIntroduction(authUserVO.getIntroduction())
        );

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result updateUser(AuthUserVO authUserVO) {
        if (authUserVO == null) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        authUserDao.updateById(new AuthUser()
                .setId(authUserVO.getId())
                .setEmail(authUserVO.getEmail())
                .setAvatar(authUserVO.getAvatar())
                .setName(authUserVO.getName())
                .setIntroduction(authUserVO.getIntroduction())
                .setStatus(authUserVO.getStatus())
        );
        // 锁定了账户，强制用户下线
        if (authUserVO.getStatus() == Constants.ONE) {
            authTokenDao.delete(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId, authUserVO.getId()));
        }
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result saveAuthUserStatus(AuthUserVO authUserVO) {
        if (authUserVO.getStatus() != null
                && authUserVO.getId() != null
                && authUserDao.selectCount(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getId, authUserVO.getId()).eq(AuthUser::getRoleId, Constants.TWO)) == 0) {
            authUserDao.updateById(new AuthUser().setId(authUserVO.getId()).setStatus(authUserVO.getStatus()));
            return Result.createWithSuccessMessage();
        }
        return Result.createWithError();
    }

    @Override
    public Result deleteUsers(Long id) {
        if (id != null && authUserDao.selectCount(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getId, id).eq(AuthUser::getRoleId, Constants.TWO)) == 0) {
            authUserDao.deleteById(id);
            return Result.createWithSuccessMessage();
        }
        return Result.createWithError();
    }

    @Override
    public String getAvatar() {
        return authUserDao.selectAvatar();
    }
}
