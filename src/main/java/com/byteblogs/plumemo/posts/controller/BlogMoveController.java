package com.byteblogs.plumemo.posts.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.util.ThrowableUtils;
import com.byteblogs.plumemo.posts.domain.vo.BlogMoveVO;
import com.byteblogs.plumemo.posts.service.BlogMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 17:20
 * @modified:
 */
@RestController
@RequestMapping("/blog-move")
public class BlogMoveController {

    @Autowired
    private BlogMoveService blogMoveService;

    @LoginRequired
    @PostMapping("/v1/file")
    public Result uploadFileList(@RequestParam("file")MultipartFile file){
        return blogMoveService.importDataByFile(file);
    }

    @LoginRequired
    @PostMapping("/v1/mysql")
    public Result importDataByDB(@Valid @RequestBody BlogMoveVO blogMoveVO){
        return blogMoveService.importDataByDB(blogMoveVO);
    }
}
