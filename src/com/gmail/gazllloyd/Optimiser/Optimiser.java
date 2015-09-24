package com.gmail.gazllloyd.Optimiser;

import java.util.*;

import com.gmail.gazllloyd.Optimiser.util.PermIterator;

/**
 * Created by Gareth Lloyd on 24/02/15.
 *
 * based on work by Cook Me Plox
 *
 * uses PermIterator from http://stackoverflow.com/questions/2000048/stepping-through-all-permutations-one-swap-at-a-time/11916946#11916946
 * or SepaPnkIterator from https://github.com/aisrael/jcombinatorics/blob/master/src/main/java/jcombinatorics/permutations/SepaPnkIterator.java
 */

public class Optimiser {

    //all abilities to be considered for the bar
    public static Ability[] abilities;

    public static HashMap<String,Boolean> barsdone;

    //maximum number of iterations - permuations is O(n!), so this stops it going for far too long if considering larger n
    //  if n! > MAX_ITER, you can run a few times, reordering the abilities array each time
    public static int MAX_ITER = 10000000;

    //number of ticks to run the simulated revobar for
    //  100 ticks per minute
    public static int TICKS = 3000;
    public static int CYCLE_TICKS = 600;
    public static int MAX_ABILS = 9;
    public static boolean STUNS = false;

