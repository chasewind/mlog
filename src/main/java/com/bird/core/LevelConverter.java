package com.bird.core;

public class LevelConverter extends ClassicConverter {

    public String convert(ILoggingEvent le) {
        return le.getLevel().toString();
    }

}
