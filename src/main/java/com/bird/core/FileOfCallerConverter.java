package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public class FileOfCallerConverter extends ClassicConverter {

    public static final String NA = "?";

    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getFileName();
        } else {
            return NA;
        }
    }

}
