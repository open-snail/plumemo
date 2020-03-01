package com.byteblogs.helloblog.dashboard.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;

/**
 * @author: byteblogs
 * @date: 2019/09/03 18:56
 */
public interface DashboardService {

    /**
     * 评论数量统计
     *
     * @return
     */
    Result getPostsQuantityTotal();

    /**
     * 获取浏览量折线图
     *
     * @return
     */
    Result getPostsStatistics(AuthUserLogVO authUserLogVO);

    /**
     * 获取文章排名
     *
     * @return
     */
    Result getPostsRanking(AuthUserLogVO authUserLogVO);
}
