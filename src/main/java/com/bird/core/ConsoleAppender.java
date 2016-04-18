package com.bird.core;

import java.io.OutputStream;

public class ConsoleAppender<E> extends OutputStreamAppender<E> {

    protected ConsoleTarget target = ConsoleTarget.SystemOut;

    @Override
    protected void append(E event) {
        System.out.println("console appender do------");
    }

    public void setTarget(String value) {
        ConsoleTarget t = ConsoleTarget.findByName(value.trim());
        if (t == null) {
            targetWarn(value);
        } else {
            target = t;
        }
    }

    public String getTarget() {
        return target.getName();
    }

    private void targetWarn(String value) {
        System.err.println("cannot find target\n");
    }

    @Override
    public void start() {
        OutputStream targetStream = target.getStream();
        setOutputStream(targetStream);
        super.start();
    }

}
