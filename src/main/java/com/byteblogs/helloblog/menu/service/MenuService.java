package com.byteblogs.helloblog.menu.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.menu.domain.vo.MenuVO;

/**
 * 菜单表:业务接口类
 */
public interface MenuService {
    
    /**
     * 新增菜单表
     */
    Result saveMenu(MenuVO menuVO);
    
    
    /**
     * 查询菜单表
     */
    Result getMenu(Long id);

    
    /**
     * 分页查询菜单表
     */
    Result getMenuList(MenuVO menuVO);

    
    /**
     * 更新菜单表
     */
    Result updateMenu(MenuVO menuVO);

    
    /**
     * 删除菜单表
     */
    Result deleteMenu(Long id);

    Result getFrontMenuList(MenuVO menuVO);
}