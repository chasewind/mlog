package com.bird.core;

import java.util.HashMap;
import java.util.Map;

public class ContextBase implements Context {

    private long        birthTime   = System.currentTimeMillis();
    private String      name;
    Map<String, Object> objectMap   = new HashMap<String, Object>();
    Map<String, String> propertyMap = new HashMap<String, String>();

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

    public Map<String, String> getCopyOfPropertyMap() {
        return new HashMap<String, String>(propertyMap);
    }

    @Override
    public long getBirthTime() {
        return birthTime;
    }
}
