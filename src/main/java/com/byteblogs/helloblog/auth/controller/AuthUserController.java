package com.byteblogs.helloblog.auth.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.helloblog.auth.domain.validator.UpdateUsers;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.AuthUserService;
import com.byteblogs.helloblog.auth.service.AuthUserSocialService;
import com.byteblogs.helloblog.auth.service.OauthService;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.system.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/user/v1/{id}")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result deleteUser(@PathVariable Long id){
        return authUserService.deleteUsers(id);
    }


    @LoginRequired(role = RoleEnum.ADMIN)
    @PutMapping("/status/v1/update")
    public Result saveAuthUserStatus(@RequestBody AuthUserVO authUserVO) {
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
    public Result updateAdmin(@RequestBody AuthUserVO authUserVO) {
        return authUserService.updateAdmin(authUserVO);
    }

    @LoginRequired
    @PutMapping("/user/v1/update")
    public Result updateUser(@RequestBody AuthUserVO authUserVO) {
        return authUserService.updateUser(authUserVO);
    }


    @PostMapping("/auth/v1/logout")
    public Result logout() {
        return authUserService.logout();
    }

    @PostMapping("/social/v1/add")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result saveSocial(@RequestBody AuthUserSocialVO authUserSocialVO) {
        ExceptionUtil.isRollback(StringUtils.isBlank(authUserSocialVO.getCode()), ErrorEnum.PARAM_ERROR);
        return this.authUserSocialService.saveAuthUserSocial(authUserSocialVO);
    }

    @PutMapping("/social/v1/update")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result editSocial(@RequestBody AuthUserSocialVO authUserSocialVO) {
        ExceptionUtil.isRollback(authUserSocialVO.getId() == null, ErrorEnum.PARAM_ERROR);
        return this.authUserSocialService.editAuthUserSocial(authUserSocialVO);
    }

    @GetMapping("/social/v1/{id}")
    public Result getSocial(@PathVariable("id") Long id) {
        return this.authUserSocialService.getSocial(id);
    }

    @GetMapping("/social/v1/list")
    public Result getSocialList(AuthUserSocialVO authUserSocialVO) {
        return authUserSocialService.getSocialList(authUserSocialVO);
    }

    @GetMapping("/social/v1/info")
    public Result getSocialInfo(){
        return authUserSocialService.getSocialInfo();
    }

}
