package com.byteblogs.helloblog.menu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.helloblog.menu.dao.MenuDao;
import com.byteblogs.helloblog.menu.domain.po.Menu;
import com.byteblogs.helloblog.menu.domain.vo.MenuVO;
import com.byteblogs.helloblog.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public Result saveMenu(MenuVO menuVO) {
        Menu menu=new Menu().setIcon(menuVO.getIcon()).setParentId(menuVO.getParentId()).setSort(menuVO.getSort()).setTitle(menuVO.getTitle()).setUrl(menuVO.getUrl());
        menuDao.insert(menu);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getMenu(Long id) {
        Menu menu=menuDao.selectById(id);
        if (menu==null){
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }
        return Result.createWithModel(menu);
    }

    @Override
    public Result getMenuList(MenuVO menuVO) {
        menuVO= Optional.ofNullable(menuVO).orElse(new MenuVO());
        Page page= Optional.of(PageUtil.checkAndInitPage(menuVO)).orElse(PageUtil.initPage());
        if (!StringUtils.isEmpty(menuVO.getKeywords())){
            menuVO.setKeywords("%"+menuVO.getKeywords()+"%");
        }
        List<MenuVO> menuVOList=menuDao.selectMenuList(page,menuVO);
        return Result.createWithPaging(menuVOList, PageUtil.initPageInfo(page));
    }

    @Override
    public Result updateMenu(MenuVO menuVO) {
        this.menuDao.updateById(new Menu().setId(menuVO.getId()).setIcon(menuVO.getIcon()).setTitle(menuVO.getTitle()).setParentId(menuVO.getParentId()).setSort(menuVO.getSort()).setUrl(menuVO.getUrl()));
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result deleteMenu(Long id) {
        this.menuDao.deleteById(id);
        return Result.createWithSuccessMessage();
    }
}
