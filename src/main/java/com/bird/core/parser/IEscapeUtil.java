package com.bird.core.parser;

public interface IEscapeUtil {

    void escape(String additionalEscapeChars, StringBuffer buf, char next, int pointer);
}
