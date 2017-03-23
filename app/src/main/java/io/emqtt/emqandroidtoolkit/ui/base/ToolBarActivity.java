package io.emqtt.emqandroidtoolkit.ui.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import io.emqtt.emqandroidtoolkit.R;

/**
 * ClassName: ToolBarActivity
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public abstract class ToolBarActivity extends BaseActivity {

    protected @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();

    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    private void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
            mToolbar.setContentInsetStartWithNavigation(0);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }
    }

    protected void setSubtitle(CharSequence subtitle) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(subtitle);
        }
    }

}
