package io.emqtt.emqandroidtoolkit.ui.activity;

import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Publication;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.ui.widget.QoSChooseLayout;
import io.emqtt.emqandroidtoolkit.util.RealmHelper;
import io.emqtt.emqandroidtoolkit.util.TipUtil;
import io.realm.RealmResults;

public class PublicationActivity extends ToolBarActivity {

    @BindView(R.id.topic) EditText mTopic;
    @BindView(R.id.payload) EditText mPayload;
    @BindView(R.id.qos) QoSChooseLayout mQoSChooseLayout;
    @BindView(R.id.retained) Switch mRetainedSwitch;
    @BindView(R.id.btn_publish) Button mPublishBtn;
    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;
    @BindView(R.id.spinner) Spinner mSpinner;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pubilcation;
    }

    @Override
    protected void setUpView() {

        RealmResults<Subscription> results = RealmHelper.getInstance().queryAll(Subscription.class);
        final List<String> list = new ArrayList<>();
        for (Subscription result : results) {
            list.add(result.getTopic());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String topic = list.get(position);
                mTopic.setText(topic);
                mTopic.setSelection(topic.length());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.btn_publish)
    public void onViewClicked() {
        String topic = mTopic.getText().toString().trim();

        try{
            MqttTopic.validate(topic, false/*wildcards NOT allowed*/);
        }catch (IllegalArgumentException e){
            TipUtil.showSnackbar(mLinearLayout, e.getMessage());
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
