package com.bird.core;

public interface Context {

    Object getObject(String key);

    void putObject(String key, Object value);

    long getBirthTime();
}
