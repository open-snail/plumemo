package com.byteblogs.helloblog.posts.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.annotation.OperateLog;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.enums.OperateEnum;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.common.validator.group.Update;
import com.byteblogs.helloblog.posts.domain.validator.CrawlerPosts;
import com.byteblogs.helloblog.posts.domain.validator.InsertPosts;
import com.byteblogs.helloblog.posts.domain.validator.UpdatePosts;
import com.byteblogs.helloblog.posts.domain.validator.UpdateStatus;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import com.byteblogs.helloblog.posts.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @OperateLog(module = "文章列表", code=OperateEnum.GET_POSTS_LIST)
    @GetMapping("/posts/v1/list")
    public Result<PostsVO> getPostsList(PostsVO postsVO) {
        return postsService.getPostsList(postsVO);
    }

    @OperateLog(module = "权重列表", code=OperateEnum.GET_POSTS_LIST)
    @GetMapping("/weight/v1/list")
    public Result<PostsVO> getWeightList(PostsVO postsVO) {
        postsVO.setIsWeight(1);
        return postsService.getPostsList(postsVO);
    }

    @GetMapping("/archive/v1/list")
    public Result<PostsVO> getArchiveTotalByDateList(PostsVO postsVO) {
        return postsService.getArchiveTotalByDateList(postsVO);
    }

    @GetMapping("/hot/v1/list")
    public Result getHotPostsList(PostsVO postsVO) {
        return postsService.getHotPostsList(postsVO);
    }

    @LoginRequired
    @PostMapping("/posts/v1/add")
    public Result savePosts(@Validated({InsertPosts.class}) @RequestBody PostsVO postsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return postsService.savePosts(postsVO);
    }

    @LoginRequired
    @PostMapping("/byte-blogs/v1/publish")
    public Result publishByteBlogs(@Validated({Update.class}) @RequestBody PostsVO postsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return postsService.publishByteBlogs(postsVO);
    }

    @OperateLog(module = "获取文章", code=OperateEnum.GET_POSTS_DETAIL)
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
    public Result updatePosts(@Validated({UpdatePosts.class}) @RequestBody PostsVO postsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.postsService.updatePosts(postsVO);
    }

    @PutMapping("/status/v1/update")
    @LoginRequired
    public Result updatePostsStatus(@Validated({UpdateStatus.class}) @RequestBody PostsVO postsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.postsService.updatePostsStatus(postsVO);
    }

    @PostMapping("/posts/v1/crawler")
    @LoginRequired
    public Result getPlatformArticle(@Validated({CrawlerPosts.class}) @RequestBody PostsVO postsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.postsService.getPlatformArticle(postsVO);
    }

}
