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

    public int clear_attack(int attack) {
        while (!queue.isEmpty()) {
            if (queue.getFirst().first() <= attack) {
                sum -= attack;
                attack -= queue.getFirst().first();
                queue.removeFirst();
            } else {
                sum -= attack;
                queue.getFirst().setFirst(queue.getFirst().first() - attack);
                break;
            }
        }
        return attack;
    }

    public void pop_front() {
        if (queue.isEmpty()) {
            return;
        }
        queue.removeFirst();
    }

    public void set_first(int attack,int x){
        if (queue.isEmpty()) {
            return;
        }
        queue.getFirst().setFirst(attack);
        queue.getFirst().setSecond(x);
    }

    public Pair<Integer, Integer> get_front() {
        if (queue.isEmpty()) {
            return null;
        }
        sum -= queue.getFirst().first();
        return queue.getFirst();
    }
}
