package com.bird.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogContext extends ContextBase implements ILogFactory {

    final FinalLog                         root;

    private Map<String, FinalLog>          logCache;
    final private List<LogContextListener> logContextListenerList = new ArrayList<LogContextListener>();
    private int                            size;

    public LogContext(){
        super();
        this.logCache = new ConcurrentHashMap<String, FinalLog>();

        this.root = new FinalLog(Log.ROOT_LOGGER_NAME, null, this);
        this.root.setLevel(Level.DEBUG);
        logCache.put(Log.ROOT_LOGGER_NAME, root);
    }

    @Override
    public FinalLog getLog(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        if (Log.ROOT_LOGGER_NAME.equalsIgnoreCase(name)) {
            return root;
        }
        int i = 0;
        FinalLog log = root;

        FinalLog childLog = (FinalLog) logCache.get(name);
        if (childLog != null) {
            return childLog;
        }

        // if the desired logger does not exist, them create all the loggers
        // in between as well (if they don't already exist)
        String childName;
        while (true) {
            int h = LoggerNameUtil.getSeparatorIndexOf(name, i);
            if (h == -1) {
                childName = name;
            } else {
                childName = name.substring(0, h);
            }
            // move i left of the last point
            i = h + 1;
            synchronized (log) {
                childLog = log.getChildByName(childName);
                if (childLog == null) {
                    childLog = log.createChildByName(childName);
                    logCache.put(childName, childLog);
                    incSize();
                }
            }
            log = childLog;
            if (h == -1) {
                return childLog;
            }
        }
    }

    private void incSize() {
        size++;
    }

    int size() {
        return size;
    }

    void fireOnLevelChange(FinalLog logger, Level level) {
        for (LogContextListener listener : logContextListenerList) {
            listener.onLevelChange(logger, level);
        }
    }

    public void noAppenderDefinedWarning(FinalLog finalLog) {
        System.out.println("log has no appender..");

    }

    public boolean isPackagingDataEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
}
