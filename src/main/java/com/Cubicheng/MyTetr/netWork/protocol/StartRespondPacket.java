package com.Cubicheng.MyTetr.netWork.protocol;

import lombok.Data;

@Data
public class StartRespondPacket extends Packet{
    @Override
    public Byte getCommand() {
        return Command.START_RESPONSE;
    }
}
