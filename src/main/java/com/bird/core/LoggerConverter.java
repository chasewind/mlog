package com.bird.core;

public class LoggerConverter extends NamedConverter {

    protected String getFullyQualifiedName(ILoggingEvent event) {
        return event.getLogName();
    }
}
