package com.byteblogs.helloblog.category.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.category.domain.vo.CategoryVO;
import com.byteblogs.helloblog.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @LoginRequired
    @PostMapping("/category/v1/add")
    public Result saveCategory(@RequestBody CategoryVO categoryVO) {
        return categoryService.saveCategory(categoryVO);
    }

    @LoginRequired
    @PutMapping("/category/v1/update")
    public Result updateCategory(@RequestBody CategoryVO categoryVO) {
        return categoryService.updateCategory(categoryVO);
    }

    @LoginRequired
    @GetMapping("/category-tags/v1/{id}")
    public Result getCategoryTags(@PathVariable(value = "id", required = true) Long id) {
        return categoryService.getCategoryTags(id);
    }

    @LoginRequired
    @GetMapping("/category-tags/v1/list")
    public Result getCategoryTagsList(CategoryVO categoryVO) {
        return categoryService.getCategoryTagsList(categoryVO);
    }


    @GetMapping("/category/v1/{id}")
    public Result getCategory(@PathVariable(value = "id", required = true) Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/category/v1/list")
    public Result getCategoryList(CategoryVO categoryVO) {
        return categoryService.getCategoryList(categoryVO);
    }

    @LoginRequired
    @DeleteMapping("/category/v1/{id}")
    public Result deleteCategory(@PathVariable(value = "id", required = true) Long id) {
        return categoryService.deleteCategory(id);
    }

}
