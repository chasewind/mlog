package com.bird.core.appender;

import com.bird.core.LifeCycle;
import com.bird.core.exceptions.RolloverFailureException;
import com.bird.core.helpers.CompressionMode;

public interface RollingPolicy extends LifeCycle {

    void rollover() throws RolloverFailureException;

    String getActiveFileName();

    CompressionMode getCompressionMode();

    void setParent(FileAppender<?> appender);
}
