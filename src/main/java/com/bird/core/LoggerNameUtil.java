package com.bird.core;

import java.util.ArrayList;
import java.util.List;

public class LoggerNameUtil {

    public static final char DOT    = '.';
    public static final char TAB    = '\t';
    public static final char DOLLAR = '$';

    public static int getFirstSeparatorIndexOf(String name) {
        return getSeparatorIndexOf(name, 0);
    }

    /**
     * Get the position of the separator character, if any, starting at position 'fromIndex'.
     *
     * @param name
     * @param fromIndex
     * @return
     */
    public static int getSeparatorIndexOf(String name, int fromIndex) {
        int dotIndex = name.indexOf(DOT, fromIndex);
        int dollarIndex = name.indexOf(DOLLAR, fromIndex);

        if (dotIndex == -1 && dollarIndex == -1) return -1;
        if (dotIndex == -1) return dollarIndex;
        if (dollarIndex == -1) return dotIndex;

        return dotIndex < dollarIndex ? dotIndex : dollarIndex;
    }

    public static List<String> computeNameParts(String loggerName) {
        List<String> partList = new ArrayList<String>();

        int fromIndex = 0;
        while (true) {
            int index = getSeparatorIndexOf(loggerName, fromIndex);
            if (index == -1) {
                partList.add(loggerName.substring(fromIndex));
                break;
            }
            partList.add(loggerName.substring(fromIndex, index));
            fromIndex = index + 1;
        }
        return partList;
    }
}
