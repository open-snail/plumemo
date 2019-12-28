package com.byteblogs.helloblog.menu.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.helloblog.menu.domain.validator.InsertMenu;
import com.byteblogs.helloblog.menu.domain.validator.UpdateMenu;
import com.byteblogs.helloblog.posts.domain.validator.InsertPosts;
import com.byteblogs.helloblog.posts.domain.validator.UpdatePosts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class MenuVO extends BaseVO<MenuVO> {

	private static final long serialVersionUID = 1L;

    // columns START

	@NotBlank(groups = {InsertMenu.class, UpdateMenu.class})
	private Long id; 

	/**
	 * 父菜单Id
	 */
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