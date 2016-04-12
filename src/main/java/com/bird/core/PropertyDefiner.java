package com.bird.core;

public interface PropertyDefiner extends ContextAware {

    /**
     * Get the property value, defined by this property definer
     * 
     * @return defined property value
     */
    String getPropertyValue();
}
