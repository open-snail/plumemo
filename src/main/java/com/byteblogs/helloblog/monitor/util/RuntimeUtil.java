package com.byteblogs.helloblog.monitor.util;
 
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.byteblogs.common.base.domain.Result;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

public class RuntimeUtil {

    /**
     * 获取系统属性
     */
    public static Result<JSONObject> getProperty(){
        JSONObject obj=new JSONObject();
        try{
            Runtime r = Runtime.getRuntime();
            Properties props = System.getProperties();
            InetAddress addr = InetAddress.getLocalHost();
            Map<String, String> map = System.getenv();
            obj.put("ip",addr.getHostName());
            obj.put("hostName",InetAddress.getLocalHost());
            obj.put("userName",map.get("USERNAME"));
            obj.put("computerName",map.get("COMPUTERNAME"));
            obj.put("userDomain",map.get("USERDOMAIN"));
            obj.put("totalMemory",r.totalMemory());//JVM可以使用的总内存
            obj.put("freeMemory", r.freeMemory());// JVM可以使用的剩余内存
            obj.put("availableProcessors",r.availableProcessors()); // JVM可以使用的处理器个数
            obj.put("javaVersion",props.getProperty("java.version")); // Java的运行环境版本
            obj.put("javaVendor",props.getProperty("java.vendor")); // Java的运行环境供应商
            obj.put("javaVendorUrl",props.getProperty("java.vendor.url")); // Java供应商的URL
            obj.put("javaHome",props.getProperty("java.home")); // Java的安装路径
            obj.put("java.vm.specification.version",props.getProperty("java.vm.specification.version")); // Java的虚拟机规范版本
//            obj.put("javaClassPath",props.getProperty("java.class.path"));// Java的类路径
//            obj.put("javaLibraryPath",props.getProperty("java.library.path"));// 加载库时搜索的路径列表
            obj.put("javaIoTmpdir",props.getProperty("java.io.tmpdir"));// 默认的临时文件路径
            obj.put("javaExtDirs",props.getProperty("java.ext.dirs")); // 一个或多个扩展目录的路径
            obj.put("osName",props.getProperty("os.name")); // 操作系统的名称
            obj.put("osArch",props.getProperty("os.arch")); // 操作系统的构架
            obj.put("osVersion",props.getProperty("os.version")); // 操作系统的版本
            obj.put("fileSeparator",props.getProperty("file.separator")); // 文件分隔符
            obj.put("pathSeparator",props.getProperty("path.separator"));// 路径分隔符
            obj.put("lineSeparator",props.getProperty("line.separator"));// 行分隔符
            obj.put("userNameDir",props.getProperty("user.name"));// 用户的账户名称
            obj.put("userHome",props.getProperty("user.home"));// 用户的主目录
            obj.put("userDir",props.getProperty("user.dir")); // 用户的当前工作目录
        }catch (Exception e){ e.printStackTrace(); }
        return Result.createWithModel(obj);
    }

