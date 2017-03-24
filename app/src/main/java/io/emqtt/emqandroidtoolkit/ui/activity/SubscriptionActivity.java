package io.emqtt.emqandroidtoolkit.ui.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;

public class SubscriptionActivity extends ToolBarActivity {


    @BindView(R.id.topic) EditText mTopic;
    @BindView(R.id.display_name) EditText mDisplayName;
    @BindView(R.id.btn_subscribe) Button mSubscribeBtn;
    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;
    @BindView(R.id.qos0) RadioButton mQos0;
    @BindView(R.id.qos1) RadioButton mQos1;
    @BindView(R.id.qos2) RadioButton mQos2;
    @BindView(R.id.radio_group_qos) RadioGroup mRadioGroupQos;



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_subscription;
    }

    @Override
    protected void setUpView() {
        setTitle("Subscription");
        mQos1.setChecked(true);
    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.btn_subscribe)
    public void onViewClicked() {
        String topic = mTopic.getText().toString().trim();
        int qos = 1;
        int checkedId = mRadioGroupQos.getCheckedRadioButtonId();
        if (checkedId == R.id.qos0) {
            qos = 0;
        } else if (checkedId == R.id.qos1) {
            qos = 1;
        } else if (checkedId == R.id.qos2) {
            qos = 2;
        }

        Subscription subscription = new Subscription(topic, qos);
        String displayName = mDisplayName.getText().toString().trim();
        if (displayName.isEmpty()) {
            displayName = topic;
        }
        subscription.setDisplayName(displayName);

        Intent intent = new Intent();
        intent.putExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION, subscription);
        setResult(RESULT_OK, intent);
        finish();

    }




}
