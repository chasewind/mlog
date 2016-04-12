package com.bird.core.jaran;

import java.util.HashMap;
import java.util.Map;

public class BeanDescriptionCache {

    private Map<Class<?>, BeanDescription> classToBeanDescription = new HashMap<Class<?>, BeanDescription>();

    /**
     * Returned bean descriptions are hold in a cache. If the cache does not contain a description for a given class, a
     * new bean description is created and put in the cache, before it is returned.
     *
     * @param clazz to get a bean description for.
     * @return a bean description for the given class.
     */
    public BeanDescription getBeanDescription(Class<?> clazz) {
        if (!classToBeanDescription.containsKey(clazz)) {
            BeanDescription beanDescription = BeanDescriptionFactory.INSTANCE.create(clazz);
            classToBeanDescription.put(clazz, beanDescription);
        }
        return classToBeanDescription.get(clazz);
    }

}
