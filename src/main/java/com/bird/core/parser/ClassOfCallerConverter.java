package com.bird.core.parser;

import com.bird.core.ILoggingEvent;
import com.bird.core.NamedConverter;

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
