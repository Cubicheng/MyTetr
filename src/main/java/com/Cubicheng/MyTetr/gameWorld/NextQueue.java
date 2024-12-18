package com.Cubicheng.MyTetr.gameWorld;

import java.util.*;

public class NextQueue {

    private Vector<Integer> queue;
    private List<Integer> piece_id = new ArrayList<>();
    private final Random random;

    public NextQueue(long seed) {
        for (int i = 0; i < 7; i++) {
            piece_id.add(i);
        }
        queue = new Vector<>();
        random = new Random(seed);
        add_a_new_pack();
    }

    void add_a_new_pack() {
        Collections.shuffle(piece_id, random);
        for (int i = 0; i < 7; i++) {
            queue.add(piece_id.get(i));
        }
    }

    public int get_next_piece(int id) {
        return queue.get(id);
    }

    public int get_next_piece_and_pop() {
        int tmp = queue.removeFirst();
        if (queue.size() < 5) {
            add_a_new_pack();
        }
        return tmp;
    }
}
