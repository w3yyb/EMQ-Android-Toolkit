package io.emqtt.emqandroidtoolkit.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.adapter.MessageAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;

public class MessageListActivity extends ToolBarActivity {

    @BindView(R.id.message_list) RecyclerView mMessageRecyclerView;
    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    private MessageAdapter mAdapter;

    private Subscription mSubscription;

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

    }


}
