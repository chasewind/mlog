package com.bird.core.appender;

import com.bird.core.LifeCycle;
import com.bird.core.exceptions.RolloverFailure;
import com.bird.core.helpers.CompressionMode;

public interface RollingPolicy extends LifeCycle {

    void rollover() throws RolloverFailure;

    String getActiveFileName();

    CompressionMode getCompressionMode();

    void setParent(FileAppender<?> appender);
}
