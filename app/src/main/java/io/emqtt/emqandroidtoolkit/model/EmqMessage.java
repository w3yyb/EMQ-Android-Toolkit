package io.emqtt.emqandroidtoolkit.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * ClassName: EmqMessage
 * Desc:
 * Created by zhiw on 2017/3/27.
 */

public class EmqMessage {

    private String topic;

    private MqttMessage mqttMessage;

    private String updateTime;

    public EmqMessage(String topic, MqttMessage mqttMessage) {
        this.topic = topic;
        this.mqttMessage = mqttMessage;
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        this.updateTime = format.format(System.currentTimeMillis());
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
}
