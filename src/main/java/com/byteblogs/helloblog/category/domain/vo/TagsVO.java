package com.byteblogs.helloblog.category.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @date: 2019/9/1 20:52
 * @modified:
 */
@Data
@Accessors(chain = true)
public class TagsVO extends BaseVO<TagsVO> {

    /**
     * 名称
     */
    private String name;

    /**
     * 文章总数
     */
    private Integer postsTotal;
}
