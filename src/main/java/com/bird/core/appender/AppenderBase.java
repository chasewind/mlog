package com.bird.core.appender;

import com.bird.core.ContextAwareBase;

public abstract class AppenderBase<E> extends ContextAwareBase implements Appender<E> {

    protected String             name;
    private ThreadLocal<Boolean> guard             = new ThreadLocal<Boolean>();
    protected volatile boolean   started           = false;
    private int                  statusRepeatCount = 0;
    private int                  exceptionCount    = 0;

    static final int             ALLOWED_REPEATS   = 5;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public synchronized void doAppend(E event) throws Exception {
        if (Boolean.TRUE.equals(guard.get())) {
            return;
        }

        try {
            guard.set(Boolean.TRUE);

            if (!this.started) {
                if (statusRepeatCount++ < ALLOWED_REPEATS) {
                    System.err.println("Attempted to append to non started appender [" + name + "].\n");
                }
                return;
            }

            this.append(event);

        } catch (Exception e) {
            if (exceptionCount++ < ALLOWED_REPEATS) {
                System.err.println("Appender [" + name + "] failed to append.\n");
            }
        } finally {
            guard.set(Boolean.FALSE);
        }
    }

    abstract protected void append(E event);

    public String toString() {
        return this.getClass().getName() + "[" + name + "]";
    }
}
