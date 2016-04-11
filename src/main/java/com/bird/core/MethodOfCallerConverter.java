package com.bird.core;

public class MethodOfCallerConverter extends ClassicConverter {

    public static final String NA = "?";

    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getMethodName();
        } else {
            return NA;
        }
    }

}
