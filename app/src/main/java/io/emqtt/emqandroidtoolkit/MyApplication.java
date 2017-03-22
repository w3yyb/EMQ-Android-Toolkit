package io.emqtt.emqandroidtoolkit;

import android.app.Application;
import android.content.Context;

/**
 * ClassName: MyApplication
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

}
