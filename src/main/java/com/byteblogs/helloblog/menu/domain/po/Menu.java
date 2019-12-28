package com.byteblogs.helloblog.menu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "hello_blog_menu")
public class Menu extends Model<Menu> {

	private static final long serialVersionUID = 1L;

    // columns START

	@TableId(value = "id",type= IdType.AUTO)
	private Long id; 

	/**
	 * 父菜单Id
	 */
	@TableField(value = "parent_id")
	private Long parentId; 

	/**
	 * 名称
	 */
	private String title; 

	/**
	 * icon图标
	 */
	private String icon; 

	/**
	 * 跳转路径
	 */
	private String url; 

	/**
	 * 排序
	 */
	private Integer sort; 
	// columns END
}