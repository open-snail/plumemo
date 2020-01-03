package com.byteblogs.helloblog.monitor.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.system.enums.RoleEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.byteblogs.helloblog.monitor.util.RuntimeUtil;

@RestController
@RequestMapping("monitor")
public class MonitorController {
    @GetMapping("/monitor/v1/system")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getSystem() {
        return RuntimeUtil.getProperty();
    }

    @GetMapping("/monitor/v1/memory")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getMemory() {
        return RuntimeUtil.getMemory();
    }

    @GetMapping("/monitor/v1/cpu")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getCpu() {
        return RuntimeUtil.getCpu();
    }

    @GetMapping("/monitor/v1/file")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getFile() {
        return RuntimeUtil.getFile();
    }

    @GetMapping("/monitor/v1/net")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getNet() {
        return RuntimeUtil.getNet();
    }

    @GetMapping("/monitor/v1/ethernet")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getEthernet() {
        return RuntimeUtil.getEthernet();
    }
}
