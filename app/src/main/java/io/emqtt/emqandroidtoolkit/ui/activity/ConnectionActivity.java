package io.emqtt.emqandroidtoolkit.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.ui.base.BaseActivity;


public class ConnectionActivity extends BaseActivity {

    private static final String EXTRA_MODE = "mode";

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;


    @BindView(R.id.host)
    EditText mHost;
    @BindView(R.id.port)
    EditText mPort;
    @BindView(R.id.client_id)
    EditText mClientId;
    @BindView(R.id.clean_session)
    Switch mCleanSession;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.operate_connection)
    Button mOperateConnectionButton;

    @IntDef({MODE_ADD, MODE_EDIT})
    public @interface mode {

    }

    public static void openActivity(Context context, @mode int mode) {
        Intent intent = new Intent(context, ConnectionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        context.startActivity(intent);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_connection;
    }

    @Override
    protected void setUpView() {
        int mode = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD);
        if (isAddMode(mode)) {
            mOperateConnectionButton.setText(R.string.add_connection);
        } else {
            mOperateConnectionButton.setText(R.string.save_connection);
        }


    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.operate_connection)
    public void onClick() {
    }

    private boolean isAddMode(int mode) {
        return mode == MODE_ADD;
    }

}
