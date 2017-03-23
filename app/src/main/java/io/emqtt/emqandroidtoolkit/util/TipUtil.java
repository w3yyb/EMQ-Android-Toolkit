package io.emqtt.emqandroidtoolkit.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * ClassName: TipUtil
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public class TipUtil {

    public static void showSnackbar(View view, String text, String actionText, View.OnClickListener listener) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).setAction(actionText, listener).show();
    }

    public static void showSnackbar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

}
