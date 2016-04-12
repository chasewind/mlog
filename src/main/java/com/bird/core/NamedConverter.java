package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public abstract class NamedConverter extends ClassicConverter {

    Abbreviator abbreviator = null;

    /**
     * Gets fully qualified name from event.
     * 
     * @param event The LoggingEvent to process, cannot not be null.
     * @return name, must not be null.
     */
    protected abstract String getFullyQualifiedName(final ILoggingEvent event);

    public void start() {
        String optStr = getFirstOption();
        if (optStr != null) {
            try {
                int targetLen = Integer.parseInt(optStr);
                if (targetLen == 0) {
                    abbreviator = new ClassNameOnlyAbbreviator();
                } else if (targetLen > 0) {
                    abbreviator = new TargetLengthBasedClassNameAbbreviator(targetLen);
                }
            } catch (NumberFormatException nfe) {
                // FIXME: better error reporting
            }
        }
    }

    public String convert(ILoggingEvent event) {
        String fqn = getFullyQualifiedName(event);

        if (abbreviator == null) {
            return fqn;
        } else {
            return abbreviator.abbreviate(fqn);
        }
    }
}
