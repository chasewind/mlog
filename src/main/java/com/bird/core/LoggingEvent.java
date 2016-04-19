package com.bird.core;

public class LoggingEvent implements ILoggingEvent {

    private String          logName;
    private LogContext      logContext;
    private transient Level level;
    private String          msg;
    private long            timeStamp;
    private String          threadName;
    private Throwable       throwable;
    private LoggerContextVO loggerContextVO;
    transient String        formattedMessage;

    public LoggingEvent(FinalLog log, Level level, String msg, Throwable t){
        this.logName = log.getName();
        this.logContext = log.getLogContext();
        this.loggerContextVO = logContext.getLoggerContextRemoteView();
        this.level = level;
        this.msg = msg;
        this.throwable = t;
        this.timeStamp = System.currentTimeMillis();
    }

    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = (Thread.currentThread()).getName();
        }
        return this.threadName;
    }

    public void setThreadName(String threadName) throws IllegalStateException {
        if (this.threadName != null) {
            throw new IllegalStateException("threadName has been already set");
        }
        this.threadName = threadName;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String getLogName() {
        return this.logName;
    }

    @Override
    public StackTraceElement[] getCallerData() {
        return CallerData.extract(new Throwable(), FinalLog.class.getName(), 8, logContext.getFrameworkPackages());
    }

    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public LoggerContextVO getLogContextVO() {
        return this.loggerContextVO;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public String getFormattedMessage() {
        if (formattedMessage != null) {
            return formattedMessage;
        }
        formattedMessage = this.msg;

        return formattedMessage;
    }

    @Override
    public IThrowableProxy getThrowableProxy() {
        return null;
    }

}
