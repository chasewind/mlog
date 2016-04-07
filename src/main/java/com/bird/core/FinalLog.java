package com.bird.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class FinalLog implements Log {

    private String                                          name;
    transient private Level                                 level;
    transient private FinalLog                              parent;
    transient private List<FinalLog>                        childrenList;
    final transient LogContext                              logContext;
    transient private int                                   effectiveLevelInt;
    transient private AppenderAttachableImpl<ILoggingEvent> aai;
    transient private boolean                               additive = true;

    FinalLog(String name, FinalLog parent, LogContext logContext){
        this.name = name;
        this.parent = parent;
        this.logContext = logContext;
    }

    public LogContext getLogContext() {
        return this.logContext;
    }

    public Level getEffectiveLevel() {
        return Level.toLevel(effectiveLevelInt);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDebugEnabled() {
        return effectiveLevelInt <= Level.DEBUG_INT;
    }

    @Override
    public void debug(String msg) {
        doLog(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String msg, Throwable t) {
        doLog(Level.DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return effectiveLevelInt <= Level.INFO_INT;
    }

    @Override
    public void info(String msg) {
        doLog(Level.INFO, msg, null);
    }

    @Override
    public void info(String msg, Throwable t) {
        doLog(Level.INFO, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return effectiveLevelInt <= Level.ERROR_INT;
    }

    @Override
    public void error(String msg) {
        doLog(Level.ERROR, msg, null);
    }

    @Override
    public void error(String msg, Throwable t) {
        doLog(Level.ERROR, msg, t);
    }

    public boolean isAdditive() {
        return additive;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    private void doLog(Level level, String msg, Throwable t) {
        String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) + "\tcurrent level--->"
                        + level;
        if (effectiveLevelInt > level.levelInt) {
            result += " is higher ,do nothing";
            System.out.println(result);
            return;
        }
        result += " fit,do something";
        System.out.println(result);
        //
        LoggingEvent le = new LoggingEvent(this, level, msg, t);
        callAppenders(le);
    }

    public void callAppenders(ILoggingEvent event) {
        int writes = 0;
        for (FinalLog l = this; l != null; l = l.parent) {
            writes += l.appendLoopOnAppenders(event);
            if (!l.additive) {
                break;
            }
        }
        if (writes == 0) {
            logContext.noAppenderDefinedWarning(this);
        }
    }

    private int appendLoopOnAppenders(ILoggingEvent event) {
        if (aai != null) {
            return aai.appendLoopOnAppenders(event);
        } else {
            return 0;
        }
    }

    public synchronized void addAppender(Appender<ILoggingEvent> newAppender) {
        if (aai == null) {
            aai = new AppenderAttachableImpl<ILoggingEvent>();
        }
        aai.addAppender(newAppender);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        if (this.level == level) {
            return;
        }
        if (level == null && isRootLog()) {
            throw new IllegalArgumentException("The level of the root logger cannot be set to null");
        }

        this.level = level;
        if (level == null) {
            effectiveLevelInt = parent.effectiveLevelInt;
            level = parent.getEffectiveLevel();
        } else {
            effectiveLevelInt = level.levelInt;
        }

        if (childrenList != null) {
            int len = childrenList.size();
            for (int i = 0; i < len; i++) {
                FinalLog child = (FinalLog) childrenList.get(i);
                child.handleParentLevelChange(effectiveLevelInt);
            }
        }
        logContext.fireOnLevelChange(this, level);
    }

    FinalLog getChildByName(final String childName) {
        if (childrenList == null) {
            return null;
        } else {
            int len = this.childrenList.size();
            for (int i = 0; i < len; i++) {
                final FinalLog childLog_i = (FinalLog) childrenList.get(i);
                final String childName_i = childLog_i.getName();

                if (childName.equals(childName_i)) {
                    return childLog_i;
                }
            }
            return null;
        }
    }

    FinalLog createChildByName(final String childName) {
        int i_index = LoggerNameUtil.getSeparatorIndexOf(childName, this.name.length() + 1);
        if (i_index != -1) {
            throw new IllegalArgumentException("For logger [" + this.name + "] child name [" + childName
                                               + " passed as parameter, may not include '.' after index"
                                               + (this.name.length() + 1));
        }

        if (childrenList == null) {
            childrenList = new CopyOnWriteArrayList<FinalLog>();
        }
        FinalLog childLog;
        childLog = new FinalLog(childName, this, this.logContext);
        childrenList.add(childLog);
        childLog.effectiveLevelInt = this.effectiveLevelInt;
        return childLog;
    }

    private synchronized void handleParentLevelChange(int newParentLevelInt) {
        if (level == null) {
            effectiveLevelInt = newParentLevelInt;

            if (childrenList != null) {
                int len = childrenList.size();
                for (int i = 0; i < len; i++) {
                    FinalLog child = (FinalLog) childrenList.get(i);
                    child.handleParentLevelChange(newParentLevelInt);
                }
            }
        }
    }

    private boolean isRootLog() {
        return parent == null;
    }
}
