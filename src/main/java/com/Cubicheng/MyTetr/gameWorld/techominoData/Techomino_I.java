package com.Cubicheng.MyTetr.gameWorld.techominoData;

import com.Cubicheng.MyTetr.util.Pair;

public class Techomino_I extends Techomino{
    public Techomino_I() {

        techomino[0] = new Pair[]{new Pair<>(-1, 0), new Pair<>(1, 0), new Pair<>(2, 0)};
        techomino[1] = new Pair[]{new Pair<>(0, 1), new Pair<>(0, -1), new Pair<>(0, -2)};
        techomino[2] = new Pair[]{new Pair<>(-2, 0), new Pair<>(-1, 0), new Pair<>(1, 0)};
        techomino[3] = new Pair[]{new Pair<>(0, 2), new Pair<>(0, 1), new Pair<>(0, -1)};

        offset[0] = new Pair[]{new Pair<>(0, 0), new Pair<>(-1, 0), new Pair<>(2, 0), new Pair<>(-1, 0), new Pair<>(2, 0)};
        offset[1] = new Pair[]{new Pair<>(-1, 0), new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 1), new Pair<>(0, -2)};
        offset[2] = new Pair[]{new Pair<>(-1, 1), new Pair<>(1, 1), new Pair<>(-2, 1), new Pair<>(1, 0), new Pair<>(-2, 0)};
        offset[3] = new Pair[]{new Pair<>(0, 1), new Pair<>(0, 1), new Pair<>(0, 1), new Pair<>(0, -1), new Pair<>(0, 2)};
    }
}
