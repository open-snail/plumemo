package com.byteblogs.helloblog.dashboard.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;

/**
 * @author: byteblogs
 * @date: 2019/09/03 18:56
 */
public interface DashboardService {

    /**
     * 评论数量统计
     * @return
     */
    Result getPostsQuantityTotal();

    /**
     * 获取ByteBlogs的文章列表
     * @param postsVO
     * @return
     */
    String getByteBlogsList(PostsVO postsVO);

    /**
     * 获取ByteBlogs的文章列表
     * @param postsVO
     * @return
     */
    String getByteBlogsChatList(PostsVO postsVO);
}
