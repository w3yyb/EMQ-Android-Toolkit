package io.emqtt.emqandroidtoolkit.ui.activity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.model.EmqMessage;
import io.emqtt.emqandroidtoolkit.model.Publication;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.adapter.ConnectionViewPagerAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.ui.fragment.PublicationListFragment;
import io.emqtt.emqandroidtoolkit.ui.fragment.SubscriptionListFragment;
import io.emqtt.emqandroidtoolkit.util.LogUtil;
import io.emqtt.emqandroidtoolkit.util.TipUtil;

public class ConnectionDetailActivity extends ToolBarActivity implements SubscriptionListFragment.OnListFragmentInteractionListener, MqttCallback {

    private static final int SUBSCRIPTION = 63;

    private static final int PUBLICATION = 64;

    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private MyHandler mHandler = new MyHandler(this);

    private Connection mConnection;
    private Subscription mSubscription;
    private Publication mPublication;

    private MqttAsyncClient mClient;
    private ConnectionViewPagerAdapter mAdapter;

    private int mCurrentMode = SUBSCRIPTION;

    public static void openActivity(Context context, Connection connection) {
        Intent intent = new Intent(context, ConnectionDetailActivity.class);
        intent.putExtra(Constant.ExtraConstant.EXTRA_CONNECTION, connection);
        context.startActivity(intent);


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_connection_detail;
    }

    @Override
    protected void setUpView() {
        mConnection = getIntent().getParcelableExtra(Constant.ExtraConstant.EXTRA_CONNECTION);
        setTitle(mConnection.getServerURI());
        setSubtitle(getString(R.string.connecting) );

    }

    @Override
    protected void setUpData() {

        initClient();

        connect();

        SubscriptionListFragment subscriptionListFragment = SubscriptionListFragment.newInstance();
        PublicationListFragment publicationListFragment = PublicationListFragment.newInstance();

        mAdapter = new ConnectionViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(subscriptionListFragment, SubscriptionListFragment.TAG);
        mAdapter.addFragment(publicationListFragment, PublicationListFragment.TAG);

        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter.getItem(position) instanceof SubscriptionListFragment) {
                    mCurrentMode = SUBSCRIPTION;
                } else if (mAdapter.getItem(position) instanceof PublicationListFragment) {
                    mCurrentMode = PUBLICATION;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(Subscription item) {
        MessageListActivity.openActivity(this,item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SUBSCRIPTION) {
                Subscription subscription = data.getParcelableExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION);
                subscribe(subscription);
                mSubscription = subscription;
//                SubscriptionListFragment subscriptionListFragment = (SubscriptionListFragment) mAdapter.getItem(0);
//                subscriptionListFragment.addData(subscription);

            }
            if (requestCode == PUBLICATION) {
                Publication publication = data.getParcelableExtra(Constant.ExtraConstant.EXTRA_PUBLICATION);
                publish(publication);
                mPublication = publication;

            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connection_detail,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isConnected()){
            menu.findItem(R.id.action_disconnect).setVisible(true);
            menu.findItem(R.id.action_connect).setVisible(false);
        }else {
            menu.findItem(R.id.action_disconnect).setVisible(false);
            menu.findItem(R.id.action_connect).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_connect) {
            connect();
            return true;
        }
        if (id == R.id.action_disconnect) {
            disconnect();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.fab)
    public void onClick() {
        if (!isConnected()){
            TipUtil.showSnackbar(mCoordinatorLayout, getString(R.string.hint_disconnect), getString(R.string.connect), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connect();
                }
            });
            return;
        }

