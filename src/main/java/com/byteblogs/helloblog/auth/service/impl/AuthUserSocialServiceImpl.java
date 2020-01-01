package com.byteblogs.helloblog.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.auth.dao.AuthUserSocialDao;
import com.byteblogs.helloblog.auth.domain.po.AuthUserSocial;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import com.byteblogs.helloblog.auth.service.AuthUserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthUserSocialServiceImpl extends ServiceImpl<AuthUserSocialDao,AuthUserSocial> implements AuthUserSocialService{

    @Autowired
    private AuthUserSocialDao authUserSocialDao;

    @Override
    public Result saveAuthUserSocial(AuthUserSocialVO authUserSocialVO) {
        if (authUserSocialVO!=null){
            AuthUserSocial authUserSocial=new AuthUserSocial()
                    .setCode(authUserSocialVO.getCode())
                    .setShowType(authUserSocialVO.getShowType())
                    .setAccount(authUserSocialVO.getAccount())
                    .setIcon(authUserSocialVO.getIcon())
                    .setQrCode(authUserSocialVO.getQrCode())
                    .setRemark(authUserSocialVO.getRemark())
                    .setUrl(authUserSocialVO.getUrl())
                    .setIsDeleted(0)
                    .setCreateTime(LocalDateTime.now());
            authUserSocialDao.insert(authUserSocial);
            return Result.createWithSuccessMessage();
        }
        return Result.createWithError();
    }
}
