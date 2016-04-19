package com.bird.core.appender;

import java.io.OutputStream;

import com.bird.core.ConsoleTarget;

public class ConsoleAppender<E> extends OutputStreamAppender<E> {

    protected ConsoleTarget target = ConsoleTarget.SystemOut;

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