    /**
     * 获取内存信息
     */
    public static Result<JSONObject> getMemory(){
        JSONObject obj=new JSONObject();
        try{
            Sigar sigar = new Sigar();
            Mem mem = sigar.getMem();
            BigDecimal total = BigDecimal.valueOf(mem.getTotal()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
            BigDecimal used = BigDecimal.valueOf(mem.getUsed()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
            BigDecimal free = BigDecimal.valueOf(mem.getFree()).divide(BigDecimal.valueOf(1024 * 1024 * 1024), BigDecimal.ROUND_HALF_UP);
            // 内存总量
            obj.put("total",total);
            // 当前内存使用量
            obj.put("used", used);
            // 当前内存剩余量
            obj.put("free", free);
            // 使用率
            obj.put("usedRatio",(used.divide(total,3, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100))).setScale(2,BigDecimal.ROUND_HALF_UP));
//            Swap swap = sigar.getSwap();
//            obj.put("swapTotal",swap.getTotal() / 1024L / 1024L);// 交换区总量
//            obj.put("swapUsed",swap.getUsed() / 1024L / 1024L);// 当前交换区使用量
//            obj.put("swapFree",swap.getFree() / 1024L / 1024L);// 当前交换区剩余量
        }catch (Exception e){e.printStackTrace();}
        return Result.createWithModel(obj);
    }

    public static Result<JSONArray> getCpu(){
        JSONArray obj=new JSONArray();
        try{
            Sigar sigar = new Sigar();
            CpuInfo infos[] = sigar.getCpuInfoList();
            CpuPerc cpuList[] = null;
            cpuList = sigar.getCpuPercList();
            for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
                CpuInfo info = infos[i];
                JSONObject cpuObj=new JSONObject();
                cpuObj.put("Mhz",info.getMhz());// CPU的总量MHz
                cpuObj.put("vendor",info.getVendor());// 获得CPU的卖主，如：Intel
                cpuObj.put("model",info.getModel());// 获得CPU的类别，如：Celeron
                cpuObj.put("cacheSize",info.getCacheSize());// 缓冲存储器数量
                cpuObj.putAll(getCpuPerc(cpuList[i]));
                obj.add(cpuObj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.createWithModel(obj);
    }

    /**
     * 获取CPU信息
     */
    private static JSONObject getCpuPerc(CpuPerc cpu) {
        JSONObject obj=new JSONObject();
        obj.put("user", CpuPerc.format(cpu.getUser()));// 用户使用率
        obj.put("sys",CpuPerc.format(cpu.getSys()));// 系统使用率
        obj.put("wait",CpuPerc.format(cpu.getWait()));// 当前等待率
        obj.put("nice",CpuPerc.format(cpu.getNice()));// 当前错误率
        obj.put("idle",CpuPerc.format(cpu.getIdle()));// 当前空闲率
        obj.put("combined",CpuPerc.format(cpu.getCombined()));// 总的使用率
        return obj;
    }

 
    public static Result<JSONObject> getFile(){
        JSONObject obj = new JSONObject();
        try{
            Sigar sigar = new Sigar();
            FileSystem fslist[] = sigar.getFileSystemList();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0, len = fslist.length; i < len; i++) {
                FileSystem fs = fslist[i];
                JSONObject jso = new JSONObject();
                jso.put("dev.name", fs.getDevName()); //分区盘符名称
                jso.put("dir.name", fs.getDirName()); //分区盘符名称
                jso.put("flags", fs.getFlags()); //分区盘符类型
                jso.put("sys.type.name", fs.getSysTypeName()); //文件系统类型
                jso.put("type.name", fs.getTypeName()); //分区盘符类型名
                jso.put("type", fs.getType()); //分区盘符文件系统类型
                FileSystemUsage usage = null;
                try {
                    usage = sigar.getFileSystemUsage(fs.getDirName());
                } catch (Exception e) { e.printStackTrace();}
                if (usage == null) {
                    continue;
                }
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        jso.put("usage.totle", usage.getTotal() / 1024); // 分区总大小
                        jso.put("usage.free", usage.getFree() / 1024); // 分区剩余大小
                        jso.put("usage.avail", usage.getAvail() / 1024); // 分区可用大小
                        jso.put("usage.used", usage.getUsed() / 1024); // 分区已经使用量
                        jso.put("usage.use.percent", usage.getUsePercent() * 100D); // 分区资源的利用率
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
                jso.put("disk.reads", usage.getDiskReads()); // 读出
                jso.put("disk.writes", usage.getDiskWrites()); // 写入
                jsonArray.add(jso);
            }
            obj.put("file.system", jsonArray);
        }catch (Exception e){e.printStackTrace();}
        return Result.createWithModel(obj);
    }

    /**
     * 获取网络信息
     */
    public static Result<JSONObject> getNet() {
        JSONObject jsonObject = new JSONObject();
        try{
            Sigar sigar = new Sigar();
            String ifNames[] = sigar.getNetInterfaceList();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0, len = ifNames.length; i < len; i++) {
                String name = ifNames[i];
                JSONObject jso = new JSONObject();
                NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
                jso.put("name", name); // 网络设备名
                jso.put("address", ifconfig.getAddress()); // IP地址
                jso.put("mask", ifconfig.getNetmask()); // 子网掩码
                if ((ifconfig.getFlags() & 1L) <= 0L) {
                    continue;
                }
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                jso.put("rx.packets", ifstat.getRxPackets());// 接收的总包裹数
                jso.put("tx.packets", ifstat.getTxPackets());// 发送的总包裹数
                jso.put("rx.bytes", ifstat.getRxBytes());// 接收到的总字节数
                jso.put("tx.bytes", ifstat.getTxBytes());// 发送的总字节数
                jso.put("rx.errors", ifstat.getRxErrors());// 接收到的错误包数
                jso.put("tx.errors", ifstat.getTxErrors());// 发送数据包时的错误数
                jso.put("rx.dropped", ifstat.getRxDropped());// 接收时丢弃的包数
                jso.put("tx.dropped", ifstat.getTxDropped());// 发送时丢弃的包数
                jsonArray.add(jso);
            }
            jsonObject.put("net", jsonArray);
        }catch (Exception e){e.printStackTrace();}
        return Result.createWithModel(jsonObject);
    }

    /**
     * 获取网卡信息
     */
    public static Result<JSONObject> getEthernet() {
        JSONObject jsonObject = new JSONObject();
        try{
            Sigar sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0, len = ifaces.length; i < len; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0 || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                JSONObject jso = new JSONObject();
                jso.put("address", cfg.getAddress());// IP地址
                jso.put("broad.cast", cfg.getBroadcast());// 网关广播地址
                jso.put("hwaddr", cfg.getHwaddr());// 网卡MAC地址
                jso.put("net.mask", cfg.getNetmask());// 子网掩码
                jso.put("description", cfg.getDescription());// 网卡描述信息
                jso.put("type", cfg.getType());// 网卡类型
                jsonArray.add(jso);
            }
            jsonObject.put("ethernet", jsonArray);
        }catch (Exception e){e.printStackTrace(); }
        return Result.createWithModel(jsonObject);
    }
}