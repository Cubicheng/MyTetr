package com.Cubicheng.MyTetr.netWork.protocol;

public interface Command {
    Byte START_RESPONSE = 1;

    static Class<? extends Packet> getRequestByCommand(byte command) {
        if(command == START_RESPONSE){
            return StartRespondPacket.class;
        }
        return null;
    }
}
