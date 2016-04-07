package com.bird.core;

import java.lang.reflect.InvocationTargetException;

public class StaticLogBinder implements LogFactoryBinder {

    private static StaticLogBinder            SINGLETON             = new StaticLogBinder();
    private LogContext                        defaultLogContext     = new LogContext();
    private final ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
    private static Object                     KEY                   = new Object();
    private boolean                           initialized           = false;
    static {
        SINGLETON.init();
    }

    private StaticLogBinder(){
        defaultLogContext.setName("default");
    }

    void init() {
        try {
            new ContextInitializer(defaultLogContext).autoConfig();
            contextSelectorBinder.init(defaultLogContext, KEY);
            initialized = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static StaticLogBinder getSingleton() {
        return SINGLETON;
    }

    static void reset() {
        SINGLETON = new StaticLogBinder();
        SINGLETON.init();
    }

    @Override
    public ILogFactory getLogFactory() {
        if (!initialized) {
            return defaultLogContext;
        }

        if (contextSelectorBinder.getContextSelector() == null) {
            throw new IllegalStateException("contextSelector cannot be null.");
        }
        return contextSelectorBinder.getContextSelector().getLogContext();
    }

    @Override
    public String getLogFactoryClassStr() {
        return contextSelectorBinder.getClass().getName();
    }
}
