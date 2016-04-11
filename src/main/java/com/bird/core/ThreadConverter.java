package com.bird.core;

public class ThreadConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getThreadName();
    }

}
