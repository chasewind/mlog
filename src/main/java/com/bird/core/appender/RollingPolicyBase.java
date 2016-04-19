package com.bird.core.appender;

import com.bird.core.ContextAwareBase;
import com.bird.core.helpers.CompressionMode;
import com.bird.core.helpers.FileNamePattern;

public abstract class RollingPolicyBase extends ContextAwareBase implements RollingPolicy {

    protected CompressionMode compressionMode = CompressionMode.NONE;

    FileNamePattern           fileNamePattern;
    // fileNamePatternStr is always slashified, see setter
    protected String          fileNamePatternStr;

    private FileAppender<?>   parent;

    // use to name files within zip file, i.e. the zipEntry
    FileNamePattern           zipEntryFileNamePattern;
    private boolean           started;

    /**
     * Given the FileNamePattern string, this method determines the compression mode depending on last letters of the
     * fileNamePatternStr. Patterns ending with .gz imply GZIP compression, endings with '.zip' imply ZIP compression.
     * Otherwise and by default, there is no compression.
     */
    protected void determineCompressionMode() {
        if (fileNamePatternStr.endsWith(".gz")) {
            addInfo("Will use gz compression");
            compressionMode = CompressionMode.GZ;
        } else if (fileNamePatternStr.endsWith(".zip")) {
            addInfo("Will use zip compression");
            compressionMode = CompressionMode.ZIP;
        } else {
            addInfo("No compression will be used");
            compressionMode = CompressionMode.NONE;
        }
    }

    public void setFileNamePattern(String fnp) {
        fileNamePatternStr = fnp;
    }

    public String getFileNamePattern() {
        return fileNamePatternStr;
    }

    public CompressionMode getCompressionMode() {
        return compressionMode;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    public void setParent(FileAppender<?> appender) {
        this.parent = appender;
    }

    public boolean isParentPrudent() {
        return parent.isPrudent();
    }

    public String getParentsRawFileProperty() {
        return parent.rawFileProperty();
    }
}
