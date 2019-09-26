package com.byteblogs.helloblog.dashboard.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.dashboard.service.DashboardService;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/byte-blogs/v1/list")
    public String getByteBlogsList(PostsVO postsVO) {
        return dashboardService.getByteBlogsList(postsVO);
    }

    @LoginRequired
    @GetMapping("/byte-blogs-chat/v1/list")
    public String getByteBlogsChatList(PostsVO postsVO) {
        return dashboardService.getByteBlogsChatList(postsVO);
    }

    @LoginRequired
    @GetMapping("/blog-total/v1/quantity")
    public Result getPostsQuantityTotal(PostsVO postsVO) {
        return dashboardService.getPostsQuantityTotal();
    }
}
