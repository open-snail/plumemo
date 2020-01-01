package com.byteblogs.helloblog.auth.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.helloblog.auth.domain.po.AuthUserSocial;
import com.byteblogs.helloblog.auth.domain.validator.UpdateUsers;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.AuthUserService;
import com.byteblogs.helloblog.auth.service.AuthUserSocialService;
import com.byteblogs.helloblog.auth.service.OauthService;
import com.byteblogs.system.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private OauthService oauthService;

    @Autowired
    private AuthUserSocialService authUserSocialService;

    @LoginRequired(role = RoleEnum.USER)
    @GetMapping("/user/v1/get")
    public Result getUserInfo(AuthUserVO authUserVO) {
        return authUserService.getUserInfo(authUserVO);
    }

    @LoginRequired(role = RoleEnum.ADMIN)
    @GetMapping("/status/v1/update")
    public Result saveAuthUserStatus(@Validated({UpdateUsers.class}) AuthUserVO authUserVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return authUserService.saveAuthUserStatus(authUserVO);
    }

    @GetMapping("/master/v1/get")
    public Result getMasterUserInfo() {
        return authUserService.getMasterUserInfo();
    }

    @LoginRequired
    @GetMapping("/user/v1/list")
    public Result getUserList(AuthUserVO authUserVO) {
        return authUserService.getUserList(authUserVO);
    }

    @GetMapping("/github/v1/get")
    public String oauthLoginByGithub() {
        return oauthService.oauthLoginByGithub();
    }

    @PostMapping("/user/v1/login")
    public Result saveUserByGithub(@RequestBody AuthUserVO authUserVO) {
        return oauthService.saveUserByGithub(authUserVO);
    }

    @PostMapping("/admin/v1/login")
    public Result saveAdminByGithub(@RequestBody AuthUserVO authUserVO) {
        return oauthService.saveAdminByGithub(authUserVO);
    }

    @LoginRequired
    @PutMapping("/admin/v1/update")
    public Result updateUser(@RequestBody AuthUserVO authUserVO) {
        return authUserService.updateUser(authUserVO);
    }

    @PostMapping("/auth/v1/logout")
    public Result logout() {
        return authUserService.logout();
    }

    @PostMapping("/social/v1/save")
    public Result saveSocial(AuthUserSocialVO authUserSocialVO){
        return this.authUserSocialService.saveAuthUserSocial(authUserSocialVO);
    }
}
