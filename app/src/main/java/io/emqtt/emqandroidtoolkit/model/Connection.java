package io.emqtt.emqandroidtoolkit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ClassName: Connection
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class Connection implements Parcelable {

    private String host;

    private String port;

    private String clintId;

    private boolean cleanSession;

    private String username;

    private String password;

    public Connection(String host, String port, String clintId, boolean cleanSession) {
        this.host = host;
        this.port = port;
        this.clintId = clintId;
        this.cleanSession = cleanSession;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getClintId() {
        return clintId;
    }

    public void setClintId(String clintId) {
        this.clintId = clintId;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.host);
        dest.writeString(this.port);
        dest.writeString(this.clintId);
        dest.writeByte(this.cleanSession ? (byte) 1 : (byte) 0);
        dest.writeString(this.username);
        dest.writeString(this.password);
    }

    protected Connection(Parcel in) {
        this.host = in.readString();
        this.port = in.readString();
        this.clintId = in.readString();
        this.cleanSession = in.readByte() != 0;
        this.username = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<Connection> CREATOR = new Parcelable.Creator<Connection>() {
        @Override
        public Connection createFromParcel(Parcel source) {
            return new Connection(source);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };
}
