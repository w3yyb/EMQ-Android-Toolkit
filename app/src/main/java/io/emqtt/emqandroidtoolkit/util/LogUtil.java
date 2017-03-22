package io.emqtt.emqandroidtoolkit.util;

import android.util.Log;

/**
 * ClassName: LogUtil
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class LogUtil {

    private static final String TAG = "EMQ";
    private static final boolean OPEN = true;

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (OPEN) {
            Log.e(tag, msg);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (OPEN) {
            Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (OPEN) {
            Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (OPEN) {
            Log.w(tag, msg);
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (OPEN) {
            Log.e(tag, msg);
        }
    }
}
