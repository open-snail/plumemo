package com.byteblogs.helloblog.posts.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import com.byteblogs.helloblog.posts.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byteblogs@aliyun.com
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/posts/v1/list")
    public Result<PostsVO> getPostsList(PostsVO postsVO) {
        return postsService.getPostsList(postsVO);
    }

    @GetMapping("/archive/v1/list")
    public Result<PostsVO> getArchiveTotalByDateList(PostsVO postsVO) {
        return postsService.getArchiveTotalByDateList(postsVO);
    }

    @LoginRequired
    @PostMapping("/posts/v1/add")
    public Result savePosts(@RequestBody PostsVO postsVO) {
        return postsService.savePosts(postsVO);
    }

    @LoginRequired
    @PostMapping("/byte-blogs/v1/publish")
    public Result publishByteBlogs(@RequestBody PostsVO postsVO) {
        return postsService.publishByteBlogs(postsVO);
    }

    @GetMapping("/posts/v1/{id}")
    public Result getPosts(@PathVariable Long id) {
        return this.postsService.getPosts(id);
    }

    @LoginRequired
    @DeleteMapping("/posts/v1/{id}")
    public Result deletePosts(@PathVariable Long id) {
        return this.postsService.deletePosts(id);
    }

    @PutMapping("/posts/v1/update")
    @LoginRequired
    public Result updatePosts(@RequestBody PostsVO postsVO) {
        return this.postsService.updatePosts(postsVO);
    }

    @PutMapping("/status/v1/update")
    @LoginRequired
    public Result updatePostsStatus(@RequestBody PostsVO postsVO) {
        return this.postsService.updatePostsStatus(postsVO);
    }

    @PostMapping("/posts/v1/crawler")
    @LoginRequired
    public Result getPlatformArticle(@RequestBody PostsVO postsVO) {
        return this.postsService.getPlatformArticle(postsVO);
    }

}
