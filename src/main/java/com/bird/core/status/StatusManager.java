package com.bird.core.status;

import java.util.List;

public interface StatusManager {

    /**
     * Add a new status message.
     * 
     * @param status
     */
    void add(Status status);

    /**
     * Obtain a copy of the status list maintained by this StatusManager.
     * 
     * @return
     */
    List<Status> getCopyOfStatusList();

    /**
     * Return the highest level of all the statii.
     * 
     * @return
     */
    // int getLevel();

    /**
     * Return the number of status entries.
     * 
     * @return
     */
    int getCount();

    /**
     * Add a status listener.
     * 
     * @param listener
     */

    /**
     * Add a status listener. The StatusManager may decide to skip installation if an earlier instance was already
     * installed.
     * 
     * @param listener
     * @return true if actually added, false if skipped
     */
    boolean add(StatusListener listener);

    /**
     * ); Remove a status listener.
     * 
     * @param listener
     */
    void remove(StatusListener listener);

    /**
     * Clear the list of status messages.
     */
    void clear();

    /**
     * Obtain a copy of the status listener list maintained by this StatusManager
     * 
     * @return
     */
    List<StatusListener> getCopyOfStatusListenerList();

}
