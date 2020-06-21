package com.android.nettylibrary.http;
public class IMHostBean {
    private String status;
    private String message;
    private HostBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HostBean getData() {
        return data;
    }

    public void setData(HostBean data) {
        this.data = data;
    }

    public class HostBean {
        private String websocketAddress;
        private String socketAddress;

        public String getWebsocketAddress() {
            return websocketAddress;
        }

        public void setWebsocketAddress(String websocketAddress) {
            this.websocketAddress = websocketAddress;
        }

        public String getSocketAddress() {
            return socketAddress;
        }

        public void setSocketAddress(String socketAddress) {
            this.socketAddress = socketAddress;
        }
    }
}
