package com.mdx.ryan.pagerecycleview.commons;

import android.util.Log;

public class MLog {

    public static final String LOGTOOLS_LOGSHOW = "logtools_logshow";

    public final static String MLOG_TAG = "frame";

    public final static String MLOG_TAG_LOAD = "frameload";

    public final static String SYS_ERR = "system.err";

    public final static String SYS_RUN = "system.run";

    public final static String NWORK_LOAD = "network.load";

    public final static String CACHE_LOAD = "cache.load";

    public final static String FILE_LOAD = "file.load";

    public static void D(String msg) {
        D(SYS_RUN, msg);
    }

    public static void D(String showType, String msg) {
        Log.d(MLOG_TAG, msg);
    }

    public static void D(Throwable msg) {
        D(SYS_RUN, msg);
    }

    public static void D(String showType, Throwable msg) {
        msg.printStackTrace();
    }


    public static void E(String msg) {
        E(SYS_RUN, msg);
    }

    public static void E(String showType, String msg) {
        Log.e(MLOG_TAG, msg);
    }

    public static void E(Throwable msg) {
        E(SYS_RUN, msg);
    }

    public static void E(String showType, Throwable msg) {
        for (StackTraceElement ste : msg.getStackTrace()) {
            Log.e(MLOG_TAG, ste.toString());
        }
        msg.printStackTrace();
    }

    public static void I(String msg) {
        I(SYS_RUN, msg);
    }

    public static void I(String showType, String msg) {
        Log.i(MLOG_TAG, msg);
    }

    public static void p(Throwable e) {
        p(SYS_RUN, e);
    }

    public static void p(String showType, Throwable e) {
        e.printStackTrace();
    }
}
