package com.byteblogs.helloblog.category.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.service.BaseService;
import com.byteblogs.helloblog.category.domain.po.Tags;
import com.byteblogs.helloblog.category.domain.vo.TagsVO;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
public interface TagsService extends BaseService<Tags> {

    /**
     * 查询标签的列表
     * @param tagsVO
     * @return
     */
    Result<TagsVO> getTagsAndArticleQuantityList(TagsVO tagsVO);

    /**
     * 查询标签的列表
     * @param tagsVO
     * @return
     */
    Result<TagsVO> getTagsList(TagsVO tagsVO);

    /**
     * 查询标签
     * @param id
     * @return
     */
    Result<TagsVO> getTags(Long id);

    /**
     * 查询标签
     * @param tagsVO
     * @return
     */
    Result<TagsVO> updateTags(TagsVO tagsVO);

    /**
     * 删除标签
     * @param id
     * @return
     */
    Result<TagsVO> deleteTags(Long id);

    /**
     * 新增标签
     * @param tagsVO
     * @return
     */
    Result<TagsVO> saveTags(TagsVO tagsVO);
}
