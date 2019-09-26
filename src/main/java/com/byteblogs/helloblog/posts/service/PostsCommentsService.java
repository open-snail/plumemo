package com.byteblogs.helloblog.posts.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.helloblog.posts.domain.po.PostsComments;
import com.byteblogs.helloblog.posts.domain.vo.PostsCommentsVO;

/**
 * <p>
 * 评论表 服务类
 * </p>
 * @author byteblogs
 * @since 2019-09-03
 */
public interface PostsCommentsService extends BaseService<PostsComments> {

    /**
     * 新增评论
     * @param postsCommentsVO
     * @return
     */
    Result savePostsComments(PostsCommentsVO postsCommentsVO);

    /**
     * 根据文章的主键查询评论列表
     * @param postsCommentsVO
     * @return
     */
    Result getPostsCommentsByPostsIdList(PostsCommentsVO postsCommentsVO);

    /**
     * 查询评论列表
     * @param postsCommentsVO
     * @return
     */
    Result getPostsCommentsList(PostsCommentsVO postsCommentsVO);

    /**
     * 删除评论
     * @param id
     * @return
     */
    Result deletePostsComments(Long id);


}
