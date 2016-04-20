package com.bird.core.appender;

import java.io.OutputStream;

import com.bird.core.ConsoleTarget;

/**
 * 类ConsoleAppender.java的实现描述：TODO 类实现描述:输出到控制台
 * 
 * @author dongwei.ydw 2016年4月19日 下午7:51:19
 */
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
