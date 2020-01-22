package com.byteblogs.helloblog.log.controller;


import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.log.service.AuthUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户行为日志记录表: 后端controller类
 * @author 青涩知夏
 */
@RestController
@RequestMapping("/logs")
public class AuthUserLogController {
    
    
    @Autowired
    private AuthUserLogService authUserLogServiceImpl;
    
    /**
     * 查询用户行为日志记录表
     */
    @GetMapping("/logs/v1/{id}")
    public Result<AuthUserLogVO> query(@PathVariable Long id){
        return authUserLogServiceImpl.getLogs(id);
    }

    @DeleteMapping("/logs/v1/{id}")
    public Result<AuthUserLogVO> deleteLogs(@PathVariable Long id){
        return authUserLogServiceImpl.deleteLogs(id);
    }


    /**
     * 分页查询用户行为日志记录表
     */
    @GetMapping("/logs/v1/list")
    public Result<AuthUserLogVO> queryPage(AuthUserLogVO authUserLogVO){
        return authUserLogServiceImpl.getLogsList(authUserLogVO);
    }
}