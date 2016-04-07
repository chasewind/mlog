package com.bird.core;

public class LoggingEvent implements ILoggingEvent {

    private String     logName;
    private LogContext logContext;
    private Level      level;
    private String     msg;
    private long       timeStamp;
    private String     threadName;
    private Throwable  throwable;

    public LoggingEvent(FinalLog log, Level level, String msg, Throwable t){
        this.logName = log.getName();
        this.logContext = log.getLogContext();
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
        if (this.throwable != null) {
            return this.throwable.getStackTrace();
        }
        return null;
    }

    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }

}
