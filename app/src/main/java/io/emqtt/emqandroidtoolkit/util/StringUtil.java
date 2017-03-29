package io.emqtt.emqandroidtoolkit.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * ClassName: StringUtil
 * Desc:
 * Created by zhiw on 2017/3/28.
 */

public class StringUtil {

    public static String formatNow() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(System.currentTimeMillis());
    }
}
