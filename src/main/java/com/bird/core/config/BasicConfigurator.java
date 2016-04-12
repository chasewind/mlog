package com.bird.core.config;

import com.bird.core.Configurator;
import com.bird.core.ConsoleAppender;
import com.bird.core.ContextAwareBase;
import com.bird.core.FinalLog;
import com.bird.core.ILoggingEvent;
import com.bird.core.Log;
import com.bird.core.LogContext;
import com.bird.core.layout.LayoutWrappingEncoder;
import com.bird.core.layout.TTLLLayout;

public class BasicConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LogContext logContext) {
        System.out.println("Setting up default configuration.");
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(logContext);
        ca.setName("console");

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
        encoder.setContext(logContext);

        TTLLLayout layout = new TTLLLayout();

        layout.setContext(logContext);
        layout.start();
        encoder.setLayout(layout);

        ca.setEncoder(encoder);
        ca.start();

        FinalLog rootLog = logContext.getLog(Log.ROOT_LOGGER_NAME);
        rootLog.addAppender(ca);
    }

}
