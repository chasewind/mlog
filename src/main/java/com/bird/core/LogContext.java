package com.bird.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class LogContext extends ContextBase implements ILogFactory, LifeCycle {

    final FinalLog                         root;

    private Map<String, FinalLog>          logCache;
    final private List<LogContextListener> logContextListenerList             = new ArrayList<LogContextListener>();
    private int                            size;
    private LoggerContextVO                logContextRemoteView;
    public static final String             EVALUATOR_MAP                      = "EVALUATOR_MAP";
    private List<String>                   frameworkPackages;
    public static final boolean            DEFAULT_PACKAGING_DATA             = false;
    private boolean                        packagingDataEnabled               = DEFAULT_PACKAGING_DATA;
    int                                    resetCount                         = 0;
    public static final String             FA_FILENAME_COLLISION_MAP          = "RFA_FILENAME_COLLISION_MAP";
    public static final String             RFA_FILENAME_PATTERN_COLLISION_MAP = "RFA_FILENAME_PATTERN_COLLISION_MAP";
    protected List<ScheduledFuture<?>>     scheduledFutures                   = new ArrayList<ScheduledFuture<?>>(1);

    public LogContext(){
        super();
        this.logCache = new ConcurrentHashMap<String, FinalLog>();
        this.logContextRemoteView = new LoggerContextVO(this);
        this.root = new FinalLog(Log.ROOT_LOGGER_NAME, null, this);
        this.root.setLevel(Level.DEBUG);
        logCache.put(Log.ROOT_LOGGER_NAME, root);
        initEvaluatorMap();
        size = 1;
        this.frameworkPackages = new ArrayList<String>();
    }

    void initEvaluatorMap() {
        putObject(EVALUATOR_MAP, new HashMap<String, EventEvaluator<?>>());
    }

    private void updateLoggerContextVO() {
        logContextRemoteView = new LoggerContextVO(this);
    }

    @Override
    public void putProperty(String key, String val) {
        super.putProperty(key, val);
        updateLoggerContextVO();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        updateLoggerContextVO();
    }

    public final Log getLog(final Class<?> clazz) {
        return getLog(clazz.getName());
    }

    @Override
    public final FinalLog getLog(final String name) {
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

    public FinalLog exists(String name) {
        return (FinalLog) logCache.get(name);
    }

    void fireOnLevelChange(FinalLog logger, Level level) {
        for (LogContextListener listener : logContextListenerList) {
            listener.onLevelChange(logger, level);
        }
    }

    public void noAppenderDefinedWarning(FinalLog finalLog) {
        System.err.println("log has no appender..");

    }

    public List<FinalLog> getLogList() {
        Collection<FinalLog> collection = logCache.values();
        List<FinalLog> logList = new ArrayList<FinalLog>(collection);
        Collections.sort(logList, new LogComparator());
        return logList;
    }

    public LoggerContextVO getLoggerContextRemoteView() {
        return logContextRemoteView;
    }

    public void setPackagingDataEnabled(boolean packagingDataEnabled) {
        this.packagingDataEnabled = packagingDataEnabled;
    }

    public boolean isPackagingDataEnabled() {
        return packagingDataEnabled;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {
        resetCount++;
        super.reset();
        initEvaluatorMap();
        initCollisionMaps();
        root.recursiveReset();
        cancelScheduledTasks();
        fireOnReset();
        resetListenersExceptResetResistant();
    }

    protected void initCollisionMaps() {
        putObject(FA_FILENAME_COLLISION_MAP, new HashMap<String, String>());
        putObject(RFA_FILENAME_PATTERN_COLLISION_MAP, new HashMap<String, String>());
    }

    private void cancelScheduledTasks() {
        for (ScheduledFuture<?> sf : scheduledFutures) {
            sf.cancel(false);
        }
        scheduledFutures.clear();
    }

    private void fireOnReset() {
        for (LogContextListener listener : logContextListenerList) {
            listener.onReset(this);
        }
    }

    private void resetListenersExceptResetResistant() {
        List<LogContextListener> toRetain = new ArrayList<LogContextListener>();

        for (LogContextListener lcl : logContextListenerList) {
            if (lcl.isResetResistant()) {
                toRetain.add(lcl);
            }
        }
        logContextListenerList.retainAll(toRetain);
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + getName() + "]";
    }

    public List<String> getFrameworkPackages() {
        return frameworkPackages;
    }

    public void addListener(LoggerContextListener lcl) {
        // TODO Auto-generated method stub

    }
}
