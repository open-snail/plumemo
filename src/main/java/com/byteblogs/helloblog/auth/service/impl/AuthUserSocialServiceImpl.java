package com.byteblogs.helloblog.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
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
import java.util.ArrayList;
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
                .setContent(authUserSocialVO.getContent())
                .setRemark(authUserSocialVO.getRemark())
                .setIcon(authUserSocialVO.getIcon())
                .setIsEnabled(authUserSocialVO.getIsEnabled())
                .setIsHome(authUserSocialVO.getIsHome())
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
                .setContent(authUserSocialVO.getContent())
                .setRemark(authUserSocialVO.getRemark())
                .setIcon(authUserSocialVO.getIcon())
                .setIsEnabled(authUserSocialVO.getIsEnabled())
                .setIsHome(authUserSocialVO.getIsHome())
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

    @Override
    public Result getSocialInfo() {
        List<AuthUserSocial> authUserSocialList=this.authUserSocialDao.selectList(new LambdaQueryWrapper<AuthUserSocial>().eq(AuthUserSocial::getIsHome,1).eq(AuthUserSocial::getIsEnabled,1));
        List<AuthUserSocialVO> authUserSocialVOList=new ArrayList<>();
        authUserSocialList.forEach(authUserSocial->{
            authUserSocialVOList.add(new AuthUserSocialVO()
                    .setIcon(authUserSocial.getIcon())
                    .setCode(authUserSocial.getCode())
                    .setShowType(authUserSocial.getShowType())
                    .setContent(authUserSocial.getContent())
                    .setRemark(authUserSocial.getRemark())
                    .setIsEnabled(authUserSocial.getIsEnabled())
                    .setIsHome(authUserSocial.getIsHome())
                    .setUpdateTime(LocalDateTime.now()));
        });
        return Result.createWithModels(authUserSocialVOList);
    }


    @Override
    public Result delSocial(Long id) {
        if (id!=null){
            this.authUserSocialDao.deleteById(id);
        }
        return Result.createWithSuccessMessage();
    }
}
