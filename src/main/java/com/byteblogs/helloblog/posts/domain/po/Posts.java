package com.byteblogs.helloblog.posts.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("hello_blog_posts")
public class Posts extends Model<Posts> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 封面图
     */
    private String thumbnail;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 状态 1 草稿 2 发布
     */
    private Integer status;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 文章权重
     */
    private Integer weight;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long authorId;

    /**
     * 是否打开评论
     */
    private Integer isComment;

    /**
     * 同步到byteblogs状态 (0 未同步 或者同步失败的状态 1同步已成功)
     */
    private Integer syncStatus;

    /**
     * 文章分类Id
     */
    private Integer categoryId;

    /**
     * 文章分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
