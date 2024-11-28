package com.Cubicheng.MyTetr.gameWorld.techominoData;

import com.Cubicheng.MyTetr.util.Pair;

public class Techomino_T extends Techomino{
    public Techomino_T() {

        techomino[0] = new Pair[]{new Pair<>(0, 0), new Pair<>(-1, 0), new Pair<>(0, 1), new Pair<>(1, 0)};
        techomino[1] = new Pair[]{new Pair<>(0, 0), new Pair<>(0, 1), new Pair<>(1, 0), new Pair<>(0, -1)};
        techomino[2] = new Pair[]{new Pair<>(0, 0), new Pair<>(-1, 0), new Pair<>(1, 0), new Pair<>(0, -1)};
        techomino[3] = new Pair[]{new Pair<>(0, 0), new Pair<>(-1, 0), new Pair<>(0, 1), new Pair<>(0, -1)};

        offset[0] = new Pair[]{new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0)};
        offset[1] = new Pair[]{new Pair<>(0, 0), new Pair<>(1, 0), new Pair<>(1, -1), new Pair<>(0, 2), new Pair<>(1, 2)};
        offset[2] = new Pair[]{new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0), new Pair<>(0, 0)};
        offset[3] = new Pair[]{new Pair<>(0, 0), new Pair<>(-1, 0), new Pair<>(-1, -1), new Pair<>(0, 2), new Pair<>(-1, 2)};
    }

}
