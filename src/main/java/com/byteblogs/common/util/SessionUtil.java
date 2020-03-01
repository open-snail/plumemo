package com.byteblogs.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.helloblog.auth.dao.AuthTokenDao;
import com.byteblogs.helloblog.auth.dao.AuthUserDao;
import com.byteblogs.helloblog.auth.domain.po.AuthToken;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class SessionUtil {

    /**
     * 获取用户Session信息
     *
     * @return
     */
    public static UserSessionVO getUserSessionInfo() {

        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求头Token值
        String token = Optional.ofNullable(request.getHeader(Constants.AUTHENTICATION)).orElse(null);

        if (StringUtils.isBlank(token)) {
            return null;
        }

        // 获取 token 中的 user id
        AuthUser authUser = null;
        try {
            authUser = JsonUtil.parseObject(JWT.decode(token).getAudience().get(0), AuthUser.class);
        } catch (JWTDecodeException j) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        AuthUserDao userDao = BeanTool.getBean(AuthUserDao.class);
        AuthUser user = userDao.selectById(authUser.getId());
        if (user == null) {
            ExceptionUtil.rollback(ErrorEnum.LOGIN_ERROR);
        }

        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        AuthTokenDao authTokenDao = BeanTool.getBean(AuthTokenDao.class);
        Integer count = authTokenDao.selectCount(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getToken, token).eq(AuthToken::getUserId, user.getId()).ge(AuthToken::getExpireTime,
                LocalDateTime.now()));
        if (count.equals(Constants.ZERO)) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        UserSessionVO userSessionVO = new UserSessionVO();
        userSessionVO.setName(user.getName()).setRoleId(user.getRoleId()).setId(user.getId());
        return userSessionVO;
    }

}
