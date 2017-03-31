package io.emqtt.emqandroidtoolkit.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * ClassName: Subscription
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public class Subscription extends RealmObject implements Parcelable {

    private String clientId;

    private String topic;

    private
    @QoSConstant.QoS
    int QoS;


    @Ignore
    private EmqMessage message;

    public Subscription() {
    }

    public Subscription(String topic, int qos) {
        this.topic = topic;
        QoS = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQoS() {
        return QoS;
    }

    public void setQoS(int qoS) {
        QoS = qoS;
    }


    public EmqMessage getMessage() {
        return message;
    }

    public void setMessage(EmqMessage message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientId);
        dest.writeString(this.topic);
        dest.writeInt(this.QoS);
    }

    protected Subscription(Parcel in) {
        this.clientId = in.readString();
        this.topic = in.readString();
        this.QoS = in.readInt();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel source) {
            return new Subscription(source);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };
}
