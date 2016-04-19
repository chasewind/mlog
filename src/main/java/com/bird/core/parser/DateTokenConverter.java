package com.bird.core.parser;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.bird.core.CachingDateFormatter;
import com.bird.core.CoreConstants;
import com.bird.core.helpers.DatePatternToRegexUtil;

public class DateTokenConverter<E> extends DynamicConverter<E> implements MonoTypedConverter {

    /**
     * The conversion word/character with which this converter is registered.
     */
    public final static String   CONVERTER_KEY        = "d";
    public final static String   AUXILIARY_TOKEN      = "AUX";
    public static final String   DEFAULT_DATE_PATTERN = CoreConstants.DAILY_DATE_PATTERN;

    private String               datePattern;
    private TimeZone             timeZone;
    private CachingDateFormatter cdf;
    // is this token converter primary or auxiliary? Only the primary converter
    // determines the rolling period
    private boolean              primary              = true;

    public void start() {
        this.datePattern = getFirstOption();
        if (this.datePattern == null) {
            this.datePattern = DEFAULT_DATE_PATTERN;
        }

        final List<String> optionList = getOptionList();
        if (optionList != null) {
            for (int optionIndex = 1; optionIndex < optionList.size(); optionIndex++) {
                String option = optionList.get(optionIndex);
                if (AUXILIARY_TOKEN.equalsIgnoreCase(option)) {
                    primary = false;
                } else {
                    timeZone = TimeZone.getTimeZone(option);
                }
            }
        }

        cdf = new CachingDateFormatter(datePattern);
        if (timeZone != null) {
            cdf.setTimeZone(timeZone);
        }
    }

    public String convert(Date date) {
        return cdf.format(date.getTime());
    }

    public String convert(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Null argument forbidden");
        }
        if (o instanceof Date) {
            return convert((Date) o);
        }
        throw new IllegalArgumentException("Cannot convert " + o + " of type" + o.getClass().getName());
    }

    /**
     * Return the date pattern.
     */
    public String getDatePattern() {
        return datePattern;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public boolean isApplicable(Object o) {
        return (o instanceof Date);
    }

    public String toRegex() {
        DatePatternToRegexUtil datePatternToRegexUtil = new DatePatternToRegexUtil(datePattern);
        return datePatternToRegexUtil.toRegex();
    }

    public boolean isPrimary() {
        return primary;
    }
}
