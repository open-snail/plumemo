package com.byteblogs.helloblog.category.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.category.domain.po.Category;
import com.byteblogs.helloblog.category.domain.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

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
    IPage<CategoryVO> selectStatistics(Page page, @Param(Constants.WRAPPER) Wrapper<CategoryVO> queryWrapper);

    IPage<Category> selectListPage(@Param("page") Page page, @Param("condition") CategoryVO categoryVO);
}
