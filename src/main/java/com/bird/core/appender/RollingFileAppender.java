package com.bird.core.appender;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.bird.core.CoreConstants;
import com.bird.core.exceptions.RolloverFailureException;
import com.bird.core.helpers.CompressionMode;
import com.bird.core.helpers.FileNamePattern;

/**
 * 类RollingFileAppender.java的实现描述：TODO 类实现描述
 * 
 * @author dongwei.ydw 2016年4月19日 下午7:53:24
 */
public class RollingFileAppender<E> extends FileAppender<E> {

    File                  currentlyActiveFile;
    TriggeringPolicy<E>   triggeringPolicy;
    RollingPolicy         rollingPolicy;

    static private String RFA_NO_TP_URL     = CoreConstants.CODES_URL + "#rfa_no_tp";
    static private String RFA_NO_RP_URL     = CoreConstants.CODES_URL + "#rfa_no_rp";
    static private String COLLISION_URL     = CoreConstants.CODES_URL + "#rfa_collision";
    static private String RFA_LATE_FILE_URL = CoreConstants.CODES_URL + "#rfa_file_after";

    public void start() {
        if (triggeringPolicy == null) {
            addError("No TriggeringPolicy was set for the RollingFileAppender named " + getName());
            addError(CoreConstants.MORE_INFO_PREFIX + RFA_NO_TP_URL);
            return;
        }
        if (!triggeringPolicy.isStarted()) {
            addError("TriggeringPolicy has not started. RollingFileAppender will not start");
            return;
        }

        if (checkForCollisionsInPreviousRollingFileAppenders()) {
            addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
            addError(CoreConstants.MORE_INFO_PREFIX + COLLISION_WITH_EARLIER_APPENDER_URL);
            return;
        }

        // we don't want to void existing log files
        if (!append) {
            addError("Append mode is mandatory for RollingFileAppender. Defaulting to append=true.");
            append = true;
        }

        if (rollingPolicy == null) {
            addError("No RollingPolicy was set for the RollingFileAppender named " + getName());
            addError(CoreConstants.MORE_INFO_PREFIX + RFA_NO_RP_URL);
            return;
        }

        // sanity check for http://jira.qos.ch/browse/LOGBACK-796
        if (checkForFileAndPatternCollisions()) {
            addError("File property collides with fileNamePattern. Aborting.");
            addError(CoreConstants.MORE_INFO_PREFIX + COLLISION_URL);
            return;
        }

        if (isPrudent()) {
            if (rawFileProperty() != null) {
                addError("Setting \"File\" property to null on account of prudent mode");
                setFile(null);
            }
            if (rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
                addError("Compression is not supported in prudent mode. Aborting");
                return;
            }
        }

        currentlyActiveFile = new File(getFile());
        addInfo("Active log file name: " + getFile());
        super.start();
    }

    private boolean checkForFileAndPatternCollisions() {
        if (triggeringPolicy instanceof RollingPolicyBase) {
            final RollingPolicyBase base = (RollingPolicyBase) triggeringPolicy;
            final FileNamePattern fileNamePattern = base.fileNamePattern;
            // no use checking if either fileName or fileNamePattern are null
            if (fileNamePattern != null && fileName != null) {
                String regex = fileNamePattern.toRegex();
                return fileName.matches(regex);
            }
        }
        return false;
    }

    private boolean checkForCollisionsInPreviousRollingFileAppenders() {
        boolean collisionResult = false;
        if (triggeringPolicy instanceof RollingPolicyBase) {
            final RollingPolicyBase base = (RollingPolicyBase) triggeringPolicy;
            final FileNamePattern fileNamePattern = base.fileNamePattern;
            boolean collisionsDetected = innerCheckForFileNamePatternCollisionInPreviousRFA(fileNamePattern.toString());
            if (collisionsDetected) collisionResult = true;
        }
        return collisionResult;
    }

    private boolean innerCheckForFileNamePatternCollisionInPreviousRFA(String fileNamePattern) {
        boolean collisionsDetected = false;
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) context.getObject(CoreConstants.FA_FILENAME_COLLISION_MAP);
        if (map == null) {
            return collisionsDetected;
        }
        for (Entry<String, String> entry : map.entrySet()) {
            if (fileNamePattern.equals(entry.getValue())) {
                addErrorForCollision("FileNamePattern", entry.getValue(), entry.getKey());
                collisionsDetected = true;
            }
        }
        if (name != null) {
            map.put(getName(), fileNamePattern);
        }
        return collisionsDetected;
    }

    @Override
    public void stop() {
        if (rollingPolicy != null) rollingPolicy.stop();
        if (triggeringPolicy != null) triggeringPolicy.stop();
        super.stop();
    }

    @Override
    public void setFile(String file) {
        if (file != null && ((triggeringPolicy != null) || (rollingPolicy != null))) {
            addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
            addError(CoreConstants.MORE_INFO_PREFIX + RFA_LATE_FILE_URL);
        }
        super.setFile(file);
    }

    @Override
    public String getFile() {
        return rollingPolicy.getActiveFileName();
    }

    /**
     * Implemented by delegating most of the rollover work to a rolling policy.
     */
    public void rollover() {
        lock.lock();
        try {
            this.closeOutputStream();
            attemptRollover();
            attemptOpenFile();
        } finally {
            lock.unlock();
        }
    }

    private void attemptOpenFile() {
        try {
            currentlyActiveFile = new File(rollingPolicy.getActiveFileName());

            this.openFile(rollingPolicy.getActiveFileName());
        } catch (IOException e) {
            addError("setFile(" + fileName + ", false) call failed.", e);
        }
    }

    private void attemptRollover() {
        try {
            rollingPolicy.rollover();
        } catch (RolloverFailureException rf) {
            addError("RolloverFailure occurred. Deferring roll-over.");
            this.append = true;
        }
    }

    /**
     * This method differentiates RollingFileAppender from its super class.
     */
    @Override
    protected void subAppend(E event) {

        synchronized (triggeringPolicy) {
            if (triggeringPolicy.isTriggeringEvent(currentlyActiveFile, event)) {
                rollover();
            }
        }

        super.subAppend(event);
    }

    public RollingPolicy getRollingPolicy() {
        return rollingPolicy;
    }

    public TriggeringPolicy<E> getTriggeringPolicy() {
        return triggeringPolicy;
    }

    @SuppressWarnings("unchecked")
    public void setRollingPolicy(RollingPolicy policy) {
        rollingPolicy = policy;
        if (rollingPolicy instanceof TriggeringPolicy) {
            triggeringPolicy = (TriggeringPolicy<E>) policy;
        }

    }

    public void setTriggeringPolicy(TriggeringPolicy<E> policy) {
        triggeringPolicy = policy;
        if (policy instanceof RollingPolicy) {
            rollingPolicy = (RollingPolicy) policy;
        }
    }
}
