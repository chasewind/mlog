package com.bird.core;

public class LineSeparatorConverter extends ClassicConverter {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public String convert(ILoggingEvent event) {
        return LINE_SEPARATOR;
    }

}
