package com.bird.core.appender;

import java.io.File;

import com.bird.core.LifeCycle;

public interface TriggeringPolicy<E> extends LifeCycle {

    boolean isTriggeringEvent(final File activeFile, final E event);
}
