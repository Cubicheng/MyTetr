package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.Pair;

import java.util.Vector;

public class AttackQueue {
    private Vector<Pair<Integer, Integer>> queue;
    private int sum = 0;

    public int getSum() {
        return sum;
    }

    public AttackQueue() {
        queue = new Vector<>();
    }

    public void add(int attack, int x) {
        queue.add(new Pair<>(attack, x));
        sum += attack;
    }

    public Pair<Integer, Integer> get_front() {
        if (queue.isEmpty()) {
            return null;
        }
        sum -= queue.getFirst().first();
        return queue.removeFirst();
    }
}