        if (mCurrentMode == SUBSCRIPTION) {
            startActivityForResult(SubscriptionActivity.class, SUBSCRIPTION);
//            SubscriptionFragment subscriptionFragment=new SubscriptionFragment();
//            subscriptionFragment.show(getFragmentManager(),"SubscriptionFragment");
        }else if (mCurrentMode == PUBLICATION){
            startActivityForResult(PublicationActivity.class, PUBLICATION);
        }


    }


    @Override
    public void connectionLost(Throwable cause) {
        LogUtil.e("connectionLost");
        Message msg = mHandler.obtainMessage();
        msg.what = Constant.MQTTStatusConstant.CONNECTION_LOST;
        mHandler.sendMessage(msg);

    }


    @Override
    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
        LogUtil.d("topic is " + topic + "\tmessage is " + message.toString());
        Message msg = mHandler.obtainMessage();
        msg.what = Constant.MQTTStatusConstant.MESSAGE_ARRIVED;
        msg.obj = new EmqMessage(topic, message);
        mHandler.sendMessage(msg);


    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LogUtil.d("deliveryComplete");

    }


    private void initClient(){
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        try {
            mClient = new MqttAsyncClient(mConnection.getServerURI(), mConnection.getClintId(), mqttClientPersistence);
            mClient.setCallback(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    private void connect() {
        try {
            setSubtitle(getString(R.string.connecting));
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

    private void subscribe(Subscription subscription){
        try {
            mClient.subscribe(subscription.getTopic(), subscription.getQoS(), "Subscribe", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.SUBSCRIBE_SUCCESS;
                    mHandler.sendMessage(message);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.SUBSCRIBE_FAIL;
                    mHandler.sendMessage(message);

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publish(Publication publication){

        try {
            mClient.publish(publication.getTopic(), publication.getMessage(), "Publish", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.PUBLISH_SUCCESS;
                    message.obj = asyncActionToken;
                    mHandler.sendMessage(message);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constant.MQTTStatusConstant.PUBLISH_FAIL;
                    message.obj = asyncActionToken;
                    mHandler.sendMessage(message);

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void disconnect(){
        try {
            mClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        updateToolBar();
    }


    private boolean isConnected() {
        return mClient != null && mClient.isConnected();
    }

    private void updateToolBar(){
        if (isConnected()){

        }else {
            setSubtitle(getString(R.string.disconnect));
            invalidateOptionsMenu();
        }
    }








    private static class MyHandler extends Handler {
        private final WeakReference<ConnectionDetailActivity> mActivityWeakReference;

        MyHandler(ConnectionDetailActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ConnectionDetailActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constant.MQTTStatusConstant.CONNECT_SUCCESS:
                        activity.setSubtitle(activity.getString(R.string.connected));
                        activity.invalidateOptionsMenu();
                        break;

                    case Constant.MQTTStatusConstant.CONNECT_FAIL:
                        activity.setSubtitle(activity.getString(R.string.connect_fail));
                        break;

                    case Constant.MQTTStatusConstant.SUBSCRIBE_SUCCESS:
                        SubscriptionListFragment subscriptionListFragment = (SubscriptionListFragment) activity.mAdapter.getItem(0);
                        subscriptionListFragment.addData(activity.mSubscription);
                        break;

                    case Constant.MQTTStatusConstant.SUBSCRIBE_FAIL:
                        break;

                    case Constant.MQTTStatusConstant.PUBLISH_SUCCESS:
                        PublicationListFragment publicationListFragment=(PublicationListFragment)activity.mAdapter.getItem(1);
                        publicationListFragment.insertData(activity.mPublication);
                        break;

                    case Constant.MQTTStatusConstant.PUBLISH_FAIL:
                        break;

                    case Constant.MQTTStatusConstant.MESSAGE_ARRIVED:
                        EmqMessage emqMessage = (EmqMessage) msg.obj;
                        SubscriptionListFragment subscriptionListFragment1 = (SubscriptionListFragment) activity.mAdapter.getItem(0);
                        subscriptionListFragment1.updateData(emqMessage);

                        TipUtil.showSnackbar(activity.mCoordinatorLayout, "New message arrived", "View", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                        break;

                    case Constant.MQTTStatusConstant.CONNECTION_LOST:
                        activity.setSubtitle(activity.getString(R.string.disconnect));
                        activity.invalidateOptionsMenu();
                        break;

                    case Constant.MQTTStatusConstant.DELIVERY_COMPLETE:
                        break;

                }

            }
        }
    }

}
