package io.emqtt.emqandroidtoolkit.ui;

/**
 * ClassName: OnItemClickListener
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public interface OnItemClickListener {

    void onItemEdit(int position, Object item);

    void onItemDelete(int position, Object item);
}
