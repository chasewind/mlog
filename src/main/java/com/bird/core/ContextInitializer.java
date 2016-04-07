package com.bird.core;

import java.net.URL;

/**
 * <p>
 * 类ContextInitializer.java的实现描述
 * </p>
 * <p>
 * 该类实现日志的自动加载配置
 * </p>
 * 
 * @author dongwei.ydw 2016年4月8日 上午9:45:22
 */
public class ContextInitializer {

    final public static String AUTOCONFIG_FILE = "log.xml";
    final LogContext           logContext;

    public ContextInitializer(LogContext logContext){
        this.logContext = logContext;
    }

    public static ClassLoader getClassLoaderOfClass(final Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return cl;
        }
    }

    public void autoConfig() {
        ClassLoader classLoader = getClassLoaderOfClass(this.getClass());
        URL url = classLoader.getResource(AUTOCONFIG_FILE);
        if (url != null) {
            configureByResource(url);
        } else {
            BasicConfigurator basicConfigurator = new BasicConfigurator();
            basicConfigurator.setContext(logContext);
            basicConfigurator.configure(logContext);
        }
    }

    public void configureByResource(URL url) {

    }

}
