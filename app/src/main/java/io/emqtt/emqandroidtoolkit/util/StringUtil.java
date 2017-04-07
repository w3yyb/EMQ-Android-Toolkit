package io.emqtt.emqandroidtoolkit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static long getTimeStamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        long timeStamp = 0;

        try {
            date = format.parse(time);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeStamp;

    }
}
