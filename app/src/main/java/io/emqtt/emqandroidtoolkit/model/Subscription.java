package io.emqtt.emqandroidtoolkit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ClassName: Subscription
 * Desc:
 * Created by zhiw on 2017/3/23.
 */

public class Subscription implements Parcelable {

    private String topic;

    private
    @QoSConstant.QoS
    int QoS;

    private String displayName;

    private EmqMessage message;

    public Subscription(String topic, int qos) {
        this.topic = topic;
        QoS = qos;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        dest.writeString(this.topic);
        dest.writeInt(this.QoS);
        dest.writeString(this.displayName);
    }

    protected Subscription(Parcel in) {
        this.topic = in.readString();
        this.QoS = in.readInt();
        this.displayName = in.readString();
    }

    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
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
