package com.byteblogs.helloblog.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.auth.dao.AuthUserSocialDao;
import com.byteblogs.helloblog.auth.domain.po.AuthUserSocial;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import com.byteblogs.helloblog.auth.service.AuthUserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthUserSocialServiceImpl extends ServiceImpl<AuthUserSocialDao,AuthUserSocial> implements AuthUserSocialService{

    @Autowired
    private AuthUserSocialDao authUserSocialDao;

    @Override
    public Result saveAuthUserSocial(AuthUserSocialVO authUserSocialVO) {
        AuthUserSocial authUserSocial=new AuthUserSocial()
                .setCode(authUserSocialVO.getCode())
                .setShowType(authUserSocialVO.getShowType())
                .setAccount(authUserSocialVO.getAccount())
                .setIcon(authUserSocialVO.getIcon())
                .setQrCode(authUserSocialVO.getQrCode())
                .setRemark(authUserSocialVO.getRemark())
                .setUrl(authUserSocialVO.getUrl())
                .setIsDeleted(0)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        authUserSocialDao.insert(authUserSocial);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result editAuthUserSocial(AuthUserSocialVO authUserSocialVO) {
        AuthUserSocial authUserSocial=new AuthUserSocial()
                .setId(authUserSocialVO.getId())
                .setCode(authUserSocialVO.getCode())
                .setShowType(authUserSocialVO.getShowType())
                .setAccount(authUserSocialVO.getAccount())
                .setIcon(authUserSocialVO.getIcon())
                .setQrCode(authUserSocialVO.getQrCode())
                .setRemark(authUserSocialVO.getRemark())
                .setUrl(authUserSocialVO.getUrl())
                .setIsDeleted(authUserSocialVO.getIsDeleted())
                .setUpdateTime(LocalDateTime.now());
        authUserSocialDao.updateById(authUserSocial);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getSocial(Long id) {
        AuthUserSocial authUserSocial=this.authUserSocialDao.selectById(id);
        if (authUserSocial==null){
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        return Result.createWithModel(authUserSocial);
    }

    @Override
    public Result getSocialList(AuthUserSocialVO authUserSocialVO) {
        authUserSocialVO= Optional.ofNullable(authUserSocialVO).orElse(new AuthUserSocialVO());
        Page page=Optional.of(PageUtil.checkAndInitPage(authUserSocialVO)).orElse(PageUtil.initPage());
        if (!StringUtils.isEmpty(authUserSocialVO.getKeywords())){
            authUserSocialVO.setKeywords("%"+authUserSocialVO.getKeywords()+"%");
        }
        List<AuthUserSocialVO> authUserSocialList=this.authUserSocialDao.selectSocialList(page,authUserSocialVO);
        return Result.createWithPaging(authUserSocialList,PageUtil.initPageInfo(page));
    }
}
