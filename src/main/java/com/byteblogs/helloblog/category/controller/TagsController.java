package com.byteblogs.helloblog.category.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.common.validator.annotion.NotNull;
import com.byteblogs.common.validator.group.Insert;
import com.byteblogs.common.validator.group.Update;
import com.byteblogs.helloblog.category.domain.vo.TagsVO;
import com.byteblogs.helloblog.category.service.TagsService;
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
 * @author byteblogs
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    @GetMapping("/tags/v1/list")
    public Result getTagsList(TagsVO tagsVO) {
        return this.tagsService.getTagsList(tagsVO);
    }

    @GetMapping("/tags-article-quantity/v1/list")
    public Result getTagsAndArticleQuantityList(TagsVO tagsVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.tagsService.getTagsAndArticleQuantityList(tagsVO);
    }

    @GetMapping("/tags/v1/{id}")
    public Result getTags(@PathVariable Long id) {
        return this.tagsService.getTags(id);
    }

    @LoginRequired
    @PostMapping("/tags/v1/add")
    public Result saveTags(@Validated({Insert.class}) @RequestBody TagsVO tagsVO,BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.tagsService.saveTags(tagsVO);
    }

    @LoginRequired
    @PutMapping("/tags/v1/update")
    public Result updateTags(@Validated({Update.class}) @RequestBody TagsVO tagsVO,BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return this.tagsService.updateTags(tagsVO);
    }

    @LoginRequired
    @DeleteMapping("/tags/v1/{id}")
    public Result updateTags(@PathVariable Long id) {
        return this.tagsService.deleteTags(id);
    }
}
