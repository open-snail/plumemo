package com.byteblogs.system.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.helloblog.auth.dao.AuthUserDao;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.bean.SystemPropertyBean;
import com.byteblogs.helloblog.config.dao.ConfigDao;
import com.byteblogs.helloblog.config.domain.po.Config;
import com.byteblogs.system.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: byteblogs
 * @date: 2019/09/03 12:09
 */
@Slf4j
@Component
@DependsOn({"dataSource"})
public class InitSystemConfig implements ApplicationListener<ApplicationContextEvent>, Ordered {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private AuthUserDao authUserDao;

    public void init() {

        List<Config> configList = configDao.selectList(null);
        configList.forEach(config -> {
            log.debug("config_key: {}, config_vlaue: {}", config.getConfigKey(), config.getConfigValue());
            ConfigCache.putConfig(config.getConfigKey(), config.getConfigValue());
        });

        List<AuthUser> authUsers = authUserDao.selectList(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        if (!CollectionUtils.isEmpty(authUsers)) {
            SystemPropertyBean systemPropertyBean = BeanTool.getBean(SystemPropertyBean.class);
            systemPropertyBean.setAccessKey(authUsers.get(0).getAccessKey());
            systemPropertyBean.setSecretKey(authUsers.get(0).getSecretKey());
        }
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        init();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}