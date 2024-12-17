package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.Pair;

import java.util.Vector;

public class AttackQueue {
    private Vector<Pair<Integer, Integer>> queue;

    public AttackQueue() {
        queue = new Vector<>();
    }

    public void add(int attack, int x) {
        queue.add(new Pair<>(attack, x));
    }

    public Pair<Integer, Integer> get_front() {
        if(queue.size()==0){
            return null;
        }
        return queue.removeFirst();
    }
}
