package com.bird.demo;

import com.bird.core.Log;
import com.bird.core.LogFactory;

public class LogDemo {

    private static Log log = LogFactory.getLog(LogDemo.class);

    public static void main(String[] args) {
        log.debug("debug...");
        log.info("info...");
        log.error("error...");
    }
}
