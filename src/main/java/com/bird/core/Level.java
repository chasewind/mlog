package com.bird.core;

/**
 * <p>
 * 类Level.java的实现描述
 * </p>
 * <p>
 * 日志级别
 * </p>
 * 
 * @author dongwei.ydw 2016年4月7日 下午8:40:17
 */
public final class Level implements java.io.Serializable {

    private static final long   serialVersionUID = 869298538466971421L;
    public static final int     OFF_INT          = Integer.MAX_VALUE;
    public static final int     ERROR_INT        = 40000;
    public static final int     INFO_INT         = 20000;
    public static final int     DEBUG_INT        = 10000;
    public static final int     ALL_INT          = Integer.MIN_VALUE;

    public static final Integer OFF_INTEGER      = OFF_INT;
    public static final Integer ERROR_INTEGER    = ERROR_INT;
    public static final Integer INFO_INTEGER     = INFO_INT;
    public static final Integer DEBUG_INTEGER    = DEBUG_INT;
    public static final Integer ALL_INTEGER      = ALL_INT;

    /**
     * The <code>OFF</code> is used to turn off logging.
     */
    public static final Level   OFF              = new Level(OFF_INT, "OFF");

    /**
     * The <code>ERROR</code> level designates error events which may or not be fatal to the application.
     */
    public static final Level   ERROR            = new Level(ERROR_INT, "ERROR");

    /**
     * The <code>INFO</code> level designates informational messages highlighting overall progress of the application.
     */
    public static final Level   INFO             = new Level(INFO_INT, "INFO");

    /**
     * The <code>DEBUG</code> level designates informational events of lower importance.
     */
    public static final Level   DEBUG            = new Level(DEBUG_INT, "DEBUG");

    /**
     * The <code>ALL</code> is used to turn on all logging.
     */
    public static final Level   ALL              = new Level(ALL_INT, "ALL");

    public final int            levelInt;
    public final String         levelStr;

    /**
     * Instantiate a Level object.
     */
    private Level(int levelInt, String levelStr){
        this.levelInt = levelInt;
        this.levelStr = levelStr;
    }

    /**
     * Returns the string representation of this Level.
     */
    public String toString() {
        return levelStr;
    }

    /**
     * Returns the integer representation of this Level.
     */
    public int toInt() {
        return levelInt;
    }

    /**
     * Convert a Level to an Integer object.
     *
     * @return This level's Integer mapping.
     */
    public Integer toInteger() {
        switch (levelInt) {
            case ALL_INT:
                return ALL_INTEGER;
            case DEBUG_INT:
                return DEBUG_INTEGER;
            case INFO_INT:
                return INFO_INTEGER;
            case ERROR_INT:
                return ERROR_INTEGER;
            case OFF_INT:
                return OFF_INTEGER;
            default:
                throw new IllegalStateException("Level " + levelStr + ", " + levelInt + " is unknown.");
        }
    }

    /**
     * Returns <code>true</code> if this Level has a higher or equal Level than the Level passed as argument,
     * <code>false</code> otherwise.
     */
    public boolean isGreaterOrEqual(Level r) {
        return levelInt >= r.levelInt;
    }

    /**
     * Convert the string passed as argument to a Level. If the conversion fails, then this method returns
     * {@link #DEBUG}.
     */
    public static Level toLevel(String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }

    /**
     * This method exists in order to comply with Joran's valueOf convention.
     *
     * @param sArg
     * @return
     */
    public static Level valueOf(String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a Level. If the conversion fails, then this method returns
     * {@link #DEBUG}.
     */
    public static Level toLevel(int val) {
        return toLevel(val, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a Level. If the conversion fails, then this method returns the specified
     * default.
     */
    public static Level toLevel(int val, Level defaultLevel) {
        switch (val) {
            case ALL_INT:
                return ALL;
            case DEBUG_INT:
                return DEBUG;
            case INFO_INT:
                return INFO;
            case ERROR_INT:
                return ERROR;
            case OFF_INT:
                return OFF;
            default:
                return defaultLevel;
        }
    }

    public static Level toLevel(String sArg, Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }

        if (sArg.equalsIgnoreCase("ALL")) {
            return Level.ALL;
        }

        if (sArg.equalsIgnoreCase("DEBUG")) {
            return Level.DEBUG;
        }
        if (sArg.equalsIgnoreCase("INFO")) {
            return Level.INFO;
        }

        if (sArg.equalsIgnoreCase("ERROR")) {
            return Level.ERROR;
        }
        if (sArg.equalsIgnoreCase("OFF")) {
            return Level.OFF;
        }
        return defaultLevel;
    }

    private Object readResolve() {
        return toLevel(this.levelInt);
    }

}
