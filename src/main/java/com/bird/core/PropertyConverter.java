package com.bird.core;

import java.util.Map;

import com.bird.core.parser.ClassicConverter;

public class PropertyConverter extends ClassicConverter {

    String key;

    public void start() {
        String optStr = getFirstOption();
        if (optStr != null) {
            key = optStr;
            super.start();
        }
    }

    public String convert(ILoggingEvent event) {
        if (key == null) {
            return "Property_HAS_NO_KEY";
        } else {
            LoggerContextVO lcvo = event.getLogContextVO();
            Map<String, String> map = lcvo.getPropertyMap();
            String val = map.get(key);
            if (val != null) {
                return val;
            } else {
                return System.getProperty(key);
            }
        }
    }
}
