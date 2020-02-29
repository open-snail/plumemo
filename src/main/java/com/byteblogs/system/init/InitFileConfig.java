package com.byteblogs.system.init;

import com.byteblogs.helloblog.monitor.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;

@Component
@Slf4j
public class InitFileConfig implements ApplicationListener<ApplicationContextEvent> {

    public String getFileName() {
        if (isWindows()) {
            return "sigar-amd64-winnt.dll";
        } else {
            return "libsigar-amd64-linux.so";
        }
    }

    public String getFilePath(){
        if (isWindows()){
            return System.getProperty("java.library.path").split(";")[0] + File.separator;
        }else {
            return "/usr/lib64/";
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public void initFile() {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            final String filePath=getFilePath();
            final String fileName=getFileName();
            final File file = new File(filePath + fileName);
            //判断父目录是否存在，如果不存在，则创建
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                fis = new ClassPathResource("dll"+File.separator+fileName).getInputStream();//创建输入流对象
                fos = new FileOutputStream(filePath+ fileName); //创建输出流对象
                final byte[] data = new byte[1024];//创建搬运工具
                int len = 0;//创建长度
                while ((len = fis.read(data)) != -1) {
                    fos.write(data, 0, len);
                }
            }
        } catch (final Exception e) {
            log.error("init file error,"+ e.getMessage());
        } finally {
            try { if (fis != null) { fis.close(); } } catch (final IOException e) { e.printStackTrace(); }
            try { if (fos != null) { fos.close(); } } catch (final IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void onApplicationEvent(final ApplicationContextEvent event) {
        initFile();
    }
}
