package com.bird.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * 类SubstituteLogFactory.java的实现描述
 * </p>
 * <p>
 * 替代日志工厂类，只是负责管理{@link SubstituteLog}的实例
 * </p>
 * 
 * @author dongwei.ydw 2016年4月7日 下午9:48:47
 */
public class SubstituteLogFactory implements ILogFactory {

    final ConcurrentMap<String, SubstituteLog> loggers = new ConcurrentHashMap<String, SubstituteLog>();

    @Override
    public Log getLog(String name) {
        SubstituteLog logger = loggers.get(name);
        if (logger == null) {
            logger = new SubstituteLog(name, NOPLog.NOP_LOG);
            SubstituteLog oldLogger = loggers.putIfAbsent(name, logger);
            if (oldLogger != null) logger = oldLogger;
        }
        return logger;
    }

    public List<String> getLoggerNames() {
        return new ArrayList<String>(loggers.keySet());
    }

    public List<SubstituteLog> getLoggers() {
        return new ArrayList<SubstituteLog>(loggers.values());
    }

    public void clear() {
        loggers.clear();
    }
}
