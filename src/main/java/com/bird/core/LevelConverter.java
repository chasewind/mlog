package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public class LevelConverter extends ClassicConverter {

    public String convert(ILoggingEvent le) {
        return le.getLevel().toString();
    }

}
