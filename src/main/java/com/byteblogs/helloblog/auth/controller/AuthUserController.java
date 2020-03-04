package com.byteblogs.helloblog.auth.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserVO;
import com.byteblogs.helloblog.auth.service.AuthUserService;
import com.byteblogs.helloblog.auth.service.AuthUserSocialService;
import com.byteblogs.helloblog.auth.service.OauthService;
import com.byteblogs.helloblog.dto.HttpResult;
import com.byteblogs.helloblog.integration.dto.UserDTO;
import com.byteblogs.system.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public Result deleteUser(@PathVariable Long id) {
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
    public HttpResult oauthLoginByGithub() {
        return oauthService.oauthLoginByGithub();
    }

    @PostMapping("/user/v1/login")
    public Result saveUserByGithub(@RequestBody AuthUserVO authUserVO) {
        return oauthService.saveUserByGithub(authUserVO);
    }

    @PostMapping("/admin/v1/register")
    public Result registerAdminByGithub(@RequestBody UserDTO userDTO) {
        return oauthService.registerAdmin(userDTO);
    }

    @PostMapping("/admin/v1/login")
    public Result adminLogin(@RequestBody AuthUserVO authUserVO) {
        return oauthService.login(authUserVO);
    }

    @LoginRequired
    @PutMapping("/password/v1/update")
    public Result updatePassword(@RequestBody AuthUserVO authUserVO) {
        return oauthService.updatePassword(authUserVO);
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

    @RequestMapping(value = "/auth/v1/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAvatar() {
        return FileUtil.tranToBytes(authUserService.getAvatar());
    }

    @PostMapping("/social/v1/add")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result saveSocial(@RequestBody AuthUserSocialVO authUserSocialVO) {
        ExceptionUtil.isRollback(StringUtils.isBlank(authUserSocialVO.getCode()), ErrorEnum.PARAM_ERROR);
        return authUserSocialService.saveAuthUserSocial(authUserSocialVO);
    }

    @PutMapping("/social/v1/update")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result editSocial(@RequestBody AuthUserSocialVO authUserSocialVO) {
        ExceptionUtil.isRollback(authUserSocialVO.getId() == null, ErrorEnum.PARAM_ERROR);
        return authUserSocialService.editAuthUserSocial(authUserSocialVO);
    }

    @GetMapping("/social/v1/{id}")
    public Result getSocial(@PathVariable("id") Long id) {
        return authUserSocialService.getSocial(id);
    }


    @DeleteMapping("/social/v1/{id}")
    public Result delSocial(@PathVariable("id") Long id) {
        return authUserSocialService.delSocial(id);
    }

    @LoginRequired(role = RoleEnum.ADMIN)
    @GetMapping("/social/v1/list")
    public Result getSocialList(AuthUserSocialVO authUserSocialVO) {
        return authUserSocialService.getSocialList(authUserSocialVO);
    }

    @GetMapping("/social/v1/socials")
    public Result getSocialEnableList(AuthUserSocialVO authUserSocialVO) {
        authUserSocialVO.setIsEnabled(1);
        return authUserSocialService.getSocialList(authUserSocialVO);
    }

    @GetMapping("/social/v1/info")
    public Result getSocialInfo() {
        return authUserSocialService.getSocialInfo();
    }

}
