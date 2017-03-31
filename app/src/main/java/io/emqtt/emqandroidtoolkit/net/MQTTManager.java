package io.emqtt.emqandroidtoolkit.net;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;

import io.emqtt.emqandroidtoolkit.Constant;
import io.emqtt.emqandroidtoolkit.event.MQTTActionEvent;
import io.emqtt.emqandroidtoolkit.event.MessageEvent;
import io.emqtt.emqandroidtoolkit.model.EmqMessage;
import io.emqtt.emqandroidtoolkit.util.LogUtil;

/**
 * ClassName: MQTTManager
 * Desc:
 * Created by zhiw on 2017/3/24.
 */

public class MQTTManager {

    private static MQTTManager INSTANCE;

    private MqttAsyncClient mClient;


    public static MQTTManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MQTTManager();
        }

        return INSTANCE;
    }

    private MQTTManager() {

    }

    public static void release() {
        if (INSTANCE != null) {
            INSTANCE.disconnect();
            INSTANCE = null;
        }
    }


    public void createClient(String serverURI, String clientId) {
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        try {
            mClient = new MqttAsyncClient(serverURI, clientId, mqttClientPersistence);
            mClient.setCallback(mCallback);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void connect(MqttConnectOptions options) {
        try {
            mClient.connect(options, "Connect", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.CONNECT_SUCCESS, asyncActionToken));

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.CONNECT_FAIL, asyncActionToken, exception));

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos) {
        if (isConnected()) {
            try {
                mClient.subscribe(topic, qos, "Subscribe", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.SUBSCRIBE_SUCCESS, asyncActionToken));

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.SUBSCRIBE_FAIL, asyncActionToken, exception));


                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void unsubscribe(String topic) {
        if (isConnected()) {
            try {
                mClient.unsubscribe(topic, "Unsubscribe", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.UNSUBSCRIBE_SUCCESS, asyncActionToken));

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.UNSUBSCRIBE_FAIL, asyncActionToken, exception));

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void publish(String topic, MqttMessage mqttMessage) {
        if (isConnected()) {
            try {
                mClient.publish(topic, mqttMessage, "Publish", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.PUBLISH_SUCCESS, asyncActionToken));

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.PUBLISH_FAIL, asyncActionToken, exception));

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


    }

    public boolean disconnect() {
        if (!isConnected()) {
            return true;
        }

        try {
            mClient.disconnect();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isConnected() {
        return mClient != null && mClient.isConnected();
    }

    private MqttCallback mCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            LogUtil.e("connectionLost");
            EventBus.getDefault().post(new MQTTActionEvent(Constant.MQTTStatusConstant.CONNECTION_LOST, null, cause));

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            LogUtil.d("topic is " + topic + ",message is " + message.toString() + ", qos is " + message.getQos());
            EventBus.getDefault().postSticky(new MessageEvent(new EmqMessage(topic, message)));

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            LogUtil.d("deliveryComplete");


        }
    };

}
