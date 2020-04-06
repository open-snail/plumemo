package com.byteblogs.plumemo.posts.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.plumemo.posts.domain.validator.InsertPostsComments;
import com.byteblogs.plumemo.posts.domain.validator.QueryPostsComments;
import com.byteblogs.plumemo.posts.domain.vo.PostsCommentsVO;
import com.byteblogs.plumemo.posts.service.PostsCommentsService;
import com.byteblogs.system.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @LoginRequired(role = RoleEnum.USER)
    @PostMapping("/admin/v1/reply")
    public Result replyComments(@RequestBody PostsCommentsVO postsCommentsVO) {
        return this.postsCommentsService.replyComments(postsCommentsVO);
    }

    @LoginRequired
    @DeleteMapping("/comments/v1/{id}")
    public Result deletePostsComments(@PathVariable(value = "id") Long id) {
        return this.postsCommentsService.deletePostsComments(id);
    }

    @LoginRequired
    @GetMapping("/comments/v1/{id}")
    public Result getPostsComment(@PathVariable(value = "id") Long id) {
        return this.postsCommentsService.getPostsComment(id);
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
