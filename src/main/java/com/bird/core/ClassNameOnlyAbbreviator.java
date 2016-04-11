package com.bird.core;

public class ClassNameOnlyAbbreviator implements Abbreviator {

    public static final char DOT = '.';

    public String abbreviate(String fqClassName) {
        // we ignore the fact that the separator character can also be a dollar
        // If the inner class is org.good.AClass#Inner, returning
        // AClass#Inner seems most appropriate
        int lastIndex = fqClassName.lastIndexOf(DOT);
        if (lastIndex != -1) {
            return fqClassName.substring(lastIndex + 1, fqClassName.length());
        } else {
            return fqClassName;
        }
    }
}
