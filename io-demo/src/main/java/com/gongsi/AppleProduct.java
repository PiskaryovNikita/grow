package com.gongsi;

import java.io.Serializable;

public class AppleProduct implements Serializable {

    private static final long serialVersionUID = 1234567L;

    public String headphonePort;
    public String thunderboltPort;
    public String lightningPort;

    public String getHeadphonePort() {
        return headphonePort;
    }

    public String getThunderboltPort() {
        return thunderboltPort;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLightningPort() {
        return lightningPort;
    }

}