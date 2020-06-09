package com.z_martin.common.entity;

public class NetworkChangeEvent {
    public boolean isConnected;

    public NetworkChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
