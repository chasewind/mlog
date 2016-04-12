package com.bird.core;

public class HostClassAndPropertyDouble {

    final Class<?> hostClass;
    final String   propertyName;

    public HostClassAndPropertyDouble(Class<?> hostClass, String propertyName){
        this.hostClass = hostClass;
        this.propertyName = propertyName;
    }

    public Class<?> getHostClass() {
        return hostClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hostClass == null) ? 0 : hostClass.hashCode());
        result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final HostClassAndPropertyDouble other = (HostClassAndPropertyDouble) obj;
        if (hostClass == null) {
            if (other.hostClass != null) return false;
        } else if (!hostClass.equals(other.hostClass)) return false;
        if (propertyName == null) {
            if (other.propertyName != null) return false;
        } else if (!propertyName.equals(other.propertyName)) return false;
        return true;
    }

}
