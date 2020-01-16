package com.byteblogs.helloblog.dashboard.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.DateUtil;
import com.byteblogs.helloblog.dashboard.domain.vo.ViewChartVO;
import com.byteblogs.helloblog.dashboard.service.DashboardService;
import com.byteblogs.helloblog.log.dao.AuthUserLogDao;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.dao.PostsDao;
import com.byteblogs.helloblog.posts.domain.po.Posts;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import com.byteblogs.system.enums.ArticleStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: byteblogs
 * @date: 2019/09/03 18:56
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private AuthUserLogDao authUserLogDao;

    @Override
    public Result getPostsQuantityTotal() {
        PostsVO postsVO = Optional.ofNullable(postsDao.selectPostsTotal()).orElse(new PostsVO().setViewsTotal(Constants.ZERO).setCommentsTotal(Constants.ZERO));
        Integer article = this.postsDao.selectCount(null);
        postsVO.setArticleTotal(article);
        Integer draft = this.postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.DRAFT.getStatus()));
        postsVO.setDraftTotal(draft);
        postsVO.setPublishTotal(article - draft);
        Integer syncTotal = this.postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getSyncStatus, Constants.YES));
        postsVO.setSyncTotal(syncTotal);
        Integer todayPublishTotal = this.postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.PUBLISH.getStatus())
                .between(Posts::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), DateUtil.MAX)));
        postsVO.setTodayPublishTotal(todayPublishTotal);
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

    @Override
    public Result getPostsStatistics() {

        List<AuthUserLogVO> todayList = authUserLogDao.selectPostsListStatistics(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), DateUtil.MAX));
        List<AuthUserLogVO> yesterdayList = authUserLogDao.selectPostsListStatistics(LocalDateTime.of(LocalDate.now().minusDays(Constants.ONE), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now().minusDays(Constants.ONE), DateUtil.MAX));
        todayList.addAll(yesterdayList);

        List<AuthUserLogVO> chartVO = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN).plusHours(i);
            AuthUserLogVO authUserLogVO = new AuthUserLogVO();
            authUserLogVO.setCreateTime(localDateTime);
            for (AuthUserLogVO userLogVO : todayList) {
                boolean equal = userLogVO.getCreateTime().isEqual(localDateTime);
                if (equal){
                    authUserLogVO.setCount(userLogVO.getCount());
                    break;
                } else {
                    authUserLogVO.setCount(Constants.ZERO);
                }
            }

            chartVO.add(authUserLogVO);
        }

        return Result.createWithModels(chartVO).setExtra(chartVO);
    }
}
