package com.byteblogs.helloblog.dashboard.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.dashboard.service.DashboardService;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: byteblogs
 * @date: 2019/09/03 19:25
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @LoginRequired
    @GetMapping("/blog-total/v1/quantity")
    public Result getPostsQuantityTotal(PostsVO postsVO) {
        return dashboardService.getPostsQuantityTotal();
    }

    @LoginRequired
    @GetMapping("/post-statistics/v1/list")
    public Result getPostsStatistics(@Valid AuthUserLogVO authUserLogVO) {
        return dashboardService.getPostsStatistics(authUserLogVO);
    }

    @LoginRequired
    @GetMapping("/post-ranking/v1/list")
    public Result getPostsRanking(@Valid AuthUserLogVO authUserLogVO) {
        return dashboardService.getPostsRanking(authUserLogVO);
    }

}
