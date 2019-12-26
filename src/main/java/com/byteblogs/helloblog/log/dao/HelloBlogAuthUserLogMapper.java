package com.byteblogs.helloblog.log.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.log.domain.po.HelloBlogAuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.HelloBlogAuthUserLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户行为日志记录表:数据层
 */
public interface HelloBlogAuthUserLogMapper extends BaseDao<HelloBlogAuthUserLog> {
    List<HelloBlogAuthUserLog> selectLogsList(@Param("page") Page<HelloBlogAuthUserLog> page,@Param("condition") HelloBlogAuthUserLogVO condition);
}
