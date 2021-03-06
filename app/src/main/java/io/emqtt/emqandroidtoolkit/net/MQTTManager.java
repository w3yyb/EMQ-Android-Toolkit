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

import java.util.HashMap;

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


    private HashMap<String, MqttAsyncClient> mHashMap;


    public static MQTTManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MQTTManager();
        }

        return INSTANCE;
    }

    private MQTTManager() {
        mHashMap = new HashMap<>();

    }

    public static void release() {
        if (INSTANCE != null) {
            INSTANCE.disconnectAllClient();
            INSTANCE = null;
        }
    }


    public MqttAsyncClient createClient(String id, String serverURI, String clientId) {
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        MqttAsyncClient client = null;
        try {
            client = new MqttAsyncClient(serverURI, clientId, mqttClientPersistence);
            client.setCallback(new MqttCallback() {
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
            });

            mHashMap.put(id, client);

        } catch (MqttException e) {
            e.printStackTrace();
        }
        return client;

    }

    public void connect(MqttAsyncClient client, MqttConnectOptions options) {
        try {
            client.connect(options, "Connect", new IMqttActionListener() {
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

    public void subscribe(MqttAsyncClient client, String topic, int qos) {
        if (isConnected(client)) {
            try {
                client.subscribe(topic, qos, "Subscribe", new IMqttActionListener() {
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

    public void unsubscribe(MqttAsyncClient client, String topic) {
        if (isConnected(client)) {
            try {
                client.unsubscribe(topic, "Unsubscribe", new IMqttActionListener() {
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

    public void publish(MqttAsyncClient client, String topic, MqttMessage mqttMessage) {
        if (isConnected(client)) {
            try {
                client.publish(topic, mqttMessage, "Publish", new IMqttActionListener() {
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

    public boolean disconnect(MqttAsyncClient client) {
        if (!isConnected(client)) {
            return true;
        }

        try {
            client.disconnect();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void disconnectAllClient() {
        for (MqttAsyncClient client : mHashMap.values()) {
            disconnect(client);
        }
    }


    public boolean isConnected(MqttAsyncClient client) {
        return client != null && client.isConnected();
    }

    public MqttAsyncClient getClient(String id) {
        return mHashMap.get(id);
    }
}
