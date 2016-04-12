package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public class ThreadConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getThreadName();
    }

}
