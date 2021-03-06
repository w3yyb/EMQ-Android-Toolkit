package io.emqtt.emqandroidtoolkit.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.util.RealmHelper;
import io.emqtt.emqandroidtoolkit.util.TipUtil;
import io.realm.Realm;


public class ConnectionActivity extends ToolBarActivity {

    private static final String EXTRA_MODE = "mode";
    public static final String EXTRA_CONNECTION = "connection";

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;


    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;
    @BindView(R.id.host) EditText mHost;
    @BindView(R.id.port) EditText mPort;
    @BindView(R.id.client_id) EditText mClientId;
    @BindView(R.id.clean_session) Switch mCleanSession;
    @BindView(R.id.username) EditText mUsername;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.operate_connection) Button mOperateConnectionButton;

    private int mMode;

    private Connection mConnection;

    @IntDef({MODE_ADD, MODE_EDIT})
    @interface mode {

    }

    public static void openActivity(Context context, @mode int mode) {
        Intent intent = new Intent(context, ConnectionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        context.startActivity(intent);

    }

    public static void openActivityForResult(Context context, @mode int mode, int requestCode) {
        openActivityForResult(context, mode, requestCode, null);

    }

    public static void openActivityForResult(Context context, @mode int mode, int requestCode, Connection connection) {
        Intent intent = new Intent(context, ConnectionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        if (null != connection) {
            intent.putExtra(EXTRA_CONNECTION, connection);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_connection;
    }

    @Override
    protected void setUpView() {

        mMode = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD);
        if (isAddMode()) {
            mOperateConnectionButton.setText(R.string.add_connection);
            mClientId.setText(getRandomClientId());
        } else {
            mOperateConnectionButton.setText(R.string.save_connection);
            mConnection = getIntent().getParcelableExtra(EXTRA_CONNECTION);
            setConnection(mConnection);

        }


    }

    private String getRandomClientId() {
        int randNum = (int) (Math.random() * 99999);
        return "EMQ-" + randNum;
    }



    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.operate_connection)
    public void onClick() {
        if (mHost.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Host cannot be empty");
            return;
        }
        if (mClientId.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Client Id cannot be empty");
            return;
        }

        if (mPort.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Port cannot be empty");
            return;
        }


        if (!isAddMode()) {
            Realm realm = RealmHelper.getInstance().getRealm();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Connection connection = realm.where(Connection.class).equalTo("id", mConnection.getId()).findFirst();
                    mConnection = updateConnection(connection);

                }
            });
        } else {
            mConnection = new Connection();
            updateConnection(mConnection);
            RealmHelper.getInstance().addData(mConnection);
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONNECTION, mConnection);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void setConnection(Connection connection) {
        mHost.setText(connection.getHost());
        mPort.setText(connection.getPort());
        mClientId.setText(connection.getClientId());
        mCleanSession.setChecked(connection.isCleanSession());
        mUsername.setText(connection.getUsername());
        mPassword.setText(connection.getPassword());
    }

    private Connection updateConnection(Connection connection) {
        String host = mHost.getText().toString().trim();
        String port = mPort.getText().toString().trim();
        String clientId = mClientId.getText().toString().trim();
        boolean cleanSession = mCleanSession.isChecked();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        connection.setHost(host);
        connection.setPort(port);
        connection.setClientId(clientId);
        connection.setCleanSession(cleanSession);
        connection.setUsername(username);
        connection.setPassword(password);
        connection.generateId();
        return connection;
    }


    private boolean isAddMode() {
        return mMode == MODE_ADD;
    }

}
