package Optimiser;

import java.util.Arrays;

/**
 * Created by Gareth Lloyd on 12/08/2015.
 */ //cycle class
class Cycle {
    public boolean hascycle;
    public String cycleints;
    public String cycle;
    public int ticks;
    public double dpt;
    public String preambleints = "";
    public String preamble = "";
    public boolean haspreamble;
    public Ability[] bar;
    public int used;
    public static boolean STUNS;

    public Cycle(Ability[] b, int u, String c,boolean s) {
        this.bar = b;
        this.used = u;
        STUNS = s;
        findcycle(c);

    }

    private boolean usedAll(String c) {
        boolean u = true;
        for (int i = 0; i < used; i++) {
            if (!c.contains(""+i)) {
                u = false;
                break;
            }
        }
        return u;
    }

    private void findcycle(String v) {
        String c = "";
        int l = v.length();
        int i = 0;

        //System.out.println("v: "+v);

        hascycle = false;
        while (!hascycle) {
            i++;
            if (l < 2*i) break;
            c = v.substring(l-i);
            if (usedAll(c) && v.substring(l-2*i).equalsIgnoreCase(c + c)) {
                hascycle=true;
            }
        }

        if (!hascycle) {
            //System.out.println("No cycle found for: "+Arrays.toString(bar)+" in "+i+"  used: "+used);
            //System.out.println("Bar "+Arrays.toString(bar));
            //System.out.println("v: "+v);
            return;
        }

        boolean reord = false;
        String ord = "012345678";
        int reordi = 0;
        reorderloop:
        while (!reord && reordi < c.length()) {
            ord = "012345678";
            while (ord.length() > 1) {
                if (c.contains(ord)) {
                    reord=true;
                    break reorderloop;
                }
                ord = ord.substring(0,ord.length()-1);
            }
            c = cyclefore(c);
            reordi++;
        }

        if (reord) {
            cycleints = c.substring(c.indexOf(ord));
            cycleints += c.substring(0, c.indexOf(ord));
        }
        else {
            cycleints = c;
        }

        //System.out.println("c: "+cycleints+"  i: "+i);

        //System.out.println("Bar "+Arrays.toString(bar));
        //System.out.println("v: "+v);
        //System.out.println("c: "+cycleints);


        String twocycle= cycleints+cycleints;
        if (twocycle.contains(v.substring(0, cycleints.length()))) {
            haspreamble = false;
        }
        else {
            haspreamble = true;
            int jind = v.indexOf(cycleints), jind2;
            String cy = new String(cycleints);
            for (int j = 0; j < cy.length(); j++) {
                cy = cyclefore(cy);
                jind2 = v.indexOf(cy);
                if (jind2 == -1) return;
                if (jind2 < jind)
                    jind = jind2;
            }
            preambleints = v.substring(0,jind);
            String s2 = "";
            for (int j = 0; j < preambleints.length(); j++) {
                s2 += bar[Character.getNumericValue(preambleints.charAt(j))].name+" ";
            }
            preamble = s2;/*
            System.out.println("Bar "+Arrays.toString(bar));
            System.out.println("v: "+v);
            System.out.println("c: "+cycleints);*/
            //System.out.println("Has preamble: "+preambleints);
        }
        calccycle();

    }
    private String cyclefore(String s) {
        String s1 = s.substring(s.length() - 1); //last char put at front
        s1 += s.substring(0,s.length()-1); //rest of things
        return s1;
    }

    private double calccycle() {
        double dam = 0;
        int t = 0, stundur = 0;
        String s = "";
        int ind = 0;
        String v1 = cycleints;

        boolean done = false;
        while (!done) {
            s = "";
            ind=-1;
            dam=0;
            stundur=0;
            t=0;

            for (int i = 0; i < v1.length(); i++) {
                if (ind > -1)
                    stundur = Math.max(0, stundur - bar[ind].duration);
                ind = Character.getNumericValue(v1.charAt(i));
                s += bar[ind].name + " ";
                dam += stundur > 0 ? bar[ind].stundmg : bar[ind].damage;
                t += bar[ind].duration;
                if (STUNS && bar[ind].stuns)
                    stundur = bar[ind].stundur;
            }
            if (stundur > 0) {
                v1 = cyclefore(v1);
            } else {
                done = true;
            }
        }


        dpt = dam/t;
        ticks = t;
        cycle = s;

        //System.out.println("Ability cycle: "+cycle+"  Cycle time: "+t+"  damage: "+dam+"  DPT: "+dpt);


        return dpt;
    }

    @Override
    public String toString() {
        return ("Bar "+ Arrays.toString(bar)+"\nAbility cycle: "+cycle+"  DPT: "+dpt+ (haspreamble ? "  preamble: "+preamble : ""));
    }
}
