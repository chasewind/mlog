package com.bird.core;

/**
 * <p>
 * 类Log.java的实现描述
 * </p>
 * <p>
 * 自定义日志类，模仿标准的logback，这里日志只定义为debug，info和error，打印日志的格式也做限定
 * </p>
 * 
 * @author dongwei.ydw 2016年4月7日 下午8:34:02
 */
public interface Log {

    final public String ROOT_LOGGER_NAME = "ROOT";

    public String getName();

    public boolean isDebugEnabled();

    public void debug(String msg);

    public void debug(String msg, Throwable t);

    public boolean isInfoEnabled();

    public void info(String msg);

    public void info(String msg, Throwable t);

    public boolean isErrorEnabled();

    public void error(String msg);

    public void error(String msg, Throwable t);

}
