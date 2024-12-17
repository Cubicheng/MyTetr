package com.Cubicheng.MyTetr.netWork.protocol;

public interface Command {
    Byte START_RESPONSE = 1;
    Byte UPDATE_MOVABLE_PIECE = 2;
    Byte HARD_DROP = 3;
    Byte HOLD = 4;

    static Class<? extends Packet> getRequestByCommand(byte command) {
        if(command == START_RESPONSE){
            return StartRespondPacket.class;
        }
        if(command == UPDATE_MOVABLE_PIECE){
            return UpdateMovablePiecePacket.class;
        }
        if(command == HARD_DROP){
            return OnHardDropPacket.class;
        }
        if(command == HOLD){
            return OnHoldPacket.class;
        }
        return null;
    }
}
