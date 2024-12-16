package com.Cubicheng.MyTetr.netWork.protocol;

public interface Command {
    Byte START_RESPONSE = 1;
    Byte UPDATE_MOVABLE_PIECE = 2;

    static Class<? extends Packet> getRequestByCommand(byte command) {
        if(command == START_RESPONSE){
            return StartRespondPacket.class;
        }
        if(command == UPDATE_MOVABLE_PIECE){
            return UpdateMovablePiecePacket.class;
        }
        return null;
    }
}
