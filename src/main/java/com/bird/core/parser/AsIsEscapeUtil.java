package com.bird.core.parser;

public class AsIsEscapeUtil implements IEscapeUtil {

    /**
     * Do not perform any character escaping.
     * <p/>
     * Note that this method assumes that it is called after the escape character has been consumed.
     */
    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        // restitute the escape char (because it was consumed
        // before this method was called).
        buf.append("\\");
        // restitute the next character
        buf.append(next);
    }
}
