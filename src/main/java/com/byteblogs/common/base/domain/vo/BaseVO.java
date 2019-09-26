package com.byteblogs.common.base.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author:byteblogs
 * @Date:2018/09/21 13:30
 */
@Data
@Accessors(chain = true)
public class BaseVO<T> {

    /**
     * 主键
     */
    protected Long id;

    /**
     * 关键词搜索
     */
    protected String keywords;

    /**
     * 页数
     */
    protected Integer page;

    /**
     * 每页大小
     */
    protected Integer size;

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Integer getSize() {
        return size;
    }

    public BaseVO<T> setSize(Integer size) {
        this.size = size > 20 ? 20 : size;
        return this;
    }

}
