package com.byteblogs.helloblog.links.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.impl.BaseServiceImpl;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.links.dao.FriendshipLinkDao;
import com.byteblogs.helloblog.links.domain.po.FriendshipLink;
import com.byteblogs.helloblog.links.domain.vo.FriendshipLinkVO;
import com.byteblogs.helloblog.links.service.FriendshipLinkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author byteblogs
 * @since 2019-09-07
 */
@Service
public class FriendshipLinkServiceImpl extends BaseServiceImpl<FriendshipLinkDao, FriendshipLink> implements FriendshipLinkService {

    @Autowired
    private FriendshipLinkDao friendshipLinkDao;

    @Override
    public Result getFriendshipLinkList(FriendshipLinkVO friendshipLinkVO) {
        Page<FriendshipLink> page = Optional.of(PageUtil.checkAndInitPage(friendshipLinkVO)).orElse(PageUtil.initPage());
        return Result.createWithPaging(getFriendshipLinkVOList(friendshipLinkVO,page), PageUtil.initPageInfo(page));
    }

    @Override
    public Result getFriendshipLinkMap(FriendshipLinkVO friendshipLinkVO) {
        Map<String, List<FriendshipLinkVO>> resultMap = getFriendshipLinkVOList(friendshipLinkVO,null)
                .stream().filter(s->!StringUtils.isBlank(s.getTitle()))
                .collect(Collectors.groupingBy(FriendshipLinkVO::getTitle, Collectors.toList()));
        return Result.createWithModels(null).setExtra(resultMap);
    }

    @Override
    public Result updateFriendshipLink(FriendshipLinkVO friendshipLinkVO) {
        if(friendshipLinkVO.getId()!=null){
            this.friendshipLinkDao.updateById(
                    new FriendshipLink()
                            .setDescription(friendshipLinkVO.getDescription())
                            .setTitle(friendshipLinkVO.getTitle())
                            .setHref(friendshipLinkVO.getHref())
                            .setLogo(friendshipLinkVO.getLogo())
                            .setName(friendshipLinkVO.getName())
                            .setId(friendshipLinkVO.getId())
                            .setSort(friendshipLinkVO.getSort())
            );

            return Result.createWithSuccessMessage();
        }
        return Result.createWithError();
    }

    @Override
    public Result deleteFriendshipLink(Long id) {
        this.friendshipLinkDao.deleteById(id);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result saveFriendshipLink(FriendshipLinkVO friendshipLinkVO) {

        this.friendshipLinkDao.insert(
                new FriendshipLink()
                        .setDescription(friendshipLinkVO.getDescription())
                        .setTitle(friendshipLinkVO.getTitle())
                        .setHref(friendshipLinkVO.getHref())
                        .setLogo(friendshipLinkVO.getLogo())
                        .setName(friendshipLinkVO.getName())
                        .setSort(friendshipLinkVO.getSort())
        );

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getFriendshipLink(Long id) {
        FriendshipLink friendshipLink = this.friendshipLinkDao.selectById(id);
        if (friendshipLink == null) {
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }
        FriendshipLinkVO friendshipLinkVO=new FriendshipLinkVO()
                .setDescription(friendshipLink.getDescription())
                .setTitle(friendshipLink.getTitle())
                .setHref(friendshipLink.getHref())
                .setLogo(friendshipLink.getLogo())
                .setName(friendshipLink.getName())
                .setId(friendshipLink.getId())
                .setSort(friendshipLink.getSort());
        return Result.createWithModel(friendshipLinkVO);
    }

    public List<FriendshipLinkVO> getFriendshipLinkVOList(FriendshipLinkVO friendshipLinkVO,Page<FriendshipLink> page) {
        LambdaQueryWrapper<FriendshipLink> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(friendshipLinkVO.getKeywords())) {
            objectLambdaQueryWrapper.and(i -> i.like(FriendshipLink::getName, friendshipLinkVO.getKeywords()));
        }
        if (StringUtils.isNotBlank(friendshipLinkVO.getHref())) {
            objectLambdaQueryWrapper.like(FriendshipLink::getHref,friendshipLinkVO.getHref());
        }
        if (StringUtils.isNotBlank(friendshipLinkVO.getName())) {
            objectLambdaQueryWrapper.eq(FriendshipLink::getName,friendshipLinkVO.getName());
        }
        List<FriendshipLink> friendshipLinks;
        if (null==page){
            friendshipLinks=friendshipLinkDao.selectList(objectLambdaQueryWrapper.orderByDesc(FriendshipLink::getSort));
        }else{
            friendshipLinks=friendshipLinkDao.selectPage(page, objectLambdaQueryWrapper.orderByDesc(FriendshipLink::getSort)).getRecords();
        }
        List<FriendshipLinkVO> friendshipLinkVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendshipLinks)) {
            friendshipLinks.forEach(friendshipLink -> {
                friendshipLinkVOList.add(new FriendshipLinkVO()
                        .setName(friendshipLink.getName())
                        .setTitle(friendshipLink.getTitle())
                        .setDescription(friendshipLink.getDescription())
                        .setHref(friendshipLink.getHref())
                        .setLogo(friendshipLink.getLogo())
                        .setId(friendshipLink.getId())
                        .setSort(friendshipLink.getSort())
                );
            });
        }
        return friendshipLinkVOList;
    }
}
