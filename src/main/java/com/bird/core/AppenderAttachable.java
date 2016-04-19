package com.bird.core;

import java.util.Iterator;

import com.bird.core.appender.Appender;

public interface AppenderAttachable<E> {

    /**
     * Add an appender.
     */
    void addAppender(Appender<E> newAppender);

    /**
     * Get an iterator for appenders contained in the parent object.
     */
    Iterator<Appender<E>> iteratorForAppenders();

    /**
     * Get an appender by name.
     */
    Appender<E> getAppender(String name);

    /**
     * Returns <code>true</code> if the specified appender is in list of attached attached, <code>false</code>
     * otherwise.
     */
    boolean isAttached(Appender<E> appender);

    /**
     * Detach and processPriorToRemoval all previously added appenders.
     */
    void detachAndStopAllAppenders();

    /**
     * Detach the appender passed as parameter from the list of appenders.
     */
    boolean detachAppender(Appender<E> appender);

    /**
     * Detach the appender with the name passed as parameter from the list of appenders.
     */
    boolean detachAppender(String name);
}
