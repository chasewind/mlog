package com.bird.core;

import java.util.Arrays;
import java.util.List;

public class DefaultContextSelector implements ContextSelector {

    private LogContext defaultLogContext;

    public DefaultContextSelector(LogContext context){
        this.defaultLogContext = context;
    }

    public LogContext getLogContext() {
        return getDefaultLogContext();
    }

    public LogContext getDefaultLogContext() {
        return defaultLogContext;
    }

    public LogContext detachLogContext(String LogContextName) {
        return defaultLogContext;
    }

    public List<String> getContextNames() {
        return Arrays.asList(defaultLogContext.getName());
    }

    public LogContext getLogContext(String name) {
        if (defaultLogContext.getName().equals(name)) {
            return defaultLogContext;
        } else {
            return null;
        }
    }
}
