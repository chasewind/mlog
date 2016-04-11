package com.bird.core;

public class RelativeTimeConverter extends ClassicConverter {

    long   lastTimestamp = -1;
    String timesmapCache = null;

    public String convert(ILoggingEvent event) {
        long now = event.getTimeStamp();

        synchronized (this) {
            // update timesmapStrCache only if now != lastTimestamp
            if (now != lastTimestamp) {
                lastTimestamp = now;
                timesmapCache = Long.toString(now - event.getLogContextVO().getBirthTime());
            }
            return timesmapCache;
        }
    }
}
