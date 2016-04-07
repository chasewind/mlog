package com.bird.core;

/**
 * <p>
 * 类SubstituteLog.java的实现描述：
 * </p>
 * <p>
 * 替代日志实现，用于委托代理的方式接入第三方的日志实现，如果第三方实现为空，则默认用{@link NOPLog}实现，不做任何操作
 * </p>
 * 
 * @author dongwei.ydw 2016年4月7日 下午9:41:11
 */
public class SubstituteLog implements Log {

    private final String name;
    private final Log    delegate;

    public SubstituteLog(String name, Log delegate){
        this.name = name;
        if (delegate == null) {
            this.delegate = new NOPLog();
        } else {
            this.delegate = delegate;
        }

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String msg) {
        this.delegate.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.delegate.debug(msg, t);

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String msg) {
        this.delegate.info(msg);

    }

    @Override
    public void info(String msg, Throwable t) {
        this.delegate.info(msg, t);

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {
        this.delegate.error(msg);

    }

    @Override
    public void error(String msg, Throwable t) {
        this.delegate.error(msg, t);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubstituteLog that = (SubstituteLog) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Log getDelegate() {
        return delegate;
    }

}
