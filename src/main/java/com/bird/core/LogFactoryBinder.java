package com.bird.core;

/**
 * <p>
 * 类LogFactoryBinder.java的实现描述
 * </p>
 * <p>
 * 内部实现，确保第三方日志框架可以接入绑定{@link ILogFactory}的实例
 * </p>
 * 
 * @author dongwei.ydw 2016年4月8日 上午9:38:41
 */
public interface LogFactoryBinder {

    public ILogFactory getLogFactory();

    public String getLogFactoryClassStr();
}
