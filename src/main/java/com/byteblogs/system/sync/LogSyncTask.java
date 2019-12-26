package com.byteblogs.system.sync;

import com.byteblogs.helloblog.log.domain.po.HelloBlogAuthUserLog;
import com.byteblogs.helloblog.log.domain.vo.HelloBlogAuthUserLogVO;
import com.byteblogs.helloblog.log.service.HelloBlogAuthUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LogSyncTask {

    @Autowired
    private HelloBlogAuthUserLogService sysLogServiceImpl;

    @Async
    public void addLog(HelloBlogAuthUserLogVO sysLog){
        this.sysLogServiceImpl.saveLogs(sysLog);
    }
}
