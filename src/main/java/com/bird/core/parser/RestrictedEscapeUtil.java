package com.bird.core.parser;

public class RestrictedEscapeUtil implements IEscapeUtil {

    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        if (escapeChars.indexOf(next) >= 0) {
            buf.append(next);
        } else {
            // restitute the escape char (because it was consumed
            // before this method was called).
            buf.append("\\");
            // restitute the next character
            buf.append(next);
        }
    }

}
