package com.byteblogs.helloblog.log.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.log.domain.po.AuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户行为日志记录表:数据层
 */
public interface AuthUserLogDao extends BaseDao<AuthUserLog> {
    List<AuthUserLog> selectLogsList(@Param("page") Page<AuthUserLog> page, @Param("condition") AuthUserLogVO condition);

    List<AuthUserLogVO> selectListByCode(String code);
}
