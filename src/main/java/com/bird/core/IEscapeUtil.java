package com.bird.core;

public interface IEscapeUtil {

    void escape(String additionalEscapeChars, StringBuffer buf, char next, int pointer);
}
