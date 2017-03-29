package io.emqtt.emqandroidtoolkit.net;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * ClassName: MQTTManager
 * Desc:
 * Created by zhiw on 2017/3/24.
 */

public class MQTTManager {

    private static MQTTManager INSTANCE;


    public static MQTTManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MQTTManager();
        }

        return INSTANCE;
    }

    private MQTTManager() {

    }

    public MqttAsyncClient createClient(String serverURI, String clientId) {
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        try {
            MqttAsyncClient client = new MqttAsyncClient(serverURI, clientId, mqttClientPersistence);
            return client;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;

    }

}
