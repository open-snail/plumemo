package com.byteblogs.helloblog.menu.controller;


import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.menu.domain.vo.MenuVO;
import com.byteblogs.helloblog.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单表: 后端controller类
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuController {
    
    
    @Autowired
    private MenuService menuService;
    
    
    /**
     * 新增菜单表
     */
    @PostMapping("/menu/v1/add")
    public Result saveMenu(@RequestBody MenuVO menuVO){
        return menuService.saveMenu(menuVO);
    }
    
    
    /**
     * 查询菜单表
     */
    @GetMapping("/menu/v1/{id}")
    public Result getMenu(@PathVariable Long id){
        return menuService.getMenu(id);
    }
    
    
    /**
     * 分页查询菜单表
     */
    @GetMapping("/menu/v1/list")
    public Result getMenuList(MenuVO menuVO){
        return menuService.getMenuList(menuVO);
    }


    @GetMapping("/front/v1/list")
    public Result getFrontMenuList(MenuVO menuVO){
        return menuService.getFrontMenuList(menuVO);
    }
    
    /**
     * 更新菜单表
     */
    @PutMapping("/menu/v1/update")
    public Result updateMenu(@RequestBody MenuVO menuVO){
        return menuService.updateMenu(menuVO);
    }

    /**
     * 删除菜单表
     */
    @DeleteMapping("/menu/v1/{id}")
    public Result delete(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }
}