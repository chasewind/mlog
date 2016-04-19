package com.bird.core;

import com.bird.core.status.StatusManager;

public interface Context extends PropertyContainer {

    Object getObject(String key);

    void putObject(String key, Object value);

    String getProperty(String key);

    void putProperty(String key, String value);

    long getBirthTime();

    Object getConfigurationLock();

    String getName();

    void setName(String name);

    StatusManager getStatusManager();
}
