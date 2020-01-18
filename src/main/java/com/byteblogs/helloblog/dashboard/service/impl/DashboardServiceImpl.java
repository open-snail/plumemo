package com.byteblogs.helloblog.dashboard.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.DateUtil;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.dashboard.service.DashboardService;
import com.byteblogs.helloblog.log.dao.AuthUserLogDao;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.dao.PostsDao;
import com.byteblogs.helloblog.posts.domain.po.Posts;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import com.byteblogs.system.enums.ArticleStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    public Result getPostsStatistics(AuthUserLogVO authUserLogVO) {

        getTime(authUserLogVO);

        List<AuthUserLogVO> todayList = authUserLogDao.selectPostsListStatistics(
                LocalDateTime.of(authUserLogVO.getStartTime().toLocalDate(), LocalTime.MIN),
                LocalDateTime.of(authUserLogVO.getEndTime().toLocalDate(), DateUtil.MAX),
                authUserLogVO.getType());
        List<AuthUserLogVO> yesterdayList = authUserLogDao.selectPostsListStatistics(
                LocalDateTime.of(authUserLogVO.getStartTime().toLocalDate().minusDays(Constants.ONE), LocalTime.MIN),
                LocalDateTime.of(authUserLogVO.getEndTime().toLocalDate().minusDays(Constants.ONE), DateUtil.MAX),
                authUserLogVO.getType());
        todayList.addAll(yesterdayList);

        List<AuthUserLogVO> chartVO = new ArrayList<>();
        if ("day".equals(authUserLogVO.getType())) {

            LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
            getDate(localDateTime, 48, authUserLogVO.getType(), chartVO, todayList);
        } else if ("week".equals(authUserLogVO.getType())) {

            LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.of(1)).minusWeeks(1), LocalTime.MIN);
            getDate(localDateTime, 14, authUserLogVO.getType(), chartVO, todayList);
        } else if ("month".equals(authUserLogVO.getType())) {

            int curMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN),
                    curMonth, authUserLogVO.getType(), chartVO, todayList);

            int preMonth = LocalDateTime.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                    LocalTime.MIN).minusMonths(1), preMonth, authUserLogVO.getType(), chartVO, todayList);
        } else if ("year".equals(authUserLogVO.getType())) {

            getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).minusYears(1), LocalTime.MIN),
                    24, authUserLogVO.getType(), chartVO, todayList);

        }


        return Result.createWithModels(chartVO).setExtra(chartVO);
    }

    private void getDate(LocalDateTime localDateTime, Integer size, String type, List<AuthUserLogVO> chartVO, List<AuthUserLogVO> todayList) {
        for (int i = 0; i < size; i++) {
            AuthUserLogVO authUserLogVO1 = new AuthUserLogVO();
            if ("day".equals(type)) {
                authUserLogVO1.setCreateTime(localDateTime.plusHours(i));
            } else if ("year".equals(type)) {
                authUserLogVO1.setCreateTime(localDateTime.plusMonths(i));
            } else {
                authUserLogVO1.setCreateTime(localDateTime.plusDays(i));
            }

            for (AuthUserLogVO userLogVO : todayList) {
                boolean equal = userLogVO.getCreateTime().isEqual(authUserLogVO1.getCreateTime());
                if (equal) {
                    authUserLogVO1.setViewTotal(userLogVO.getViewTotal());
                    break;
                } else {
                    authUserLogVO1.setViewTotal(Constants.ZERO);
                }
            }

            chartVO.add(authUserLogVO1);
        }
    }

    @Override
    public Result getPostsRanking(AuthUserLogVO authUserLogVO) {

        if (StringUtils.isBlank(authUserLogVO.getType())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_INCORRECT);
        }

        getTime(authUserLogVO);

        Page page = Optional.of(PageUtil.checkAndInitPage(authUserLogVO)).orElse(PageUtil.initPage());
        List<AuthUserLogVO> authUserLogVOList = authUserLogDao.selectPostsRanking(
                page,
                LocalDateTime.of(authUserLogVO.getStartTime().toLocalDate(), LocalTime.MIN),
                LocalDateTime.of(authUserLogVO.getEndTime().toLocalDate(), DateUtil.MAX));

        AtomicInteger start = new AtomicInteger((int) ((page.getCurrent() - 1) * page.getSize()));
        if (!CollectionUtils.isEmpty(authUserLogVOList)) {
            authUserLogVOList.forEach(authUserLogVO1 -> {
                String parameter = authUserLogVO1.getParameter();
                JSONObject jsonObject = JSONObject.parseObject(parameter);
                if (jsonObject != null) {
                    String id = (String) jsonObject.get("id");
                    Posts posts = this.postsDao.selectById(id);
                    if (posts != null) {
                        authUserLogVO1.setTitle(posts.getTitle());
                    }

                    authUserLogVO1.setParameter(null).setIndex(start.incrementAndGet());
                }
            });
        }

        return Result.createWithPaging(authUserLogVOList, PageUtil.initPageInfo(page));
    }

    private void getTime(AuthUserLogVO authUserLogVO) {

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();
        if ("day".equals(authUserLogVO.getType())) {
            startTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN);
            endTime = LocalDateTime.of(endTime.toLocalDate(), DateUtil.MAX);
        } else if ("week".equals(authUserLogVO.getType())) {
            startTime = LocalDateTime.of(startTime.toLocalDate().with(DayOfWeek.of(Constants.ONE)), LocalTime.MIN);
            endTime = LocalDateTime.of(endTime.toLocalDate().with(DayOfWeek.of(Constants.SEVEN)), DateUtil.MAX);
        } else if ("month".equals(authUserLogVO.getType())) {
            startTime = LocalDateTime.of(startTime.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
            endTime = LocalDateTime.of(endTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()), DateUtil.MAX);
        } else if ("year".equals(authUserLogVO.getType())) {
            startTime = LocalDateTime.of(startTime.toLocalDate().with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
            endTime = LocalDateTime.of(endTime.toLocalDate().with(TemporalAdjusters.lastDayOfYear()), DateUtil.MAX);
        }

        authUserLogVO.setStartTime(startTime).setEndTime(endTime);
    }
}
