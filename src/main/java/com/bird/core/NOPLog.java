package com.bird.core;

/**
 * <p>
 * 类NOPLog.java的实现描述
 * </p>
 * <p>
 * 该类是不做任何日志纪录，只满足语义的完整性
 * </p>
 * 
 * @author dongwei.ydw 2016年4月7日 下午9:43:56
 */
public class NOPLog implements Log {

    public static final NOPLog NOP_LOG = new NOPLog();

    @Override
    public String getName() {
        return "NoPLog";
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {
    }

    @Override
    public void error(String msg, Throwable t) {
    }

}
