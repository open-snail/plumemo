package com.byteblogs.system.initFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class InitFileConfig implements ApplicationListener<ApplicationContextEvent> {

    private String FILE_PATH=System.getProperty("java.home").replaceFirst("jre","bin")+File.separator;
    private String RESOURCES_PATH= Objects.requireNonNull(InitFileConfig.class.getClassLoader().getResource("dll")).getPath()+File.separator;

    public String getFileName(){
        if (isWindows()){
            return "sigar-amd64-winnt.dll";
        }else{
            return "libsigar-amd64-linux.so";
        }
    }
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public  void initFile(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
       try{
           File file=new File(FILE_PATH+getFileName());
           if (!file.exists()){
               fis = new FileInputStream(new File(RESOURCES_PATH+getFileName()));//创建输入流对象
               fos = new FileOutputStream(FILE_PATH+getFileName()); //创建输出流对象
               byte data[] = new byte[1024];//创建搬运工具
               int len = 0;//创建长度
               while((len = fis.read(data))!=-1){
                   fos.write(data,0,len);
               }
           }
       } catch (Exception e){e.printStackTrace();}
       finally {
           try { if(fis!=null){fis.close();} } catch (IOException e) { e.printStackTrace(); }
           try { if (fos!=null){fos.close();}} catch (IOException e) { e.printStackTrace(); }
       }
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        initFile();
    }
}
