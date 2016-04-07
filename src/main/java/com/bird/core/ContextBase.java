package com.bird.core;

import java.util.HashMap;
import java.util.Map;

public class ContextBase implements Context {

    private String      name;
    Map<String, Object> objectMap = new HashMap<String, Object>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject(String key) {
        return objectMap.get(key);
    }

    public void putObject(String key, Object value) {
        objectMap.put(key, value);
    }

}
