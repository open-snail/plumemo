package com.byteblogs.plumemo.posts.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.plumemo.posts.domain.po.PostsComments;
import com.byteblogs.plumemo.posts.domain.vo.PostsCommentsVO;

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
     */
    Result savePostsComments(PostsCommentsVO postsCommentsVO);

    /**
     * 根据文章的主键查询评论列表
     */
    Result getPostsCommentsByPostsIdList(PostsCommentsVO postsCommentsVO);

    /**
     * 查询评论列表
     */
    Result getPostsCommentsList(PostsCommentsVO postsCommentsVO);

    /**
     * 删除评论
     */
    Result deletePostsComments(Long id);


    Result getPostsComment(Long id);

    Result replyComments(PostsCommentsVO postsCommentsVO);

}
