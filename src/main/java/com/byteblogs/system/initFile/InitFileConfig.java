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

@Component
@Slf4j
public class InitFileConfig implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private String FILE_PATH=System.getProperty("java.home").replaceFirst("jre","bin")+File.separator;
    private String RESOURCES_PATH=InitFileConfig.class.getClassLoader().getResource("dll").getPath()+File.separator;

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
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public  void initFile(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
       try{
           fis = new FileInputStream(new File(RESOURCES_PATH+getFileName()));//创建输入流对象
           File file=new File(FILE_PATH+getFileName());
           if (!file.exists()){
               fos = new FileOutputStream(FILE_PATH+getFileName()); //创建输出流对象
               byte datas[] = new byte[1024];//创建搬运工具
               int len = 0;//创建长度
               while((len = fis.read(datas))!=-1){
                   fos.write(datas,0,len);
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

    @Override
    public int getOrder() {
        return 2;
    }
}
