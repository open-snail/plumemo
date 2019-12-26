package com.byteblogs.helloblog.monitor.controller;

import com.byteblogs.common.base.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.byteblogs.helloblog.monitor.util.RuntimeUtil;

@RestController
@RequestMapping("monitor")
public class monitorController {
    @GetMapping("/monitor/v1/system")
    public Result getSystem() {
        return RuntimeUtil.getProperty();
    }

    @GetMapping("/monitor/v1/memory")
    public Result getMemory() {
        return RuntimeUtil.getMemory();
    }

    @GetMapping("/monitor/v1/cpu")
    public Result getCpu() {
        return RuntimeUtil.getCpu();
    }

    @GetMapping("/monitor/v1/file")
    public Result getFile() {
        return RuntimeUtil.getFile();
    }

    @GetMapping("/monitor/v1/net")
    public Result getNet() {
        return RuntimeUtil.getNet();
    }
    @GetMapping("/monitor/v1/ethernet")
    public Result getEthernet() {
        return RuntimeUtil.getEthernet();
    }
}
