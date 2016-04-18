package com.bird.core;

public interface LoggerContextListener {

    /**
     * Some listeners should not be removed when the LoggerContext is reset. Such listeners are said to be reset
     * resistant.
     * 
     * @return whether this listener is reset resistant or not.
     */
    boolean isResetResistant();

    void onStart(LogContext context);

    void onReset(LogContext context);

    void onStop(LogContext context);

    void onLevelChange(FinalLog logger, Level level);
}
