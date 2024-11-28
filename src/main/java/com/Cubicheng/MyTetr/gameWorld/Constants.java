package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.gameWorld.techominoData.*;

import java.util.Map;

public class Constants {
    public static final int MAP_WIDTH = 10;
    public static final int MAP_HEIGHT = 40;
    public static final double BLOCK_SIZE = 47/1.75;
    public static double startX = (double) 1035 / 2520;
    public static double startY = (double) 390 / 1680;

    public static final Techomino TECHOMINO_S = new Techomino_S();
    public static final Techomino TECHOMINO_Z = new Techomino_Z();
    public static final Techomino TECHOMINO_L = new Techomino_L();
    public static final Techomino TECHOMINO_J = new Techomino_J();
    public static final Techomino TECHOMINO_T = new Techomino_T();
    public static final Techomino TECHOMINO_O = new Techomino_O();
    public static final Techomino TECHOMINO_I = new Techomino_I();

    public static Map<Integer,TechominoType> int2techominoType = Map.of(
            0, TechominoType.I,
            1, TechominoType.J,
            2, TechominoType.L,
            3, TechominoType.O,
            4, TechominoType.S,
            5, TechominoType.T,
            6, TechominoType.Z
    );

    public static Map<Integer,Techomino> int2techomino = Map.of(
            0, TECHOMINO_I,
            1, TECHOMINO_J,
            2, TECHOMINO_L,
            3, TECHOMINO_O,
            4, TECHOMINO_S,
            5, TECHOMINO_T,
            6, TECHOMINO_Z
    );
}
