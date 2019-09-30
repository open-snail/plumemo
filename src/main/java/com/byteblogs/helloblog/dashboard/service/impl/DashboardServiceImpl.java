package com.byteblogs.helloblog.dashboard.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.constant.ErrorConstants;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.helloblog.dashboard.service.DashboardService;
import com.byteblogs.helloblog.posts.dao.PostsDao;
import com.byteblogs.helloblog.posts.domain.po.Posts;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import com.byteblogs.system.enums.ArticleStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * @author: byteblogs
 * @date: 2019/09/03 18:56
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PostsDao postsDao;

    @Override
    public Result getPostsQuantityTotal() {
        PostsVO postsVO = Optional.ofNullable(postsDao.selectPostsTotal()).orElse(new PostsVO().setViewsTotal(Constants.ZERO).setCommentsTotal(Constants.ZERO));
        Integer article = this.postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.PUBLISH.getStatus()));
        postsVO.setArticleTotal(article);
        Integer draft = this.postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.DRAFT.getStatus()));
        postsVO.setDraftTotal(draft);
        return Result.createWithModel(postsVO);
    }

    @Override
    public String getByteBlogsList(PostsVO postsVO) {
        return HttpUtil.get(MessageFormat.format(Constants.BYTE_BLOGS_ARTICLE_LIST, postsVO.getPage(), postsVO.getSize()));
    }

    @Override
    public String getByteBlogsChatList(PostsVO postsVO) {
        return HttpUtil.get(MessageFormat.format(Constants.BYTE_BLOGS_CHAT_LIST, postsVO.getPage(), postsVO.getSize()));
    }
}
