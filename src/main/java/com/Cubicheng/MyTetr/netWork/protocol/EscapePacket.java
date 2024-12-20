package com.Cubicheng.MyTetr.netWork.protocol;

public class EscapePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.ESCAPE;
    }
}
