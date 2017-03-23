package io.emqtt.emqandroidtoolkit.ui.activity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.adapter.ConnectionViewPagerAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.ui.fragment.PublicationFragment;
import io.emqtt.emqandroidtoolkit.ui.fragment.SubscriptionFragment;

public class ConnectionDetailActivity extends ToolBarActivity implements SubscriptionFragment.OnListFragmentInteractionListener{

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private MyHandler mHandler = new MyHandler(this);

    private Connection mConnection;
    private MqttAsyncClient mClient;

    public static void openActivity(Context context, Connection connection) {
        Intent intent = new Intent(context, ConnectionDetailActivity.class);
        intent.putExtra(Constant.EXTRA_CONNECTION, connection);
        context.startActivity(intent);


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_connection_detail;
    }

    @Override
    protected void setUpView() {
        setTitle("Connection");

    }

    @Override
    protected void setUpData() {

        mConnection = getIntent().getParcelableExtra(Constant.EXTRA_CONNECTION);
        setSubtitle("Connecting to " + mConnection.getServerURI());
        connect();

        SubscriptionFragment subscriptionFragment = SubscriptionFragment.newInstance();
        PublicationFragment publicationFragment = PublicationFragment.newInstance();

        ConnectionViewPagerAdapter viewPagerAdapter = new ConnectionViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(subscriptionFragment,SubscriptionFragment.TAG);
        viewPagerAdapter.addFragment(publicationFragment,PublicationFragment.TAG);

        mViewpager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onListFragmentInteraction(Subscription item) {

    }

    private void connect() {
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        try {
            mClient = new MqttAsyncClient(mConnection.getServerURI(), mConnection.getClintId(), mqttClientPersistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(mConnection.isCleanSession());
            mClient.connect(options, "Connect", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.CONNECT_SUCCESS;
                    mHandler.sendMessage(message);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.CONNECT_FAIL;
                    mHandler.sendMessage(message);

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.fab)
    public void onClick() {
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ConnectionDetailActivity> mActivityWeakReference;

        MyHandler(ConnectionDetailActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ConnectionDetailActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constant.MQTTStatusConstant.CONNECT_SUCCESS:
                        activity.setSubtitle("Connected to " + activity.mConnection.getServerURI());
                        break;
                    case Constant.MQTTStatusConstant.CONNECT_FAIL:
                        activity.setSubtitle("Fail");
                        break;
                }

            }
        }
    }

}
