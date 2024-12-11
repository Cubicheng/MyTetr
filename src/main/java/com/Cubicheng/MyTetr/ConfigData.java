package com.Cubicheng.MyTetr;

public class ConfigData {
    private double ghost_piece_visibility = 0.5;
    private long das = 167;
    private long arr = 33;
    private long sfd_ARR = 33;

    public void setGhost_piece_visibility(double ghost_piece_visibility) {
        this.ghost_piece_visibility = ghost_piece_visibility;
    }

    public void setDas(long das) {
        this.das = das;
    }

    public void setArr(long arr) {
        this.arr = arr;
    }

    public void setSfd_ARR(long sfd_ARR) {
        this.sfd_ARR = sfd_ARR;
    }

    public long getDas() {
        return das;
    }

    public long getArr() {
        return arr;
    }

    public long getSfd_ARR() {
        return sfd_ARR;
    }

    public double getGhost_piece_visibility() {
        return ghost_piece_visibility;
    }
}
