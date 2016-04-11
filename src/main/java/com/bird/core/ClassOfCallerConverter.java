package com.bird.core;

public class ClassOfCallerConverter extends NamedConverter {

    public static String NA = "?";

    protected String getFullyQualifiedName(ILoggingEvent event) {

        StackTraceElement[] cda = event.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getClassName();
        } else {
            return NA;
        }
    }
}
