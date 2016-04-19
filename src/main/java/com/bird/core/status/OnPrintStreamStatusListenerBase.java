package com.bird.core.status;

import java.io.PrintStream;
import java.util.List;

import com.bird.core.ContextAwareBase;
import com.bird.core.LifeCycle;

abstract class OnPrintStreamStatusListenerBase extends ContextAwareBase implements StatusListener, LifeCycle {

    boolean           isStarted             = false;

    static final long DEFAULT_RETROSPECTIVE = 300;
    long              retrospectiveThresold = DEFAULT_RETROSPECTIVE;

    /**
     * The PrintStream used by derived classes
     * 
     * @return
     */
    abstract protected PrintStream getPrintStream();

    private void print(Status status) {
        StringBuilder sb = new StringBuilder();
        StatusPrinter.buildStr(sb, "", status);
        getPrintStream().print(sb);
    }

    public void addStatusEvent(Status status) {
        if (!isStarted) return;
        print(status);
    }

    /**
     * Print status messages retrospectively
     */
    private void retrospectivePrint() {
        if (context == null) return;
        long now = System.currentTimeMillis();
        StatusManager sm = context.getStatusManager();
        List<Status> statusList = sm.getCopyOfStatusList();
        for (Status status : statusList) {
            long timestampOfStatusMesage = status.getDate();
            if (isElapsedTimeLongerThanThreshold(now, timestampOfStatusMesage)) {
                print(status);
            }
        }
    }

    private boolean isElapsedTimeLongerThanThreshold(long now, long timestamp) {
        long elapsedTime = now - timestamp;
        return elapsedTime < retrospectiveThresold;
    }

    /**
     * Invoking the start method can cause the instance to print status messages created less than value of
     * retrospectiveThresold.
     */
    public void start() {
        isStarted = true;
        if (retrospectiveThresold > 0) {
            retrospectivePrint();
        }
    }

    public void setRetrospective(long retrospective) {
        this.retrospectiveThresold = retrospective;
    }

    public long getRetrospective() {
        return retrospectiveThresold;
    }

    public void stop() {
        isStarted = false;
    }

    public boolean isStarted() {
        return isStarted;
    }

}
