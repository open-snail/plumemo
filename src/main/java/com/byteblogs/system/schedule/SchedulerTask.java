package com.byteblogs.system.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.helloblog.auth.dao.AuthTokenDao;
import com.byteblogs.helloblog.auth.domain.po.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
@Component
@Slf4j
public class SchedulerTask {

    @Autowired
    private AuthTokenDao authTokenDao;

    @Scheduled(cron = "0 0/1 * * * ?")
    private void scanToken() {
        log.debug(" {} 扫描过期Token", LocalDateTime.now());
        authTokenDao.delete(new LambdaQueryWrapper<AuthToken>().le(AuthToken::getExpireTime, LocalDateTime.now()));
    }
}
