package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public class LineOfCallerConverter extends ClassicConverter {

    public static String NA = "?";

    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return Integer.toString(cda[0].getLineNumber());
        } else {
            return NA;
        }
    }

}
