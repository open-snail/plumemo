package com.byteblogs.plumemo.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.JsonUtil;
import com.byteblogs.common.util.JwtUtil;
import com.byteblogs.common.util.SessionUtil;
import com.byteblogs.common.util.ToolUtil;
import com.byteblogs.plumemo.auth.dao.AuthTokenDao;
import com.byteblogs.plumemo.auth.dao.AuthUserDao;
import com.byteblogs.plumemo.auth.domain.po.AuthToken;
import com.byteblogs.plumemo.auth.domain.po.AuthUser;
import com.byteblogs.plumemo.auth.domain.vo.AuthUserVO;
import com.byteblogs.plumemo.auth.domain.vo.GithubVO;
import com.byteblogs.plumemo.auth.service.OauthService;
import com.byteblogs.system.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_INFO_URL = "https://api.github.com/user";

    @Value("${github.redirectUri}")
    private String redirectUri;
    @Value("${github.clientId}")
    private String clientId;
    @Value("${github.clientSecret}")
    private String clientSecret;

    @Override
    public Result oauthLoginByGithub() {

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", clientId);

        String authorizeUrl = AUTH_URL + "?" + HttpUtil.toParams(params);
        log.debug("github -> getAuthorizeUrl -> result -> {}", authorizeUrl);
        log.debug("获取授权链接 {}", authorizeUrl);
        Map<String, String> map = new HashMap<>();
        map.put("authorizeUrl", authorizeUrl);

        return Result.createWithModel(map);
    }

    @Override
    public String saveUserByGithub(String code, String state) {

        String accessToken = getAccessToken(code);
        Map<String, Object> objectObjectMap = JsonUtil.parseHashMap(accessToken);

        String userInfo = getUserInfo((String) objectObjectMap.get("access_token"));
        GithubVO githubVO = JsonUtil.parseObject(userInfo, GithubVO.class);
        if (githubVO == null) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("avatar", githubVO.getAvatar_url());
        result.put("name", githubVO.getLogin());
        result.put("htmlUrl", githubVO.getHtml_url());
        result.put("socialId", githubVO.getId());

        String html = "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "</head>" +
                "<body>\n" +
                "   <p style=\"text-align: center;\"><h3>登录中....</h3></p>\n" +
                "</body>" +
                "<script>\n" +
                "  window.onload = function () {\n" +
                "    var message =" + JsonUtil.toJsonString(result) + ";\n" +
                "    window.opener.parent.postMessage(message, '*');\n" +
                "    parent.window.close();\n" +
                "  }\n" +
                "</script>\n";

        log.debug(html);
        return html;
    }

    @Override
    public String getAccessToken(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        HttpRequest post = HttpRequest.post(TOKEN_URL);
        post.body(JsonUtil.toJsonString(params)).contentType("application/json").header(Header.ACCEPT, "application/json");
        String result = post.execute().body();
        log.debug("github -> getAccessToken -> result -> {}", result);
        return result;
    }

    @Override
    public String getUserInfo(String accessToken) {
        String result = HttpRequest.get(USER_INFO_URL).header("Authorization", "bearer " + accessToken).execute().body();
        log.debug("github -> getAccessToken -> result -> {}", result);
        return result;
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
            authUser.setUsername(authUserVO.getNickname());
            authUser.setNickname(authUserVO.getNickname());
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
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setNickname(authUser.getNickname()).setId(authUser.getId()));

        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));
        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result login(AuthUserVO authUserVO) {
        log.debug("login {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getUsername()) || StringUtils.isBlank(authUserVO.getPassword())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId())
                .eq(AuthUser::getUsername, authUserVO.getUsername()));
        ExceptionUtil.isRollback(authUser == null, ErrorEnum.ACCOUNT_NOT_EXIST);

        String psw = SecureUtil.md5(authUserVO.getPassword());
        ExceptionUtil.isRollback(!authUser.getPassword().equals(psw), ErrorEnum.PASSWORD_ERROR);

        authUserVO.setRoles(Collections.singletonList(RoleEnum.getEnumTypeMap().get(authUser.getRoleId()).getRoleName()));
        authUserVO.setCreateTime(authUser.getCreateTime());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setNickname(authUser.getNickname()).setId(authUser.getId()));
        authUserVO.setToken(token);
        authTokenDao.insert(new AuthToken().setUserId(authUser.getId()).setToken(token).setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime()));

        return Result.createWithModel(authUserVO);
    }

    @Override
    public Result registerAdmin(AuthUserVO authUserVO) {

        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        ExceptionUtil.isRollback(authUser != null, ErrorEnum.ACCOUNT_EXIST);

        if (authUser == null) {

            authUser = new AuthUser();
            authUser.setUsername(authUserVO.getUsername());
            authUser.setNickname(authUserVO.getUsername());
            authUser.setEmail(authUserVO.getEmail());
            authUser.setRoleId(RoleEnum.ADMIN.getRoleId());
            authUser.setPassword(SecureUtil.md5(authUserVO.getPassword()));
            authUser.setCreateTime(LocalDateTime.now());
            authUserDao.insert(authUser);

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

    @Override
    public String getAuthorizeUrl(String state) {
        return null;
    }
}
