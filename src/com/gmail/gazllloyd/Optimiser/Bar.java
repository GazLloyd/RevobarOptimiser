package com.gmail.gazllloyd.Optimiser;

import java.util.ArrayList;

/**
 * Created by Gareth Lloyd on 12/08/2015.
 */ //
public class Bar {
    public double val;
    public ArrayList<Ability> used;
    public ArrayList<Integer> usedints;
    public boolean err;
    public Bar() {
        err = false;
        used = new ArrayList<>();
        usedints = new ArrayList<>();
        val = 0;
    }

    @Override
    public String toString() {
        String str = "";
        for (Ability a : used) {
            str += a.name + ",";
        }
        return str;
    }

    public String key() {
        String s = "";
        for (int i : usedints) {
            s += i;
        }
        return s;
    }
}
