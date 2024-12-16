package com.Cubicheng.MyTetr.netWork.protocol;

public abstract class Packet {
    private Byte version = 1;

    public abstract Byte getCommand();

    public int getVersion() {
        return version;
    }
}
