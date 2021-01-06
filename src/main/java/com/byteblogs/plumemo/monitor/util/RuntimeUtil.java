package com.byteblogs.plumemo.monitor.util;

import com.alibaba.fastjson.JSONObject;
import com.byteblogs.common.base.domain.Result;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class RuntimeUtil {

    /**
     * 获取系统属性
     */
    public static Result<JSONObject> getProperty() {
        JSONObject obj = new JSONObject();
        try {
            Runtime r = Runtime.getRuntime();
            Properties props = System.getProperties();
            InetAddress addr = InetAddress.getLocalHost();
            Map<String, String> map = System.getenv();
            obj.put("ip", addr.getHostName());
            obj.put("hostName", InetAddress.getLocalHost());
            obj.put("userName", map.get("USERNAME"));
            obj.put("computerName", map.get("COMPUTERNAME"));
            obj.put("userDomain", map.get("USERDOMAIN"));
            obj.put("totalMemory", r.totalMemory());//JVM可以使用的总内存
            obj.put("freeMemory", r.freeMemory());// JVM可以使用的剩余内存
            obj.put("availableProcessors", r.availableProcessors()); // JVM可以使用的处理器个数
            obj.put("javaVersion", props.getProperty("java.version")); // Java的运行环境版本
            obj.put("javaVendor", props.getProperty("java.vendor")); // Java的运行环境供应商
            obj.put("javaVendorUrl", props.getProperty("java.vendor.url")); // Java供应商的URL
            obj.put("javaHome", props.getProperty("java.home")); // Java的安装路径
            obj.put("java.vm.specification.version", props.getProperty("java.vm.specification.version")); // Java的虚拟机规范版本
            obj.put("javaIoTmpdir", props.getProperty("java.io.tmpdir"));// 默认的临时文件路径
            obj.put("javaExtDirs", props.getProperty("java.ext.dirs")); // 一个或多个扩展目录的路径
            obj.put("osName", props.getProperty("os.name")); // 操作系统的名称
            obj.put("osArch", props.getProperty("os.arch")); // 操作系统的构架
            obj.put("osVersion", props.getProperty("os.version")); // 操作系统的版本
            obj.put("fileSeparator", props.getProperty("file.separator")); // 文件分隔符
            obj.put("pathSeparator", props.getProperty("path.separator"));// 路径分隔符
            obj.put("lineSeparator", props.getProperty("line.separator"));// 行分隔符
            obj.put("userNameDir", props.getProperty("user.name"));// 用户的账户名称
            obj.put("userHome", props.getProperty("user.home"));// 用户的主目录
            obj.put("userDir", props.getProperty("user.dir")); // 用户的当前工作目录
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.createWithModel(obj);
    }

    /**
     * 获取内存信息
     */
    public static Result<JSONObject> getMemory() {
        JSONObject obj = new JSONObject();
        try {
            SystemInfo systemInfo = new SystemInfo();
            GlobalMemory memory = systemInfo.getHardware().getMemory();
            BigDecimal total = BigDecimal.valueOf(memory.getTotal()).divide(BigDecimal.valueOf(1024 * 1024 * 1024)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal usedRatio = BigDecimal.valueOf(memory.getTotal() - memory.getAvailable()).divide(BigDecimal.valueOf(memory.getTotal()), 3, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            log.warn("total={} used={} free={}", memory.getTotal(), memory.getTotal() - memory.getAvailable(), memory.getAvailable());
            obj.put("total", total);// 内存总量
            obj.put("usedRatio", usedRatio);// 使用率
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.createWithModel(obj);
    }

}