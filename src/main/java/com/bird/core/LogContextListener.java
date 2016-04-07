package com.bird.core;

public interface LogContextListener {

    boolean isResetResistant();

    void onStart(LogContext context);

    void onReset(LogContext context);

    void onStop(LogContext context);

    void onLevelChange(Log log, Level level);
}
