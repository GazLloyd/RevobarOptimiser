package rsw.gazlloyd.Optimiser;

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
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (Ability a : used) {
            str.append(a.name);
            if (++i < used.size()) {
                str.append(",");
            }
        }
        return str.toString();
    }

    public String key() {
        String s = "";
        for (int i : usedints) {
            s += i;
        }
        return s;
    }
}
