package com.byteblogs.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
@Component
public class BeanTool implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanTool.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取当前服务（应用）名称
     * @return
     */
    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    /**
     * 根据Bean名称获取 Bean对象
     * @param
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据Bean名称获取 Bean对象
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 根据Class获取 Bean对象
     * @param
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取配置文件中的属性值
     * @return
     */
    public static String getProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }
}
