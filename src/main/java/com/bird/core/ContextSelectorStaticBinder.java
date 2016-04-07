package com.bird.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ContextSelectorStaticBinder {

    static ContextSelectorStaticBinder singleton = new ContextSelectorStaticBinder();

    ContextSelector                    contextSelector;
    Object                             key;

    public static ContextSelectorStaticBinder getSingleton() {
        return singleton;
    }

    public void init(LogContext defaultLogContext, Object key) throws ClassNotFoundException, NoSuchMethodException,
                                                               InstantiationException, IllegalAccessException,
                                                               InvocationTargetException {
        if (this.key == null) {
            this.key = key;
        } else if (this.key != key) {
            throw new IllegalAccessException("Only certain classes can access this method.");
        }

        String contextSelectorStr = System.getProperty("log.ContextSelector");
        // 不配置系统属性， 不处理JNDI
        if (contextSelectorStr == null) {
            contextSelector = new DefaultContextSelector(defaultLogContext);
        } else {
            contextSelector = dynamicalContextSelector(defaultLogContext, contextSelectorStr);
        }
    }

    static ContextSelector dynamicalContextSelector(LogContext defaultLogContext,
                                                    String contextSelectorStr) throws ClassNotFoundException,
                                                                               SecurityException, NoSuchMethodException,
                                                                               IllegalArgumentException,
                                                                               InstantiationException,
                                                                               IllegalAccessException,
                                                                               InvocationTargetException {
        Class<?> contextSelectorClass = Class.forName(contextSelectorStr);
        Constructor cons = contextSelectorClass.getConstructor(new Class[] { LogContext.class });
        return (ContextSelector) cons.newInstance(defaultLogContext);
    }

    public ContextSelector getContextSelector() {
        return contextSelector;
    }

}
