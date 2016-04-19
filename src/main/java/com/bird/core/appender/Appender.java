package com.bird.core.appender;

import com.bird.core.ContextAware;
import com.bird.core.LifeCycle;

public interface Appender<E> extends ContextAware, LifeCycle {

    /**
     * Get the name of this appender. The name uniquely identifies the appender.
     */
    String getName();

    /**
     * This is where an appender accomplishes its work. Note that the argument is of type Object.
     * 
     * @param event
     */
    void doAppend(E event) throws Exception;

    /**
     * Set the name of this appender. The name is used by other components to identify this appender.
     */
    void setName(String name);
}
