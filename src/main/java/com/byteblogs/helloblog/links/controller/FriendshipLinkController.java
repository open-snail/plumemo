package com.byteblogs.helloblog.links.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.links.domain.vo.FriendshipLinkVO;
import com.byteblogs.helloblog.links.service.FriendshipLinkService;
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
 * @author byteblogs
 * @since 2019-04-03
 */
@RestController
@RequestMapping("/link")
public class FriendshipLinkController {

    @Autowired
    private FriendshipLinkService friendshipLinkService;

    @GetMapping("/link/v1/list")
    public Result getFriendshipLinkList(FriendshipLinkVO friendshipLinkVO) {
        return friendshipLinkService.getFriendshipLinkList(friendshipLinkVO);
    }

    @LoginRequired
    @PostMapping("/link/v1/add")
    public Result saveFriendshipLink(@RequestBody FriendshipLinkVO friendshipLinkVO) {
        return friendshipLinkService.saveFriendshipLink(friendshipLinkVO);
    }

    @LoginRequired
    @PutMapping("/link/v1/update")
    public Result updateFriendshipLink(@RequestBody FriendshipLinkVO friendshipLinkVO) {
        return friendshipLinkService.updateFriendshipLink(friendshipLinkVO);
    }

    @LoginRequired
    @DeleteMapping("/link/v1/{id}")
    public Result deleteFriendshipLink(@PathVariable Long id) {
        return friendshipLinkService.deleteFriendshipLink(id);
    }
}

