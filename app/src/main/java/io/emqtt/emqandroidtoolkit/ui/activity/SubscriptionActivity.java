package io.emqtt.emqandroidtoolkit.ui.activity;

import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.ui.widget.QoSChooseLayout;
import io.emqtt.emqandroidtoolkit.util.TipUtil;

public class SubscriptionActivity extends ToolBarActivity {


    @BindView(R.id.topic) EditText mTopic;
    @BindView(R.id.btn_subscribe) Button mSubscribeBtn;
    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;
    @BindView(R.id.qos) QoSChooseLayout mQoSLayout;




    @Override
    protected int getLayoutResId() {
        return R.layout.activity_subscription;
    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.btn_subscribe)
    public void onViewClicked() {
        String topic = mTopic.getText().toString().trim();
        try {
            MqttTopic.validate(topic, true/*allow wildcards*/);
        } catch (IllegalArgumentException e) {
            TipUtil.showSnackbar(mLinearLayout, e.getMessage());
            return;
        }

        int qos = mQoSLayout.getQoS();

        Subscription subscription = new Subscription(topic, qos);

        Intent intent = new Intent();
        intent.putExtra(Constant.ExtraConstant.EXTRA_SUBSCRIPTION, subscription);
        setResult(RESULT_OK, intent);
        finish();

    }


}
