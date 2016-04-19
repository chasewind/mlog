package com.bird.core.config;

import com.bird.core.Configurator;
import com.bird.core.ContextAwareBase;
import com.bird.core.FinalLog;
import com.bird.core.ILoggingEvent;
import com.bird.core.Log;
import com.bird.core.LogContext;
import com.bird.core.appender.ConsoleAppender;
import com.bird.core.layout.LayoutWrappingEncoder;
import com.bird.core.layout.TTLLLayout;

/**
 * 类BasicConfigurator.java的实现描述：TODO 类实现描述:做一个基本的实现，控制台打印出结果，日志格式采用{@link com.bird.core.layout.TTLLLayout}
 * 
 * @author dongwei.ydw 2016年4月18日 下午7:46:39
 */
public class BasicConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LogContext logContext) {
        addInfo("Setting up default configuration.");
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
