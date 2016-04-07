package com.bird.core;

public interface ILoggingEvent {

    String getThreadName();

    String getMessage();

    String getLogName();

    StackTraceElement[] getCallerData();

    long getTimeStamp();
}
