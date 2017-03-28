package io.emqtt.emqandroidtoolkit.ui.activity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.event.MessageEvent;
import io.emqtt.emqandroidtoolkit.model.EmqMessage;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.adapter.MessageAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.util.LogUtil;

public class MessageListActivity extends ToolBarActivity {

    @BindView(R.id.message_list) RecyclerView mMessageRecyclerView;
    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    private MessageAdapter mAdapter;

    private Subscription mSubscription;

    private List<EmqMessage> mMessageList;

    public static void openActivity(Context context, Subscription subscription){
        Intent intent = new Intent(context, MessageListActivity.class);
        intent.putExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION, subscription);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void setUpView() {
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSubscription = getIntent().getParcelableExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION);
        setTitle(mSubscription.getTopic());


    }

    @Override
    protected void setUpData() {
        mMessageList = new ArrayList<>();
        mAdapter = new MessageAdapter(mMessageList);
        mMessageRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event){
        mAdapter.insertData(event.getMessage());
    }
}
