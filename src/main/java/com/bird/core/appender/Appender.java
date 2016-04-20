package com.bird.core.appender;

import com.bird.core.ContextAware;
import com.bird.core.LifeCycle;

/**
 * 类Appender.java的实现描述：TODO 类实现描述
 * 
 * @author dongwei.ydw 2016年4月19日 下午7:30:26
 */
public interface Appender<E> extends ContextAware, LifeCycle {

    /**
     * 命名必须唯一
     */
    String getName();

    /**
     * 执行输出操作
     * 
     * @param event
     */
    void doAppend(E event) throws Exception;

    /**
     * 命名必须唯一
     */
    void setName(String name);
}