    public static void main(String[] args) {
        //define all abilities to be considered
        Ability[] l = {

/*
                //attack
                new Ability("Slice",5,3,0.7), //0.7 from data - appears slice's minimum is ~30% and max is 110%
                //new Ability("Havoc",17,3,1.25*0.6), //dw
                new Ability("Backhand",25,3,1*0.6,5), //no kick included
                new Ability("Smash",17,3,1.25*0.6), //2h
                //new Ability("Barge",34,3,1.25*0.6), //doesn't work in instances
                new Ability("Sever",25,3,1.88*0.6),

                //strength
                new Ability("Punish",5,3,0.94*0.6,1.25*0.6),
                new Ability("Dismember",25,3,1.22), //50% chance of hitting 100%, 50% chance of being uniformly ditributed between 100% and 188%, thus 122% average
                new Ability("Fury",9,6,(0.75+0.82+0.89)*0.6),
                //new Ability("Cleave",12,3,1.88*0.6), //2h
                //new Ability("Decimate",12,3,1.88*0.6), //dw
*/


                //magic
                new Ability("Wrack", 5, 3, 0.564, 1.25*0.6),
                new Ability("Dragon Breath", 17, 3, 1.128),
                new Ability("Impact", 25, 3, 0.6, 5),
                new Ability("Combust", 25, 3, 1.22),
                new Ability("Chain", 17, 3, 0.6),
                //new Ability("Concentrated Blast", 9, 6, 1.476),
                new Ability("Sonic Wave", 9, 3, 0.942),
                //new Ability("Corruption Blast", 25, 3, 2),

/*
                //ranged
                new Ability("Piercing Shot", 5, 3, 0.564, 1.25*0.6),
                new Ability("Snipe", 17, 6, 1.72),
                //new Ability("Dazing Shot", 9, 3, 0.942),
                new Ability("Binding Shot", 25, 3, 0.6, 5),
                new Ability("Needle Strike", 9, 3, 0.992),
                new Ability("Fragmentation Shot", 25, 3, 1.22),
                new Ability("Ricochet", 17, 3, 0.6),
                new Ability("Corruption Shot", 25, 3, 2),
*/

                //defence
                //new Ability("Bash", 25, 3, 1.442*0.6),
                //new Ability("Anticipation",40,3,0),
                //new Ability("Freedom",50,3,0),
                //constitution
                new Ability("Sacrifice",50,3,0.6),
                new Ability("Tuska's Wrath",25,3,0.7), // (Non-Slayer)
                //new Ability("Tuska's Wrath",200,3,6.72), // (Slayer)

        };

        //since you cannot use array initialiser for abilities directly here
        abilities = l;

        STUNS = false;
        //iterate over every permutation, with a cap of MAX_ITER times
        int i = 0;
        int[] bar;
        Bar val = null;
        Bar best = new Bar();
        barsdone = new HashMap<String,Boolean>();
        MAX_ABILS = 9;
        for(Iterator<int[]> it = new PermIterator(abilities.length); it.hasNext() && i < MAX_ITER; i++) {
            //for(Iterator<int[]> it = new SepaPnkIterator(abilities.length, MAX_ABILS); it.hasNext() && i < MAX_ITER; i++) {
            bar = it.next();
            if (!checkbar(bar)) {
            //if (true) {
                val = calcrevo(bar, best.val);
                barsdone.put(val.used.toString(),true);
                if (val.val > best.val) {
                    best = val;
                }
            }
        }
        System.out.println("Best bar: " + best.toString());
        System.out.println("AADPT: " + best.val);
        System.out.println("Individual bars checked: " + barsdone.size());
        /*i = 0;
        Cycle cycl, cyclmax = null;
        max = 0;
        for(Iterator<int[]> it = new PermIterator(abilities.length); it.hasNext() && i < MAX_ITER; i++) {
            cycl = cyclerevo(reorder(it.next()));
            if (cycl.dpt > max) {
                max = cycl.dpt;
                cyclmax = cycl;
            }
        }
        System.out.println(cyclmax.toString());*/

        //int[] a = {6,3,5,8,1,4};
        //calcrevo(a, 0);
        //int[] b = {1,5,4,3,0,6};
        //calcrevo(reorder(b), 0);
        //int[] c = {1,5,2,3,0,6};
        //calcrevo(reorder(c), 0);

/*
        int i = 0;
        int pre = 0, errs = 0, preerr = 0;
        for(Iterator<int[]> it = new PermIterator(abilities.length); it.hasNext() && i < MAX_ITER; i++) {
            int[] a = it.next();
            Cycle c = cyclerevo(reorder(a));

            if (!c.hascycle && c.haspreamble) {
                preerr++;
                continue;
            }
            if (c.haspreamble) {
                pre++;
                continue;
            }
            if (!c.hascycle) {
                errs++;
            }
        }
        System.out.println("Number in "+i+" with: preambles "+pre+";  errors "+errs+";  preerrs: "+preerr);
*/

        //test a specific layout - watch for arrayoutofboundsexceptions
        //int[] a = {5,6,3,0};

        //CYCLE_TICKS = 1000;

        //int[] a = {0,1};
        //cyclerevo(reorder(a));
        //calcrevo(reorder(a), 0);
        //STUNS = true;
        //calcrevo(reorder(a),0);
        //cyclerevo(reorder(a));

    }

    public static boolean checkbar(int[] bar) {
        boolean out;
        ArrayList<Integer> b = new ArrayList<Integer>(bar.length);
        for (int i : bar) {
            b.add(i);
        }
        for (int i = 1; i < bar.length; i++) {
            if (barsdone.containsKey(b.subList(0,i))) {
                return true;
            }
        }
        return false;
    }


    //turn int[] into corresponding Ability[]
    public static Ability[] reorder(int[] order) {
        //make sure to create a new array, don't reorder the base array
        Ability[] out = new Ability[order.length];
        for (int i = 0; i < order.length; i++)
            out[i] = abilities[order[i]];

        return out;
    }

