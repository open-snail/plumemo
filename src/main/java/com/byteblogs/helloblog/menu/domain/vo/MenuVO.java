package com.byteblogs.helloblog.menu.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.helloblog.menu.domain.po.Menu;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class MenuVO extends BaseVO<MenuVO> {

	private static final long serialVersionUID = 1L;

    // columns START
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

	private List<Menu> child;
	// columns END
}