package com.bird.core;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public class LogFactory {

    static final int            UNINITIALIZED               = 0;
    static final int            ONGOING_INITIALIZATION      = 1;
    static final int            FAILED_INITIALIZATION       = 2;
    static final int            SUCCESSFUL_INITIALIZATION   = 3;
    static final int            NOP_FALLBACK_INITIALIZATION = 4;
    static volatile int         INITIALIZATION_STATE        = UNINITIALIZED;
    static NOPLogFactory        NOP_FALLBACK_FACTORY        = new NOPLogFactory();
    static SubstituteLogFactory SUBST_FACTORY               = new SubstituteLogFactory();

    static final String         UNSUCCESSFUL_INIT_MSG       = "log system init fail...";

    public static Log getLog(Class<?> clazz) {
        Log log = getLog(clazz.getName());
        return log;
    }

    public static Log getLog(String name) {
        ILogFactory logFactory = getILogFactory();
        return logFactory.getLog(name);
    }

    private final static void bind() {
        Set<URL> staticLogBinderPathSet = findPossibleStaticLogBinderPathSet();
        StaticLogBinder.getSingleton();
        INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
        reportActualBinding(staticLogBinderPathSet);
        replayEvents();
    }

    private static void replayEvents() {
        // TODO
    }

    private static void reportActualBinding(Set<URL> binderPathSet) {
        // TODO
        if (binderPathSet != null) {

        }
    }

    private final static void performInitialization() {
        bind();
    }

    public static ILogFactory getILogFactory() {
        if (INITIALIZATION_STATE == UNINITIALIZED) {
            synchronized (LogFactory.class) {
                if (INITIALIZATION_STATE == UNINITIALIZED) {
                    INITIALIZATION_STATE = ONGOING_INITIALIZATION;
                    performInitialization();
                }
            }
        }
        switch (INITIALIZATION_STATE) {
            case SUCCESSFUL_INITIALIZATION:
                return StaticLogBinder.getSingleton().getLogFactory();
            case NOP_FALLBACK_INITIALIZATION:
                return NOP_FALLBACK_FACTORY;
            case FAILED_INITIALIZATION:
                throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
            case ONGOING_INITIALIZATION:
                return SUBST_FACTORY;
        }
        throw new IllegalStateException("Unreachable code");
    }

    private static String STATIC_LOGGER_BINDER_PATH = "com/bird/core/StaticLogBinder.class";

    static Set<URL> findPossibleStaticLogBinderPathSet() {
        Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LogFactory.class.getClassLoader();
            Enumeration<URL> paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("class loader fail...");
        }
        return staticLoggerBinderPathSet;
    }

}
