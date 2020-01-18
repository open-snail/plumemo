package com.byteblogs.helloblog.log.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.log.domain.po.AuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为日志记录表:数据层
 */
public interface AuthUserLogDao extends BaseDao<AuthUserLog> {

    List<AuthUserLog> selectLogsList(@Param("page") Page<AuthUserLog> page, @Param("condition") AuthUserLogVO condition);

    List<AuthUserLogVO> selectListByCode(String code);

    /**
     * 查询文章访问统计
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AuthUserLogVO> selectPostsListStatistics(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,@Param("type") String type);

    /**
     * 查询文章的排名
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AuthUserLogVO> selectPostsRanking(Page page,@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
