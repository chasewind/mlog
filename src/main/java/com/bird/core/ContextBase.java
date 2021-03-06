package com.bird.core;

import java.util.HashMap;
import java.util.Map;

import com.bird.core.status.BasicStatusManager;
import com.bird.core.status.StatusManager;

public class ContextBase implements Context, LifeCycle {

    private long               birthTime         = System.currentTimeMillis();
    private String             name;
    Map<String, Object>        objectMap         = new HashMap<String, Object>();
    Map<String, String>        propertyMap       = new HashMap<String, String>();
    public static final String CONTEXT_NAME_KEY  = "CONTEXT_NAME";
    Object                     configurationLock = new Object();
    private StatusManager      sm                = new BasicStatusManager();

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

    public void removeObject(String key) {
        objectMap.remove(key);
    }

    public Map<String, String> getCopyOfPropertyMap() {
        return new HashMap<String, String>(propertyMap);
    }

    @Override
    public long getBirthTime() {
        return birthTime;
    }

    @Override
    public String getProperty(String key) {
        if (CONTEXT_NAME_KEY.equals(key)) return getName();

        return (String) this.propertyMap.get(key);
    }

    @Override
    public void putProperty(String key, String value) {
        this.propertyMap.put(key, value);
    }

    public void reset() {
        removeShutdownHook();
        propertyMap.clear();
        objectMap.clear();
    }

    private void removeShutdownHook() {
        Thread hook = (Thread) getObject(CoreConstants.SHUTDOWN_HOOK_THREAD);
        if (hook != null) {
            removeObject(CoreConstants.SHUTDOWN_HOOK_THREAD);
            try {
                Runtime.getRuntime().removeShutdownHook(hook);
            } catch (IllegalStateException e) {
                // if JVM is already shutting down, ISE is thrown
                // no need to do anything else
            }
        }
    }

    @Override
    public Object getConfigurationLock() {
        return configurationLock;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isStarted() {
        // TODO Auto-generated method stub
        return false;
    }

    public StatusManager getStatusManager() {
        return sm;
    }

    public void setStatusManager(StatusManager statusManager) {
        // this method was added in response to http://jira.qos.ch/browse/LBCORE-35
        if (statusManager == null) {
            throw new IllegalArgumentException("null StatusManager not allowed");
        }
        this.sm = statusManager;
    }
}
