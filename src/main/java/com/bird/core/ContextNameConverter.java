package com.bird.core;

public class ContextNameConverter extends ClassicConverter {

    /**
     * Return the name of the logger context's name.
     */
    public String convert(ILoggingEvent event) {
        return event.getLogContextVO().getName();
    }

}
