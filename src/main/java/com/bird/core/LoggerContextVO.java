package com.bird.core;

import java.io.Serializable;
import java.util.Map;

public class LoggerContextVO implements Serializable {

    private static final long serialVersionUID = 2999022325585179396L;
    final String              name;
    final Map<String, String> propertyMap;
    final long                birthTime;

    public LoggerContextVO(LogContext lc){
        this.name = lc.getName();
        this.propertyMap = lc.getCopyOfPropertyMap();
        this.birthTime = lc.getBirthTime();
    }

    public LoggerContextVO(String name, Map<String, String> propertyMap, long birthTime){
        this.name = name;
        this.propertyMap = propertyMap;
        this.birthTime = birthTime;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public long getBirthTime() {
        return birthTime;
    }

    @Override
    public String toString() {
        return "LoggerContextVO{" + "name='" + name + '\'' + ", propertyMap=" + propertyMap + ", birthTime=" + birthTime
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggerContextVO)) return false;

        LoggerContextVO that = (LoggerContextVO) o;

        if (birthTime != that.birthTime) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (propertyMap != null ? !propertyMap.equals(that.propertyMap) : that.propertyMap != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (propertyMap != null ? propertyMap.hashCode() : 0);
        result = 31 * result + (int) (birthTime ^ (birthTime >>> 32));

        return result;
    }
}
