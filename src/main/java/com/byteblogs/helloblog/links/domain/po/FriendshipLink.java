package com.byteblogs.helloblog.links.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author byteblogs
 * @since 2019-04-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hello_blog_friendship_link")
public class FriendshipLink extends Model<FriendshipLink> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    private String title;

    /**
     * 文件
     */
    private String logo;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String description;

    /**
     * 路径
     */
    private String href;

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
