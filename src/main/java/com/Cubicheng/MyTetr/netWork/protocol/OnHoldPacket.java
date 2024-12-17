package com.Cubicheng.MyTetr.netWork.protocol;

public class OnHoldPacket extends Packet{
    @Override
    public Byte getCommand() {
        return Command.HOLD;
    }
}
