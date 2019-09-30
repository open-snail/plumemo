package com.byteblogs.helloblog.category.controller;

import com.byteblogs.common.validator.annotion.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.helloblog.category.domain.vo.CategoryVO;
import com.byteblogs.helloblog.category.service.CategoryService;

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
    public Result saveCategory(@Validated @RequestBody CategoryVO categoryVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.saveCategory(categoryVO);
    }

    @LoginRequired
    @PutMapping("/category/v1/update")
    public Result updateCategory(@Validated @RequestBody CategoryVO categoryVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.updateCategory(categoryVO);
    }

    @LoginRequired
    @GetMapping("/category-tags/v1/{id}")
    public Result getCategoryTags(@Validated @PathVariable(value = "id", required = true) @NotNull Long id, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.getCategoryTags(id);
    }

    @LoginRequired
    @GetMapping("/category-tags/v1/list")
    public Result getCategoryTagsList(@Validated CategoryVO categoryVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.getCategoryTagsList(categoryVO);
    }


    @GetMapping("/category/v1/{id}")
    public Result getCategory(@Validated @PathVariable(value = "id", required = true) @NotNull Long id, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.getCategory(id);
    }

    @GetMapping("/category/v1/list")
    public Result getCategoryList(@Validated CategoryVO categoryVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.getCategoryList(categoryVO);
    }

    @LoginRequired
    @DeleteMapping("/category/v1/{id}")
    public Result deleteCategory(@Validated @PathVariable(value = "id", required = true) @NotNull Long id, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return categoryService.deleteCategory(id);
    }

}
