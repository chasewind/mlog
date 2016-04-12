package com.bird.core.jaran;

import java.net.URL;

import com.bird.core.Context;
import com.bird.core.CoreConstants;

public class ConfigurationWatchListUtil {

    final static ConfigurationWatchListUtil origin = new ConfigurationWatchListUtil();

    private ConfigurationWatchListUtil(){
    }

    public static void registerConfigurationWatchList(Context context, ConfigurationWatchList cwl) {
        context.putObject(CoreConstants.CONFIGURATION_WATCH_LIST, cwl);
    }

    public static void setMainWatchURL(Context context, URL url) {
        ConfigurationWatchList cwl = getConfigurationWatchList(context);
        if (cwl == null) {
            cwl = new ConfigurationWatchList();
            cwl.setContext(context);
            context.putObject(CoreConstants.CONFIGURATION_WATCH_LIST, cwl);
        } else {
            cwl.clear();
        }
        // setConfigurationWatchListResetFlag(context, true);
        cwl.setMainURL(url);
    }

    public static URL getMainWatchURL(Context context) {
        ConfigurationWatchList cwl = getConfigurationWatchList(context);
        if (cwl == null) {
            return null;
        } else {
            return cwl.getMainURL();
        }
    }

    public static void addToWatchList(Context context, URL url) {
        ConfigurationWatchList cwl = getConfigurationWatchList(context);
        if (cwl == null) {
            addWarn(context, "Null ConfigurationWatchList. Cannot add " + url);
        } else {
            addInfo(context, "Adding [" + url + "] to configuration watch list.");
            cwl.addToWatchList(url);
        }
    }

    public static ConfigurationWatchList getConfigurationWatchList(Context context) {
        return (ConfigurationWatchList) context.getObject(CoreConstants.CONFIGURATION_WATCH_LIST);
    }

    static void addInfo(Context context, String msg) {
    }

    static void addWarn(Context context, String msg) {
    }
}
