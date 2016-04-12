package com.bird.core;

import java.lang.reflect.Constructor;
import java.util.Properties;

import com.bird.core.exceptions.ScanException;
import com.bird.core.subst.NodeToStringTransformer;

public class OptionHelper {

    public static String getSystemProperty(String key, String def) {
        try {
            return System.getProperty(key, def);
        } catch (SecurityException e) {
            return def;
        }
    }

    public static String getEnv(String key) {
        try {
            return System.getenv(key);
        } catch (SecurityException e) {
            return null;
        }
    }

    public static boolean toBoolean(String value, boolean dEfault) {
        if (value == null) {
            return dEfault;
        }

        String trimmedVal = value.trim();

        if ("true".equalsIgnoreCase(trimmedVal)) {
            return true;
        }

        if ("false".equalsIgnoreCase(trimmedVal)) {
            return false;
        }

        return dEfault;
    }

    public static ClassLoader getClassLoaderOfClass(final Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return cl;
        }
    }

    public static Object instantiateByClassName(String className, Class<?> superClass,
                                                Context context) throws Exception {
        if (context == null) {
            throw new NullPointerException("Argument cannot be null");
        }
        ClassLoader classLoader = getClassLoaderOfClass(context.getClass());
        return instantiateByClassName(className, superClass, classLoader);
    }

    public static Object instantiateByClassName(String className, Class<?> superClass,
                                                ClassLoader classLoader) throws Exception {
        return instantiateByClassNameAndParameter(className, superClass, classLoader, null, null);
    }

    public static Object instantiateByClassNameAndParameter(String className, Class<?> superClass,
                                                            ClassLoader classLoader, Class<?> type,
                                                            Object parameter) throws Exception {

        if (className == null) {
            throw new NullPointerException();
        }
        try {
            Class<?> classObj = null;
            classObj = classLoader.loadClass(className);
            if (!superClass.isAssignableFrom(classObj)) {
                throw new IncompatibleClassException(superClass, classObj);
            }
            if (type == null) {
                return classObj.newInstance();
            } else {
                Constructor<?> constructor = classObj.getConstructor(type);
                return constructor.newInstance(parameter);
            }
        } catch (IncompatibleClassException ice) {
            throw ice;
        } catch (Throwable t) {
            throw new DynamicClassLoadingException("Failed to instantiate type " + className, t);
        }
    }

    public static String substVars(String val, PropertyContainer pc1) {
        return substVars(val, pc1, null);
    }

    /**
     * See http://logback.qos.ch/manual/configuration.html#variableSubstitution
     */
    public static String substVars(String input, PropertyContainer pc0, PropertyContainer pc1) {
        try {
            return NodeToStringTransformer.substituteVariable(input, pc0, pc1);
        } catch (ScanException e) {
            throw new IllegalArgumentException("Failed to parse input [" + input + "]", e);
        }
    }

    public static boolean isEmpty(String str) {
        return ((str == null) || CoreConstants.EMPTY_STRING.equals(str));
    }

    public static void setSystemProperty(ContextAware contextAware, String key, String value) {
        try {
            System.setProperty(key, value);
        } catch (SecurityException e) {
            contextAware.addError("Failed to set system property [" + key + "]", e);
        }
    }

    /**
     * Very similar to {@link System#getProperties()} except that the {@link SecurityException} is absorbed.
     *
     * @return the system properties
     */
    public static Properties getSystemProperties() {
        try {
            return System.getProperties();
        } catch (SecurityException e) {
            return new Properties();
        }
    }

    public static void setSystemProperties(ContextAware contextAware, Properties props) {
        for (Object o : props.keySet()) {
            String key = (String) o;
            String value = props.getProperty(key);
            setSystemProperty(contextAware, key, value);
        }
    }
}
