package com.byteblogs.helloblog.links.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.impl.BaseServiceImpl;
import com.byteblogs.common.constant.ErrorConstants;
import com.byteblogs.common.util.ExceptionUtil;
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
        LambdaQueryWrapper<FriendshipLink> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(friendshipLinkVO.getKeywords())) {
            objectLambdaQueryWrapper.and(
                    i -> i.like(FriendshipLink::getName, friendshipLinkVO.getKeywords())
            );
        }

        List<FriendshipLink> friendshipLinkList = friendshipLinkDao.selectList(objectLambdaQueryWrapper.orderByAsc(FriendshipLink::getSort));
        List<FriendshipLinkVO> friendshipLinkVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendshipLinkList)) {
            friendshipLinkList.forEach(friendshipLink -> {
                friendshipLinkVOList.add(new FriendshipLinkVO()
                        .setName(friendshipLink.getName())
                        .setDescription(friendshipLink.getDescription())
                        .setHref(friendshipLink.getHref())
                        .setLogo(friendshipLink.getLogo())
                        .setId(friendshipLink.getId())
                );
            });
        }

        return Result.createWithModels(friendshipLinkVOList);
    }

    @Override
    public Result updateFriendshipLink(FriendshipLinkVO friendshipLinkVO) {

        if (friendshipLinkVO == null || friendshipLinkVO.getId() == null) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        this.friendshipLinkDao.updateById(
                new FriendshipLink()
                        .setDescription(friendshipLinkVO.getDescription())
                        .setHref(friendshipLinkVO.getHref())
                        .setLogo(friendshipLinkVO.getLogo())
                        .setName(friendshipLinkVO.getName())
                        .setId(friendshipLinkVO.getId())
        );

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result deleteFriendshipLink(Long id) {

        if (id == null){
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        this.friendshipLinkDao.deleteById(id);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result saveFriendshipLink(FriendshipLinkVO friendshipLinkVO) {

        if (friendshipLinkVO == null) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        this.friendshipLinkDao.insert(
                new FriendshipLink()
                        .setDescription(friendshipLinkVO.getDescription())
                        .setHref(friendshipLinkVO.getHref())
                        .setLogo(friendshipLinkVO.getLogo())
                        .setName(friendshipLinkVO.getName())
        );

        return Result.createWithSuccessMessage();
    }
}
