package io.emqtt.emqandroidtoolkit.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.emqtt.emqandroidtoolkit.util.StringUtil;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * ClassName: EmqMessage
 * Desc:
 * Created by zhiw on 2017/3/27.
 */

public class EmqMessage extends RealmObject {

    private String topic;

    @Ignore
    private MqttMessage mqttMessage;

    private String updateTime;

    private boolean mutable = true;

    private String payload;

    private int qos = 1;

    private boolean retained = false;

    private boolean dup = false;

    private int messageId;

    public EmqMessage() {
    }

    public EmqMessage(String topic, MqttMessage mqttMessage) {
        this.topic = topic;
        this.mqttMessage = mqttMessage;
        this.updateTime = StringUtil.formatNow();

        if (mqttMessage != null) {
            this.dup = mqttMessage.isDuplicate();
            this.payload = new String(mqttMessage.getPayload());
            this.qos = mqttMessage.getQos();
            this.messageId = mqttMessage.getId();
            this.retained = mqttMessage.isRetained();
        }


    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }

    public void setMqttMessage(MqttMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }

    public boolean isDup() {
        return dup;
    }

    public void setDup(boolean dup) {
        this.dup = dup;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
