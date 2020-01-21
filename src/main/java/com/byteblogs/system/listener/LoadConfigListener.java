package com.byteblogs.system.listener;

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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * @author: byteblogs
 * @date: 2019/09/03 12:09
 */
@Slf4j
public class LoadConfigListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        final ConfigDao configDao = BeanTool.getBean(ConfigDao.class);
        final List<Config> configList = configDao.selectList(null);
        configList.forEach(config -> {
            log.debug("config_key: {}, config_vlaue: {}", config.getConfigKey(), config.getConfigValue());
            ConfigCache.putConfig(config.getConfigKey(), config.getConfigValue());
        });
        AuthUserDao authUserDao=BeanTool.getBean(AuthUserDao.class);
        List<AuthUser> authUsers=authUserDao.selectList(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getRoleId, RoleEnum.ADMIN.getRoleId()));
        if (!CollectionUtils.isEmpty(authUsers)){
            SystemPropertyBean systemPropertyBean=BeanTool.getBean(SystemPropertyBean.class);
            systemPropertyBean.setSocialId(authUsers.get(0).getSocialId());
        }
    }


}
