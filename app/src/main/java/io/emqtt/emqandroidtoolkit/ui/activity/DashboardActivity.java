package io.emqtt.emqandroidtoolkit.ui.activity;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.event.MQTTActionEvent;
import io.emqtt.emqandroidtoolkit.event.MessageEvent;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.model.Publication;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.net.MQTTManager;
import io.emqtt.emqandroidtoolkit.ui.adapter.ConnectionViewPagerAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.BaseActivity;
import io.emqtt.emqandroidtoolkit.ui.fragment.PublicationListFragment;
import io.emqtt.emqandroidtoolkit.ui.fragment.SubscriptionListFragment;
import io.emqtt.emqandroidtoolkit.util.RealmHelper;
import io.emqtt.emqandroidtoolkit.util.TipUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class DashboardActivity extends BaseActivity implements SubscriptionListFragment.OnListFragmentInteractionListener {

    private static final int SUBSCRIPTION = 63;

    private static final int PUBLICATION = 64;

    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private Connection mConnection;
    private Subscription mSubscription;
    private Publication mPublication;

    private MQTTManager mMQTTManager;
    private MqttAsyncClient mClient;
    private ConnectionViewPagerAdapter mAdapter;

    private RealmResults<Subscription> mSubscriptionResults;
    private List<Subscription> mSubscriptionList;

    private int mCurrentMode = SUBSCRIPTION;

    public static void openActivity(Context context, Connection connection) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra(Constant.ExtraConstant.EXTRA_CONNECTION, connection);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void setUpView() {
        mConnection = getIntent().getParcelableExtra(Constant.ExtraConstant.EXTRA_CONNECTION);

        mToolbar.setTitle(mConnection.getServerURI());
        setSupportActionBar(mToolbar);
        setSubtitle(getString(R.string.connecting) );
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();

            }
        });
    }

    private void displayDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_connection_status, null);
        ((TextView)view.findViewById(R.id.server)).setText(mConnection.getServerURI());
        ((TextView)view.findViewById(R.id.client_id)).setText(mConnection.getClientId());
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void setUpData() {

        EventBus.getDefault().register(this);

        getSubscription();

        setUpTabLayout();

        initClient();

    }




    @Override
    public void onItemClick(int position, Subscription item) {
        MessageListActivity.openActivity(this, item);

    }

    @Override
    public void onItemDelete(int position, Subscription item) {
        RealmHelper.getInstance().delete(mSubscriptionResults.get(position));

    }

    @Override
    public void onItemSubscribe(int position, Subscription item) {
        subscribe(item);
    }

    @Override
    public void onItemUnsubscribe(int position, Subscription item) {
        unsubscribe(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == SUBSCRIPTION) {
                Subscription subscription = data.getParcelableExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION);
                subscription.setClientId(mConnection.getClientId());
                mSubscription = subscription;
                RealmHelper.getInstance().addSubscription(mSubscription);
                subscribe(subscription);

            }
            if (requestCode == PUBLICATION) {
                Publication publication = data.getParcelableExtra(Constant.ExtraConstant.EXTRA_PUBLICATION);
                mPublication = publication;
                publish(publication);

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
//            SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
//            subscriptionFragment.show(getFragmentManager(), "SubscriptionFragment");
        }else if (mCurrentMode == PUBLICATION){
            startActivityForResult(PublicationActivity.class, PUBLICATION);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MQTTActionEvent event){

        switch (event.getStatus()) {
            case Constant.MQTTStatusConstant.CONNECT_SUCCESS:
                setSubtitle(getString(R.string.connected));
                invalidateOptionsMenu();

                for (Subscription subscription : mSubscriptionList) {
                    subscribe(subscription);
                }
                break;

            case Constant.MQTTStatusConstant.CONNECT_FAIL:
                setSubtitle(getString(R.string.connect_fail));
                break;

            case Constant.MQTTStatusConstant.SUBSCRIBE_SUCCESS:
                TipUtil.showSnackbar(mCoordinatorLayout,getString(R.string.subscribe_success));
                SubscriptionListFragment subscriptionListFragment = (SubscriptionListFragment) mAdapter.getItem(0);
                subscriptionListFragment.addData(mSubscription);

                break;

            case Constant.MQTTStatusConstant.SUBSCRIBE_FAIL:
                TipUtil.showSnackbar(mCoordinatorLayout,getString(R.string.subscribe_fail));
                break;

            case Constant.MQTTStatusConstant.UNSUBSCRIBE_SUCCESS:
                TipUtil.showSnackbar(mCoordinatorLayout,getString(R.string.unsubscurbe_success));
                break;

            case Constant.MQTTStatusConstant.UNSUBSCRIBE_FAIL:
                TipUtil.showSnackbar(mCoordinatorLayout,getString(R.string.unsubscirbe_fail));

                break;

            case Constant.MQTTStatusConstant.PUBLISH_SUCCESS:
                PublicationListFragment fragment = (PublicationListFragment) mAdapter.getItem(1);
                fragment.insertData(mPublication);
                break;

            case Constant.MQTTStatusConstant.PUBLISH_FAIL:
                TipUtil.showSnackbar(mCoordinatorLayout,getString(R.string.publish_fail));
                break;

            case Constant.MQTTStatusConstant.CONNECTION_LOST:
                setSubtitle(getString(R.string.disconnect));
                invalidateOptionsMenu();
                break;
            default:
                break;

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event){
        SubscriptionListFragment subscriptionListFragment = (SubscriptionListFragment) mAdapter.getItem(0);
        subscriptionListFragment.updateData(event.getMessage());
        RealmHelper.getInstance().addData(event.getMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        MessageEvent event = EventBus.getDefault().getStickyEvent(MessageEvent.class);
        if (event != null) {
            SubscriptionListFragment subscriptionListFragment = (SubscriptionListFragment) mAdapter.getItem(0);
            subscriptionListFragment.updateData(event.getMessage());
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }




    private void setSubtitle(String subtitle) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(subtitle);
        }
    }


    private void getSubscription() {
        mSubscriptionList = new ArrayList<>();
        Realm realm = RealmHelper.getInstance().getRealm();
        realm.beginTransaction();
        mSubscriptionResults = realm.where(Subscription.class).equalTo("clientId", mConnection.getClientId()).findAll();
        realm.commitTransaction();

        mSubscriptionList.addAll(mSubscriptionResults);


    }


    private void setUpTabLayout() {
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

    private void initClient() {
        mMQTTManager = MQTTManager.getInstance();
        mClient = mMQTTManager.getClient(mConnection.toString());
        if (mClient == null) {
            mClient = mMQTTManager.createClient(mConnection.toString(), mConnection.getServerURI(), mConnection.getClientId());
        }
        if (mMQTTManager.isConnected(mClient)) {
            setSubtitle(getString(R.string.connected));
        } else {
            connect();
        }

    }


    private void connect() {
        setSubtitle(getString(R.string.connecting));
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(mConnection.isCleanSession());
        mMQTTManager.connect(mClient,options);
    }

    private void subscribe(Subscription subscription) {
        mSubscription = subscription;
        mMQTTManager.subscribe(mClient,subscription.getTopic(), subscription.getQoS());
    }

    private void unsubscribe(Subscription subscription){
        mMQTTManager.unsubscribe(mClient,subscription.getTopic());
    }

    private void publish(Publication publication){
        mMQTTManager.publish(mClient,publication.getTopic(),publication.getMessage());
    }

    private void disconnect(){
        if (mMQTTManager.disconnect(mClient)){
            finish();
        }
    }


    private boolean isConnected() {
        return mMQTTManager.isConnected(mClient);
    }

    public List<Subscription> getSubscriptionList() {
        return mSubscriptionList;
    }


}
