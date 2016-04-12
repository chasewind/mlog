package com.bird.core;

import java.util.Comparator;

public class LogComparator implements Comparator<FinalLog> {

    public int compare(FinalLog l1, FinalLog l2) {
        if (l1.getName().equals(l2.getName())) {
            return 0;
        }
        if (l1.getName().equals(FinalLog.ROOT_LOGGER_NAME)) {
            return -1;
        }
        if (l2.getName().equals(FinalLog.ROOT_LOGGER_NAME)) {
            return 1;
        }
        return l1.getName().compareTo(l2.getName());
    }

}
