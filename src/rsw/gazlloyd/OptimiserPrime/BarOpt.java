package rsw.gazlloyd.OptimiserPrime;

import rsw.gazlloyd.Optimiser.util.SepaPnkIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by gaz-l on 17/12/2017.
 */
public class BarOpt {
    static int MAX_ABILS = 14;
    ArrayList<Ability> abils = new ArrayList<>();
    static HashMap<String,Ability> abilMap = Ability.abilities;

    HashMap<ArrayList<Integer>,Boolean> doneBars = new HashMap<>();

    Revobar best = new NullRevobar();

    public BarOpt(String... abin) {
        for (String s : abin) {
            if (abilMap.containsKey(s)) {
                abils.add(abilMap.get(s));
            }
        }
        run();
    }

    private void run() {
        int i = 0;
        ArrayList<Ability> b;
        ArrayList<Integer>  c;
        Revobar curr;
        for(Iterator<int[]> it = new SepaPnkIterator(abils.size(), Math.min(MAX_ABILS, abils.size())); it.hasNext(); i++) {
            c = Util.intArrToList(it.next());

            boolean skip = false;
            for (int j = 2; i < 16; i++) {
                if (doneBars.get(c.subList(0, j)) != null) {
                    skip = true;
                    break;
                }
            }
            if (skip) continue;
            b = new ArrayList<>();
            for (int j : c) {
                b.add(abils.get(j));
            }
            curr = new Revobar(b);
            if (curr.calcRevo() > best.revoval) {
                best = curr;
            }
            if (curr.calcRevo() != -1) {
                doneBars.put(curr.asIntsUsed, true);
            }
        }
        System.out.println(best);
    }



}
