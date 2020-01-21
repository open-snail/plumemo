package com.byteblogs.helloblog.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.helloblog.config.dao.ConfigDao;
import com.byteblogs.helloblog.config.domain.po.Config;
import com.byteblogs.helloblog.config.domain.vo.ConfigVO;
import com.byteblogs.helloblog.config.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, Config> implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public Result updateConfig(final List<ConfigVO> configList) {

        if (CollectionUtils.isEmpty(configList)) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_INCORRECT);
        }

        final boolean b = configList.stream().anyMatch(configVO -> StringUtils.isBlank(configVO.getConfigKey()) || StringUtils.isBlank(configVO.getConfigValue()));
        if (b) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_INCORRECT);
        }

        configList.forEach(configVO -> {
            if (configVO.getConfigKey() != null) {
                this.configDao.update(new Config().setConfigValue(configVO.getConfigValue()),
                        new LambdaQueryWrapper<Config>().eq(Config::getConfigKey, configVO.getConfigKey()));
                ConfigCache.putConfig(configVO.getConfigKey(), configVO.getConfigValue());
            }
        });

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getConfigList(final ConfigVO configVO) {
        final List<Config> configs;
        if (configVO.getType() == Constants.ONE || configVO.getType() == Constants.FOUR || configVO.getType()==Constants.FIVE) {
            configs = this.configDao.selectList(new LambdaQueryWrapper<Config>().in(Config::getType, configVO.getType(), 3));
        } else {
            configs = this.configDao.selectList(new LambdaQueryWrapper<Config>().eq(Config::getType, configVO.getType()));
        }

        final List<ConfigVO> configVOList = new ArrayList<>();
        configs.forEach(config -> {
            final ConfigVO configVO1 = new ConfigVO();
            configVOList.add(configVO1.setConfigKey(config.getConfigKey()).setConfigValue(config.getConfigValue()));
        });

        return Result.createWithModels(configVOList);
    }

    @Override
    public Result getConfigBaseList() {
        return getConfigList(new ConfigVO().setType(Constants.ZERO));
    }
}
