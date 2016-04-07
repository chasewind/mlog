package com.bird.core;

import java.util.List;

public interface ContextSelector {

    LogContext getLogContext();

    LogContext getLogContext(String name);

    LogContext getDefaultLogContext();

    LogContext detachLogContext(String LogContextName);

    List<String> getContextNames();
}
