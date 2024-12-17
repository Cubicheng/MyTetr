package com.Cubicheng.MyTetr.netWork.protocol;

import lombok.Data;

@Data
public class StartRespondPacket extends Packet{
    private long seed;

    public StartRespondPacket(long seed) {
        this.seed = seed;
    }

    public long getSeed(){
        return seed;
    }

    @Override
    public Byte getCommand() {
        return Command.START_RESPONSE;
    }
}
