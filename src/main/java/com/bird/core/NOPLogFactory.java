package com.bird.core;

public class NOPLogFactory implements ILogFactory {

    public NOPLogFactory(){
        // nothing to do
    }

    public Log getLog(String name) {
        return NOPLog.NOP_LOG;
    }

}
