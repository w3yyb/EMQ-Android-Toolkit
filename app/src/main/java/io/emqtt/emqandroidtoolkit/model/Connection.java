package io.emqtt.emqandroidtoolkit.model;

/**
 * ClassName: Connection
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class Connection {

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
}
