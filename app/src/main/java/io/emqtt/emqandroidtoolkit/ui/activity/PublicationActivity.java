package io.emqtt.emqandroidtoolkit.ui.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Publication;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.ui.widget.QoSChooseLayout;
import io.emqtt.emqandroidtoolkit.util.TipUtil;

public class PublicationActivity extends ToolBarActivity {

    @BindView(R.id.topic) EditText mTopic;
    @BindView(R.id.payload) EditText mPayload;
    @BindView(R.id.qos) QoSChooseLayout mQoSChooseLayout;
    @BindView(R.id.retained) Switch mRetainedSwitch;
    @BindView(R.id.btn_publish) Button mPublishBtn;
    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pubilcation;
    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.btn_publish)
    public void onViewClicked() {
        String topic = mTopic.getText().toString().trim();
        if (topic.isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Invalid topic length");
            return;
        }
        int qos = mQoSChooseLayout.getQoS();
        String payload = mPayload.getText().toString().trim();
        boolean isRetained = mRetainedSwitch.isChecked();

        Publication publication = new Publication(topic, payload, qos, isRetained);

        Intent intent = new Intent();
        intent.putExtra(Constant.ExtraConstant.EXTRA_PUBLICATION, publication);
        setResult(RESULT_OK, intent);
        finish();
    }
}
