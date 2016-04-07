package com.bird.core;

public class BasicConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LogContext logContext) {
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(logContext);
        ca.setName("console");

        FinalLog rootLog = logContext.getLog(Log.ROOT_LOGGER_NAME);
        rootLog.addAppender(ca);
    }

}