    //calculate the damage per tick of the revolution bar given, and only print it if it a new best
    public static Bar calcrevo(int[] inbar, double max) {
        Ability[] bar = reorder(inbar);
        int time = 0, incr = 0, stundur=0;
        double damage = 0;
        Ability next;
        String forcedabil = "Sacrifice";
        boolean forcedabilused = false;
        Bar out = new Bar();

        //reset all ability cooldowns and used status
        for (Ability a : abilities)
            a.reset();

        //test for TICKS
        while (time < TICKS) {
            next = null;
            String barstate = "";
            //reduce cooldown of all abilities by duration of previous ability
            for (Ability a : abilities) {
                a.reducecooldown(incr);
            }
            stundur = Math.max(0,stundur-incr);


            //for (int i = 0; i < bar.length && i < MAX_ABILS; i++) {
             //   barstate += "[" + bar[i].cd + "]  ";
            //}

            //determine next ability to be used
            //for (Ability a :  bar) {
            for (int i = 0; i < bar.length && i < MAX_ABILS; i++) {
                Ability a = bar[i];
                if (a.cd == 0) {
                    next = a;
                    a.used = true;
                    break;
                }
            }

            //if there is no next ability, we have a broken revobar
            if (next == null) {
                if (time > 0) {
                    //System.out.println("Bar " + Arrays.toString(bar) + " (stuns: " + STUNS + ")  damage: " + damage + " ticks " + time + "  DPT: " + damage / time);
                    //System.out.println("not enough abilities provided for a continuous revobar");
                }
                out.err = true;
                return out;
            }
            if (next.name == forcedabil) {
                forcedabilused = true;
                //System.out.println("Used anticipation at t=" + time);
            }
            //System.out.println("Used: " + next.name + "\t\t" + barstate);

            //activate the ability
            next.putoncooldown(); //put it on cooldown
            incr = next.duration; //set advancement (ticks)
            time += incr; //advance time by duration
            damage += stundur > 0 ? next.stundmg : next.damage; //increment damage
            if (STUNS && next.stuns)
                stundur = next.stundur;
        }

        double dps = damage/time;
        //System.out.println("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps+"  max:"+max);

        if (dps > max) {
            //System.out.println("Bar "+Arrays.toString(bar)+" (stuns: "+STUNS+")  damage: "+damage+" ticks "+time+"  DPT: "+dps+"\nUsed abilities:");
            //System.out.println(dps);
            String s = "";
            for (int i = 0; i < bar.length; i++)
                if (bar[i].used) {
                    s += bar[i]+",";
                }
            //System.out.println(s);
        }

        for (Ability a : bar)
            out.used.add(a);
        out.val = dps;
        if (!forcedabilused)
            out.val = 0;
        return out;
    }

    public static Cycle cyclerevo(Ability[] bar) {
        String s = "";
        int time = 0, incr = 0, next, used=0,stundur=0;
        double damage = 0;

        for (Ability a : abilities)
            a.reset();

        //test for TICKS
        while (time < CYCLE_TICKS) {
            next = -1;

            //reduce cooldown of all abilities by duration of previous ability
            for (Ability a : abilities)
                a.reducecooldown(incr);
            stundur = Math.max(0,stundur-incr);

            //determine next ability to be used
            for (int i = 0; i < bar.length; i++) {
                if (bar[i].cd == 0) {
                    next = i;
                    bar[i].used = true;
                    bar[i].usedcount++;
                    break;
                }
            }

            //if there is no next ability, we have a broken revobar
            if (next == -1) {
                //System.out.println("not enough abilities provided for a continuous revobar");
                return null;
            }

            //activate the ability
            bar[next].putoncooldown(); //put it on cooldown
            incr = bar[next].duration; //set advancement (ticks)
            time += incr; //advance time by duration
            damage += stundur > 0 ? bar[next].stundmg : bar[next].damage; //increment damage
            if (STUNS && bar[next].stuns)
                stundur = bar[next].stundur;
            s += next;
        }


        for (Ability a : bar)
            if (a.usedcount > 1)
                used++;

        double dps = damage/time;
        //System.out.println("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps);

        /*if (dps > max) {
            System.out.println("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps+"\nUsed abilities:");
            //System.out.println(dps);
            for (Ability a : bar)
                if (a.used)
                    System.out.println(a);

            return s;
        }
        return "";
        }*/

        return (new Cycle(bar,used,s,STUNS));
    }


}


