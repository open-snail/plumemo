package com.byteblogs.common.base.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface BaseDao<T> extends BaseMapper<T> {

    /**
     * 逻辑删除
     * @param entity
     * @param updateWrapper
     * @return
     */
    int logicDeleteByWrapper(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);
}
