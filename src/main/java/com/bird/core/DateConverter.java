package com.bird.core;

import java.util.List;
import java.util.TimeZone;

public class DateConverter extends ClassicConverter {

    long                       lastTimestamp        = -1;
    String                     timestampStrCache    = null;
    CachingDateFormatter       cachingDateFormatter = null;
    public static final String ISO8601_PATTERN      = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final String DAILY_DATE_PATTERN   = "yyyy-MM-dd";
    public static final String ISO8601_STR          = "ISO8601";

    public void start() {

        String datePattern = getFirstOption();
        if (datePattern == null) {
            datePattern = ISO8601_PATTERN;
        }

        if (datePattern.equals(ISO8601_STR)) {
            datePattern = ISO8601_PATTERN;
        }

        try {
            cachingDateFormatter = new CachingDateFormatter(datePattern);
        } catch (IllegalArgumentException e) {
            System.err.println("Could not instantiate SimpleDateFormat with pattern " + datePattern);
            cachingDateFormatter = new CachingDateFormatter(ISO8601_PATTERN);
        }

        List optionList = getOptionList();

        // if the option list contains a TZ option, then set it.
        if (optionList != null && optionList.size() > 1) {
            TimeZone tz = TimeZone.getTimeZone((String) optionList.get(1));
            cachingDateFormatter.setTimeZone(tz);
        }
    }

    public String convert(ILoggingEvent le) {
        long timestamp = le.getTimeStamp();
        return cachingDateFormatter.format(timestamp);
    }

}
