package com.byteblogs.helloblog.category.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.Messages;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.common.validator.group.Insert;
import com.byteblogs.common.validator.group.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@Accessors(chain = true)
public class CategoryVO extends BaseVO<CategoryVO> {

    /**
     * 名称
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private List<TagsVO> tagsList;

    private Integer total;
}
