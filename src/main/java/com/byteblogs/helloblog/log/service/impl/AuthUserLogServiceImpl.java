package com.byteblogs.helloblog.log.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.impl.BaseServiceImpl;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.enums.OperateEnum;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.log.dao.AuthUserLogDao;
import com.byteblogs.helloblog.log.domain.po.AuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.log.service.AuthUserLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 用户行为日志记录表:业务接口实现类
 */
@Service
@Transactional
public class AuthUserLogServiceImpl extends BaseServiceImpl<AuthUserLogDao, AuthUserLog> implements AuthUserLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserLogServiceImpl.class);
    
    @Autowired
    private AuthUserLogDao authUserLogDao;


    @Override
    public Result saveLogs(AuthUserLogVO authUserLogVO){
        logger.debug("saveLogs AuthUserLog ,the authUserLogVO is {}", authUserLogVO.toString());
        AuthUserLog authUserLog =new AuthUserLog();
        authUserLog.setIp(authUserLogVO.getIp())
                .setCreateTime(authUserLogVO.getCreateTime())
                .setDescription(authUserLogVO.getDescription())
                .setDevice(authUserLogVO.getDevice())
                .setParameter(authUserLogVO.getParameter())
                .setUrl(authUserLogVO.getUrl())
                .setCode(authUserLogVO.getCode())
                .setUserId(authUserLogVO.getUserId())
                .setRunTime(authUserLogVO.getRunTime())
                .setBrowserName(authUserLogVO.getBrowserName())
                .setBrowserVersion(authUserLogVO.getBrowserVersion());
        authUserLogDao.insert(authUserLog);
        return Result.createWithSuccessMessage();
    }

    @Override
    public  Result<AuthUserLogVO> getLogs(Long id){
        logger.debug("getLogs id by {}",id);
        AuthUserLog authUserLog = authUserLogDao.selectById(id);
        AuthUserLogVO authUserLogVO=new AuthUserLogVO();
        authUserLogVO.setId(authUserLog.getId())
                .setIp(authUserLogVO.getIp())
                .setCodeName(OperateEnum.getName(authUserLog.getCode()))
                .setCreateTime(authUserLog.getCreateTime())
                .setParameter(authUserLog.getParameter())
                .setUserId(authUserLog.getUserId())
                .setRunTime(authUserLog.getRunTime())
                .setBrowserName(authUserLog.getBrowserName())
                .setBrowserVersion(authUserLog.getBrowserVersion())
                .setDevice(authUserLog.getDevice())
                .setDescription(authUserLog.getDescription())
                .setUrl(authUserLog.getUrl()) ;
        return Result.createWithModel(authUserLogVO);
    }

    @Override
    public Result<AuthUserLogVO> getLogsList(AuthUserLogVO authUserLogVO) {
        logger.debug("queryPage AuthUserLog ,the entity is {}", authUserLogVO.toString());
        authUserLogVO = Optional.ofNullable(authUserLogVO).orElse(new AuthUserLogVO());

        Page page = Optional.ofNullable(PageUtil.checkAndInitPage(authUserLogVO)).orElse(PageUtil.initPage());
        if (StringUtils.isNotBlank(authUserLogVO.getKeywords())) {
            authUserLogVO.setKeywords("%" + authUserLogVO.getKeywords() + "%");
        }
        List<AuthUserLogVO> logVOList = authUserLogDao.selectLogsList(page, authUserLogVO);
        logVOList.forEach(obj->obj.setCodeName(OperateEnum.getName(obj.getCode())));
        return Result.createWithPaging(logVOList, PageUtil.initPageInfo(page));
    }

    @Override
    public Result<AuthUserLogVO> deleteLogs(Long id) {
        authUserLogDao.deleteById(id);
        return Result.createWithSuccessMessage();
    }
}