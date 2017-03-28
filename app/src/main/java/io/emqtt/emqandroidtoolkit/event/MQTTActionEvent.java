package io.emqtt.emqandroidtoolkit.event;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * ClassName: MQTTActionEvent
 * Desc:
 * Created by zhiw on 2017/3/28.
 */

public class MQTTActionEvent {

    private int status;


    private IMqttToken asyncActionToken;

    private Throwable exception;


    public MQTTActionEvent(int status, IMqttToken asyncActionToken) {
        this.status = status;
        this.asyncActionToken = asyncActionToken;
    }

    public MQTTActionEvent(int status, IMqttToken asyncActionToken, Throwable exception) {
        this.status = status;
        this.asyncActionToken = asyncActionToken;
        this.exception = exception;
    }

    public int getStatus() {
        return status;
    }

    public IMqttToken getAsyncActionToken() {
        return asyncActionToken;
    }

    public Throwable getException() {
        return exception;
    }
}
