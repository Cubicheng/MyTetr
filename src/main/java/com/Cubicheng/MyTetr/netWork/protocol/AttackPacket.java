package com.Cubicheng.MyTetr.netWork.protocol;

public class AttackPacket extends Packet{
    private int attack;
    private int x;

    public AttackPacket(int attack,int x) {
        this.attack = attack;
        this.x = x;
    }

    public int getAttack() {
        return attack;
    }

    public int getX(){
        return x;
    }

    @Override
    public Byte getCommand() {
        return Command.ATTACK;
    }
}
