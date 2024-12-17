package com.Cubicheng.MyTetr.netWork.protocol;

public class UpdateMovablePiecePacket extends Packet{
    private int x;
    private int y;
    private int rotate_index;

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public int getRotate_index() {
        return rotate_index;
    }

    public UpdateMovablePiecePacket(int x, int y, int rotate_index) {
        this.x = x;
        this.y = y;
        this.rotate_index = rotate_index;
    }

    @Override
    public Byte getCommand() {
        return Command.UPDATE_MOVABLE_PIECE;
    }
}
