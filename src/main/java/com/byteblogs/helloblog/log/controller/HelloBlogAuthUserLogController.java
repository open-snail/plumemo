package com.byteblogs.helloblog.log.controller;


import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.log.domain.vo.HelloBlogAuthUserLogVO;
import com.byteblogs.helloblog.log.service.HelloBlogAuthUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户行为日志记录表: 后端controller类
 * @author generator
 * @date 2019-12-25 09:10:17
 * @since 1.0
 */
@RestController
@RequestMapping("/logs")
public class HelloBlogAuthUserLogController {
    
    
    @Autowired
    private HelloBlogAuthUserLogService helloBlogAuthUserLogServiceImpl;
    
    /**
     * 查询用户行为日志记录表
     */
    @GetMapping("/logs/v1/{id}")
    public Result query(@PathVariable Long id){
        return helloBlogAuthUserLogServiceImpl.getLogs(id);
    }
    
    
    /**
     * 分页查询用户行为日志记录表
     */
    @PostMapping("/logs/v1/list")
    public Result queryPage(HelloBlogAuthUserLogVO helloBlogAuthUserLogVO){
        return helloBlogAuthUserLogServiceImpl.getLogsList(helloBlogAuthUserLogVO);
    }
}