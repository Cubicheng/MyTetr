package com.Cubicheng.MyTetr.netWork.protocol;

public class UpdateMovablePiecePacket extends Packet{
    private int x;
    private int y;
    private int rotate_index;
    private int techomino_type;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setRotate_index(int rotate_index) {
        this.rotate_index = rotate_index;
    }

    public int getRotate_index() {
        return rotate_index;
    }

    public void setTechomino_type(int techomino_type) {
        this.techomino_type = techomino_type;
    }

    public int getTechomino_type(){
        return techomino_type;
    }

    public UpdateMovablePiecePacket(int x, int y, int rotate_index, int techomino_type) {
        this.x = x;
        this.y = y;
        this.rotate_index = rotate_index;
        this.techomino_type = techomino_type;
    }

    @Override
    public Byte getCommand() {
        return Command.UPDATE_MOVABLE_PIECE;
    }
}
