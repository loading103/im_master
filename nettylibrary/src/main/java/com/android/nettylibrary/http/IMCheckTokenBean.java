package com.android.nettylibrary.http;

import java.util.List;

public class IMCheckTokenBean {
    private List<String> aud;
    private List<String> scope;
    private boolean active;
    private long exp;
    private String client_id;
    public void setAud(List<String> aud) {
        this.aud = aud;
    }
    public List<String> getAud() {
        return aud;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }
    public List<String> getScope() {
        return scope;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean getActive() {
        return active;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
    public long getExp() {
        return exp;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
    public String getClient_id() {
        return client_id;
    }
}
