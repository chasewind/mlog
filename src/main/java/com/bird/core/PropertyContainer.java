package com.bird.core;

import java.util.Map;

public interface PropertyContainer {

    String getProperty(String key);

    Map<String, String> getCopyOfPropertyMap();
}
