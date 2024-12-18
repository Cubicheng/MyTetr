package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.gameWorld.techominoData.*;

import java.util.Map;

import static com.Cubicheng.MyTetr.gameWorld.Type.*;

public class Variables {
    public static final int MAP_WIDTH = 10;
    public static final int MAP_HEIGHT = 40;
    public static final double BLOCK_SIZE = 47 / 1.75;
    public static double startX = (double) 1035 / 2520;
    public static double startY = (double) 390 / 1680;
    public static double SOFT_DROP_TIME = 0.75;

    public static final Techomino TECHOMINO_S = new Techomino_S();
    public static final Techomino TECHOMINO_Z = new Techomino_Z();
    public static final Techomino TECHOMINO_L = new Techomino_L();
    public static final Techomino TECHOMINO_J = new Techomino_J();
    public static final Techomino TECHOMINO_T = new Techomino_T();
    public static final Techomino TECHOMINO_O = new Techomino_O();
    public static final Techomino TECHOMINO_I = new Techomino_I();

    public static final String config_file_path = "config/config.json";

    public static Map<Integer, Techomino> int2techomino = Map.of(
            0, TECHOMINO_I,
            1, TECHOMINO_J,
            2, TECHOMINO_L,
            3, TECHOMINO_O,
            4, TECHOMINO_S,
            5, TECHOMINO_T,
            6, TECHOMINO_Z
    );

    public static Map<Type, Integer> typeSize = Map.of(
            MovablePiece, 1,
            GhostPiece, 1,
            NextPiece, 5,
            OnePiece, 0,
            HoldPiece, 1,
            GameMap, 1,
            WarnPiece, 1,
            MapImageEntity, 1
    );

    public static long seed = 0;
}
