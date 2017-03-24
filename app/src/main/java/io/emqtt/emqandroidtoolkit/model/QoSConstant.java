package io.emqtt.emqandroidtoolkit.model;

import android.support.annotation.IntDef;



/**
 * ClassName: QoSConstant
 * Desc:
 * Created by zhiw on 2017/3/24.
 */

public class QoSConstant {

    public static final int AT_MOST_ONCE = 0;

    public static final int AT_LEAST_ONCE = 1;

    public static final int EXACTLY_ONCE = 2;

    @IntDef({AT_MOST_ONCE,AT_LEAST_ONCE,EXACTLY_ONCE})
    public @interface QoS{

    }
}
