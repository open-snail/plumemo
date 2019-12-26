package com.byteblogs.helloblog.log.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.impl.BaseServiceImpl;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.log.dao.HelloBlogAuthUserLogMapper;
import com.byteblogs.helloblog.log.domain.po.HelloBlogAuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.HelloBlogAuthUserLogVO;
import com.byteblogs.helloblog.log.service.HelloBlogAuthUserLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * 用户行为日志记录表:业务接口实现类
 */
@Service
@Transactional
public class HelloBlogAuthUserLogServiceImpl extends BaseServiceImpl<HelloBlogAuthUserLogMapper, HelloBlogAuthUserLog> implements HelloBlogAuthUserLogService {
    private static final Logger logger = LoggerFactory.getLogger(HelloBlogAuthUserLogServiceImpl.class);
    
    @Autowired
    private HelloBlogAuthUserLogMapper helloBlogAuthUserLogMapper;


    @Override
    public Result saveLogs(HelloBlogAuthUserLogVO helloBlogAuthUserLogVO){
        logger.debug("saveLogs HelloBlogAuthUserLog ,the helloBlogAuthUserLogVO is {}",helloBlogAuthUserLogVO.toString());
        HelloBlogAuthUserLog helloBlogAuthUserLog=new HelloBlogAuthUserLog();
        helloBlogAuthUserLog.setIp(helloBlogAuthUserLogVO.getIp())
                .setCreateTime(helloBlogAuthUserLogVO.getCreateTime())
                .setDescription(helloBlogAuthUserLogVO.getDescription())
                .setDevice(helloBlogAuthUserLogVO.getDevice())
                .setParamter(helloBlogAuthUserLogVO.getParamter())
                .setUrl(helloBlogAuthUserLogVO.getUrl())
                .setCode(helloBlogAuthUserLogVO.getCode())
                .setUserId(helloBlogAuthUserLogVO.getUserId())
                .setRunTime(helloBlogAuthUserLogVO.getRunTime())
                .setBrowserName(helloBlogAuthUserLogVO.getBrowserName())
                .setBrowserVersion(helloBlogAuthUserLogVO.getBrowserVersion());
        helloBlogAuthUserLogMapper.insert(helloBlogAuthUserLog);
        return Result.createWithSuccessMessage();
    }

    @Override
    public  Result getLogs(Long id){
        logger.debug("getLogs id by {}",id);
        return Result.createWithModel(helloBlogAuthUserLogMapper.selectById(id));
    }

    @Override
    public Result getLogsList(HelloBlogAuthUserLogVO helloBlogAuthUserLogVO) {
        logger.debug("queryPage HelloBlogAuthUserLog ,the entity is {}",helloBlogAuthUserLogVO.toString());
        helloBlogAuthUserLogVO = Optional.ofNullable(helloBlogAuthUserLogVO).orElse(new HelloBlogAuthUserLogVO());

        Page page = Optional.ofNullable(PageUtil.checkAndInitPage(helloBlogAuthUserLogVO)).orElse(PageUtil.initPage());
        if (StringUtils.isNotBlank(helloBlogAuthUserLogVO.getKeywords())) {
            helloBlogAuthUserLogVO.setKeywords("%" + helloBlogAuthUserLogVO.getKeywords() + "%");
        }
        return Result.createWithModels(helloBlogAuthUserLogMapper.selectLogsList(page,helloBlogAuthUserLogVO));
    }
}