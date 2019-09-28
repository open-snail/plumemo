package com.byteblogs.helloblog.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.ErrorConstants;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.category.dao.CategoryTagsDao;
import com.byteblogs.helloblog.category.dao.TagsDao;
import com.byteblogs.helloblog.category.domain.po.CategoryTags;
import com.byteblogs.helloblog.category.domain.po.Tags;
import com.byteblogs.helloblog.category.domain.vo.TagsVO;
import com.byteblogs.helloblog.category.service.TagsService;
import com.byteblogs.helloblog.posts.dao.PostsTagsDao;
import com.byteblogs.helloblog.posts.domain.po.PostsTags;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsDao, Tags> implements TagsService {

    @Autowired
    private TagsDao tagsDao;

    @Autowired
    private PostsTagsDao postsTagsDao;

    @Autowired
    private CategoryTagsDao categoryTagsDao;

    @Override
    public Result<TagsVO> getTagsAndArticleQuantityList(TagsVO tagsVO) {
        List<Tags> records = this.tagsDao.selectList(new LambdaQueryWrapper<Tags>().orderByDesc(Tags::getCreateTime));

        List<TagsVO> tagsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(tags -> {
                Integer total = postsTagsDao.selectCount(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getTagsId, tags.getId()));
                tagsList.add(new TagsVO().setId(tags.getId()).setPostsTotal(total).setName(tags.getName()));
            });
        }

        return Result.createWithModels(tagsList);
    }

    @Override
    public Result<TagsVO> getTagsList(TagsVO tagsVO) {

        List<TagsVO> tagsList = new ArrayList<>();
        if (tagsVO == null || tagsVO.getPage() == null || tagsVO.getSize() == null) {
            List<Tags> records = this.tagsDao.selectList(new LambdaQueryWrapper<Tags>().orderByDesc(Tags::getCreateTime));

            if (!CollectionUtils.isEmpty(records)) {
                records.forEach(tags -> {
                    tagsList.add(new TagsVO().setId(tags.getId()).setName(tags.getName()));
                });
            }
            return Result.createWithModels(tagsList);
        }

        LambdaQueryWrapper<Tags> tagsLambdaQueryWrapper = new LambdaQueryWrapper<Tags>();
        if (StringUtils.isNotBlank(tagsVO.getKeywords())){
            tagsLambdaQueryWrapper.like(Tags::getName, tagsVO.getKeywords());
        }

        Page page = PageUtil.checkAndInitPage(tagsVO);
        IPage<Tags> tagsIPage = this.tagsDao.selectPage(page,tagsLambdaQueryWrapper.orderByDesc(Tags::getCreateTime));
        List<Tags> records = tagsIPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(tags -> {
                tagsList.add(new TagsVO().setId(tags.getId()).setName(tags.getName()));
            });
        }

        return Result.createWithPaging(tagsList, PageUtil.initPageInfo(page));
    }

    @Override
    public Result<TagsVO> getTags(Long id) {

        if (id == null) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        Tags tags = this.tagsDao.selectById(id);
        return Result.createWithModel(new TagsVO().setId(tags.getId()).setName(tags.getName()));
    }

    @Override
    public Result<TagsVO> updateTags(TagsVO tagsVO) {

        if (tagsVO == null || tagsVO.getId() == null || StringUtils.isBlank(tagsVO.getName())) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        Tags tags = this.tagsDao.selectById(tagsVO.getId());
        if (tags == null) {
            ExceptionUtil.rollback("", ErrorConstants.DATA_NO_EXIST);
        }

        this.tagsDao.updateById(tags.setName(tagsVO.getName()).setUpdateTime(LocalDateTime.now()));
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result<TagsVO> deleteTags(Long id) {
        if (id == null) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        this.tagsDao.deleteById(id);
        this.categoryTagsDao.delete(new LambdaQueryWrapper<CategoryTags>().eq(CategoryTags::getTagsId, id));
        this.postsTagsDao.delete(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getTagsId, id));

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result<TagsVO> saveTags(TagsVO tagsVO) {

        if (tagsVO == null || StringUtils.isBlank(tagsVO.getName())) {
            ExceptionUtil.rollback("", ErrorConstants.PARAM_INCORRECT);
        }

        this.tagsDao.insert(new Tags().setName(tagsVO.getName()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()));
        return Result.createWithSuccessMessage();
    }
}
