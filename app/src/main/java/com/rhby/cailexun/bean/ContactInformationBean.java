package com.rhby.cailexun.bean;

import java.io.Serializable;

/**
 */
public class ContactInformationBean implements Serializable {
    public int userId;
    public String userName;
    public String realName;
    public String email;
    public String mobile;
    public String userPhoto;
    public String letter;

    public ContactInformationBean(String realName) {
        this.realName = realName;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getMobile() {
        return mobile;
    }

}
