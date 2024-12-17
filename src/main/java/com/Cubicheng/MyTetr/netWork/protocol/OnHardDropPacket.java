package com.Cubicheng.MyTetr.netWork.protocol;

public class OnHardDropPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HARD_DROP;
    }
}
