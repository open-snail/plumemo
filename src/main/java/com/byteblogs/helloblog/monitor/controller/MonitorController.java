package com.byteblogs.helloblog.monitor.controller;

import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.system.enums.RoleEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.byteblogs.helloblog.monitor.util.RuntimeUtil;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @GetMapping("/system/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getSystem() {
        return RuntimeUtil.getProperty();
    }

    @GetMapping("/memory/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getMemory(){
        return RuntimeUtil.getMemory();
    }

    @GetMapping("/cpu/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getCpu() {
        return RuntimeUtil.getCpu();
    }

    @GetMapping("/file/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getFile() {
        return RuntimeUtil.getFile();
    }

    @GetMapping("/net/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getNet() {
        return RuntimeUtil.getNet();
    }

    @GetMapping("/ethernet/v1/get")
    @LoginRequired(role = RoleEnum.ADMIN)
    public Result getEthernet() {
        return RuntimeUtil.getEthernet();
    }
}
