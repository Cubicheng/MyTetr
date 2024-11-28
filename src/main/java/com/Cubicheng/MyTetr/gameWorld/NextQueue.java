package com.Cubicheng.MyTetr.gameWorld;

import java.util.*;

public class NextQueue {

    private Vector<Integer> next_queue;
    private List<Integer> piece_id = new ArrayList<>();

    public NextQueue() {
        for (int i = 0; i < 7; i++) {
            piece_id.add(i);
        }
        next_queue = new Vector<>();
        add_a_new_pack();
    }

    void add_a_new_pack() {
        Collections.shuffle(piece_id);
        for (int i = 0; i < 7; i++) {
            next_queue.add(piece_id.get(i));
        }
    }

    public int get_next_piece() {
        int tmp = next_queue.removeFirst();
        if(next_queue.size()<5){
            add_a_new_pack();
        }
        return tmp;
    }
}
