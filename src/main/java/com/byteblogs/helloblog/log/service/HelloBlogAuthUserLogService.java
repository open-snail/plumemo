package com.byteblogs.helloblog.log.service;


import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.helloblog.log.domain.po.HelloBlogAuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.HelloBlogAuthUserLogVO;

/**
 * 用户行为日志记录表:业务接口类
 * @author generator
 * @date 2019-12-25 09:10:17
 * @since 1.0
 */
public interface HelloBlogAuthUserLogService extends BaseService<HelloBlogAuthUserLog> {


    Result saveLogs(HelloBlogAuthUserLogVO helloBlogAuthUserLogVO);

    /**
     * 查询用户行为日志记录表
     */
    Result getLogs(Long id);

    
    /**
     * 分页查询用户行为日志记录表
     */
    Result getLogsList(HelloBlogAuthUserLogVO helloBlogAuthUserLogVO);
}