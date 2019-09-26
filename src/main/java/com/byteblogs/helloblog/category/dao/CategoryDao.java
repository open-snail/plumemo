package com.byteblogs.helloblog.category.dao;

import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.category.domain.po.Category;
import com.byteblogs.helloblog.category.domain.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author byteblogs
 * @since 2019-08-28
 */
public interface CategoryDao extends BaseDao<Category> {

    /**
     * 查询一个分类有多少篇文章
     * @return
     */
    List<CategoryVO> selectCategoryPostsTotal();
}
