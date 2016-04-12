package com.bird.core.layout;

import com.bird.core.CachingDateFormatter;
import com.bird.core.CoreConstants;
import com.bird.core.ILoggingEvent;
import com.bird.core.IThrowableProxy;
import com.bird.core.ThrowableProxyConverter;

public class TTLLLayout extends LayoutBase<ILoggingEvent> {

    CachingDateFormatter    cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss.SSS");
    ThrowableProxyConverter tpc                  = new ThrowableProxyConverter();

    @Override
    public void start() {
        tpc.start();
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        if (!isStarted()) {
            return CoreConstants.EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();

        long timestamp = event.getTimeStamp();

        sb.append(cachingDateFormatter.format(timestamp));
        sb.append(" [");
        sb.append(event.getThreadName());
        sb.append("] ");
        sb.append(event.getLevel().toString());
        sb.append(" ");
        sb.append(event.getLogName());
        sb.append(" - ");
        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            String stackTrace = tpc.convert(event);
            sb.append(stackTrace);
        }
        return sb.toString();
    }

}
