package io.emqtt.emqandroidtoolkit.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * ClassName: Connection
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class Connection extends RealmObject implements Parcelable {

    private String host;

    private String port;

    private String clientId;

    private boolean cleanSession;

    private String username;

    private String password;


    public Connection() {
    }

    public Connection(String host, String port, String clientId, boolean cleanSession) {
        this.host = host;
        this.port = port;
        this.clientId = clientId;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getServerURI() {
        return "tcp://"+host + ":" + port;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.host);
        dest.writeString(this.port);
        dest.writeString(this.clientId);
        dest.writeByte(this.cleanSession ? (byte) 1 : (byte) 0);
        dest.writeString(this.username);
        dest.writeString(this.password);
    }

    protected Connection(Parcel in) {
        this.host = in.readString();
        this.port = in.readString();
        this.clientId = in.readString();
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
