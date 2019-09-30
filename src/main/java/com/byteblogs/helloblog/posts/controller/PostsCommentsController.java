package com.byteblogs.helloblog.posts.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.validator.annotion.NotNull;
import com.byteblogs.helloblog.posts.domain.validator.InsertPosts;
import com.byteblogs.helloblog.posts.domain.validator.InsertPostsComments;
import com.byteblogs.helloblog.posts.domain.validator.QueryPostsComments;
import com.byteblogs.helloblog.posts.domain.vo.PostsCommentsVO;
import com.byteblogs.helloblog.posts.service.PostsCommentsService;
import com.byteblogs.system.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byteblogs
 * @since 2019-09-03
 */
@RestController
@RequestMapping("/comments")
public class PostsCommentsController {

    @Autowired
    private PostsCommentsService postsCommentsService;

    @LoginRequired(role = RoleEnum.USER)
    @PostMapping("/comments/v1/add")
    public Result savePostsComments(@Validated({InsertPostsComments.class}) @RequestBody PostsCommentsVO postsCommentsVO) {

        return this.postsCommentsService.savePostsComments(postsCommentsVO);
    }

    @LoginRequired
    @PostMapping("/comments/v1/{id}")
    public Result deletePostsComments(@PathVariable(value = "id") Long id) {
        return this.postsCommentsService.deletePostsComments(id);
    }

    @GetMapping("/comments-posts/v1/list")
    public Result getPostsCommentsByPostsIdList(@Validated({QueryPostsComments.class}) PostsCommentsVO postsCommentsVO) {
        return this.postsCommentsService.getPostsCommentsByPostsIdList(postsCommentsVO);
    }

    @LoginRequired
    @GetMapping("/comments/v1/get")
    public Result getPostsCommentsList(PostsCommentsVO postsCommentsVO) {
        return this.postsCommentsService.getPostsCommentsList(postsCommentsVO);
    }
}
