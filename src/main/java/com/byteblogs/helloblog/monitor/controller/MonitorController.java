package com.byteblogs.helloblog.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.byteblogs.common.annotation.LoginRequired;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.system.enums.RoleEnum;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.byteblogs.helloblog.monitor.util.RuntimeUtil;

import java.math.BigDecimal;

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
    public Result getMemory() throws SigarException {
        JSONObject obj=new JSONObject();
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        BigDecimal total = BigDecimal.valueOf(mem.getTotal()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
        BigDecimal used = BigDecimal.valueOf(mem.getUsed()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
        BigDecimal free = BigDecimal.valueOf(mem.getFree()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
        obj.put("total",total); // 内存总量
        obj.put("used", used);// 当前内存使用量
        obj.put("free", free); // 当前内存剩余量
        // 使用率
        obj.put("usedRatio",(used.divide(total,3, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100))).setScale(2,BigDecimal.ROUND_HALF_UP));
        return Result.createWithModel(obj);
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
