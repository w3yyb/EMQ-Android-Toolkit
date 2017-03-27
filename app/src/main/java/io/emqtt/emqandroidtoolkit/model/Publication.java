package io.emqtt.emqandroidtoolkit.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ClassName: Publication
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public class Publication implements Parcelable {

    private String topic;

    private String payload;

    private
    @QoSConstant.QoS
    int QoS;

    private boolean isRetained;


    public Publication(String topic, String payload, int qoS, boolean isRetained) {
        this.topic = topic;
        this.payload = payload;
        QoS = qoS;
        this.isRetained = isRetained;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getQoS() {
        return QoS;
    }

    public void setQoS(int qoS) {
        QoS = qoS;
    }

    public boolean isRetained() {
        return isRetained;
    }

    public void setRetained(boolean retained) {
        isRetained = retained;
    }

    public MqttMessage getMessage() {
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setQos(QoS);
        message.setRetained(isRetained());
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topic);
        dest.writeString(this.payload);
        dest.writeInt(this.QoS);
        dest.writeByte(this.isRetained ? (byte) 1 : (byte) 0);
    }

    protected Publication(Parcel in) {
        this.topic = in.readString();
        this.payload = in.readString();
        this.QoS = in.readInt();
        this.isRetained = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Publication> CREATOR = new Parcelable.Creator<Publication>() {
        @Override
        public Publication createFromParcel(Parcel source) {
            return new Publication(source);
        }

        @Override
        public Publication[] newArray(int size) {
            return new Publication[size];
        }
    };


}
