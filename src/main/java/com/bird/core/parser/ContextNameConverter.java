package com.bird.core.parser;

import com.bird.core.ILoggingEvent;

public class ContextNameConverter extends ClassicConverter {

    /**
     * Return the name of the logger context's name.
     */
    public String convert(ILoggingEvent event) {
        return event.getLogContextVO().getName();
    }

}
