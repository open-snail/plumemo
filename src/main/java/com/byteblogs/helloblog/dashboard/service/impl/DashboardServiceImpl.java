package com.byteblogs.helloblog.dashboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.DateTypeEnum;
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

import java.time.*;
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
        Integer article = postsDao.selectCount(null);
        postsVO.setArticleTotal(article);
        Integer draft = postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.DRAFT.getStatus()));
        postsVO.setDraftTotal(draft);
        postsVO.setPublishTotal(article - draft);
        Integer syncTotal = postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getSyncStatus, Constants.YES));
        postsVO.setSyncTotal(syncTotal);
        Integer todayPublishTotal = postsDao.selectCount(new LambdaQueryWrapper<Posts>().eq(Posts::getStatus, ArticleStatusEnum.PUBLISH.getStatus())
                .between(Posts::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), DateUtil.MAX)));
        postsVO.setTodayPublishTotal(todayPublishTotal);
        return Result.createWithModel(postsVO);
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

        DateTypeEnum dateTypeEnum = DateTypeEnum.valueOf(authUserLogVO.getType().toUpperCase());
        List<AuthUserLogVO> chartVO = new ArrayList<>();

        switch (dateTypeEnum) {
            case DAY:
                getDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN),
                        48, dateTypeEnum, chartVO, todayList);
                break;
            case WEEK:
                getDate(LocalDateTime.of(LocalDate.now().with(DayOfWeek.of(1)), LocalTime.MIN),
                        7, dateTypeEnum, chartVO, todayList);
                break;
            case MONTH:
                int curMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN),
                        curMonth, dateTypeEnum, chartVO, todayList);

                int preMonth = LocalDateTime.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                        LocalTime.MIN).minusMonths(1), preMonth, dateTypeEnum, chartVO, todayList);
                break;
            case YEAR:
                getDate(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).minusYears(1), LocalTime.MIN),
                        24, dateTypeEnum, chartVO, todayList);
                break;
            default:
                long days = Duration.between(authUserLogVO.getStartTime(), authUserLogVO.getEndTime()).toDays();
                getDate(LocalDateTime.of(authUserLogVO.getStartTime().toLocalDate().minusDays(1), LocalTime.MIN),
                        (int) days, dateTypeEnum, chartVO, todayList);
                break;
        }

        return Result.createWithModels(chartVO);
    }

    /**
     * 查询日期
     *
     * @param localDateTime
     * @param size
     * @param dateTypeEnum
     * @param chartVO
     * @param todayList
     */
    private void getDate(LocalDateTime localDateTime, Integer size, DateTypeEnum dateTypeEnum, List<AuthUserLogVO> chartVO, List<AuthUserLogVO> todayList) {
        for (int i = 0; i < size; i++) {
            AuthUserLogVO authUserLogVO1 = new AuthUserLogVO();
            switch (dateTypeEnum) {
                case DAY:
                    authUserLogVO1.setCreateTime(localDateTime.plusHours(i));
                    break;
                case YEAR:
                    authUserLogVO1.setCreateTime(localDateTime.plusMonths(i));
                    break;
                case WEEK:
                    authUserLogVO1.setCreateTime(localDateTime.plusDays(i));
                    authUserLogVO1.setIndex(authUserLogVO1.getCreateTime().getDayOfWeek().getValue());
                    break;
                default:
                    authUserLogVO1.setCreateTime(localDateTime.plusDays(i));
                    break;
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
                    Posts posts = postsDao.selectById(id);
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
        DateTypeEnum dateTypeEnum = DateTypeEnum.valueOf(authUserLogVO.getType().toUpperCase());
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();

        switch (dateTypeEnum) {
            case DAY:
                startTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN);
                endTime = LocalDateTime.of(endTime.toLocalDate(), DateUtil.MAX);
                break;
            case WEEK:
                startTime = LocalDateTime.of(startTime.toLocalDate().with(DayOfWeek.of(Constants.ONE)), LocalTime.MIN);
                endTime = LocalDateTime.of(endTime.toLocalDate().with(DayOfWeek.of(Constants.SEVEN)), DateUtil.MAX);
                break;
            case MONTH:
                startTime = LocalDateTime.of(startTime.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
                endTime = LocalDateTime.of(endTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()), DateUtil.MAX);
                break;
            case YEAR:
                startTime = LocalDateTime.of(startTime.toLocalDate().with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
                endTime = LocalDateTime.of(endTime.toLocalDate().with(TemporalAdjusters.lastDayOfYear()), DateUtil.MAX);
                break;
            default:
                startTime = authUserLogVO.getStartTime();
                endTime = authUserLogVO.getEndTime();
                if (startTime == null || endTime == null) {
                    startTime = LocalDateTime.now();
                    endTime = LocalDateTime.now();
                }
                break;
        }

        authUserLogVO.setStartTime(startTime).setEndTime(endTime);
    }
}
