package com.byteblogs.helloblog.config.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.config.domain.validator.QueryConfigList;
import com.byteblogs.helloblog.config.domain.validator.UpdateConfig;
import com.byteblogs.helloblog.config.domain.vo.ConfigVO;
import com.byteblogs.helloblog.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/config")
@Validated({UpdateConfig.class})
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @LoginRequired
    @PutMapping("/config/v1/update")
    public Result updateConfig(@RequestBody List<ConfigVO> configList) {
        return configService.updateConfig(configList);
    }

    @LoginRequired
    @GetMapping("/config/v1/list")
    public Result getConfigList(@Validated({QueryConfigList.class}) ConfigVO configVO) {
        return configService.getConfigList(configVO);
    }

    @GetMapping("/config-base/v1/list")
    public Result getConfigBaseList() {
        return configService.getConfigBaseList();
    }
}
