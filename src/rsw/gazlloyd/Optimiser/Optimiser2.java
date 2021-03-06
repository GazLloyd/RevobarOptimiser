package rsw.gazlloyd.Optimiser;

import rsw.gazlloyd.Optimiser.util.PermIterator;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 22/08/2015.
 */
public class Optimiser2 {
    //globals
    public static HashMap<String,Ability> abilities;
    public static int MAX_ABILS = 9, TICKS = 1000, CYCLE_TICKS = 1000, MAX_ITER = 5000000;
    public static boolean STUNS = false, SLAYER = false, FORCED_ENABLED = false;
    public static String FORCED_ABIL = "Sacrifice";
    static Logger log;
    static PrintStream OUT = System.out;

    //init
    static {
        setup();
    }


    //per-optimisation
    public HashMap<String,Boolean> barsdone;
    public String[] abilstr;
    public Ability[] abils;
    public String note;
    public Bar bar;
    public boolean optimised = false;

    public static void setup() {
        log = Logger.getAnonymousLogger();
        abilities = new HashMap<>();
        //attack
        makeAbility("Slice", 5, 3, (0.3+1.2)/2, (0.8+1.46)/2);
        makeAbility("Havoc", 17, 1.57*0.6); //dw
        makeAbility("Backhand", 25, 3, 1*0.6, 5); //no kick included
        makeAbility("Smash", 17, 1.25*0.6); //2h
        makeAbility("Barge", 34, 1.25*0.6); //doesn't work in instances
        makeAbility("Sever", 25, 1.88*0.6);
        //strength
        makeAbility("Punish", 5, 3, 0.94*0.6, 1.88*0.6);
        makeAbility("Kick", 25, 3, 1*0.6, 5); //identical to backhand
        makeAbility("Dismember", 25, 3, (100 * 1 + 88 * (1.88+1)/2)/188, null, null, null, true); //100/188 chance of hitting 100%, 88/188 chance of being uniformly ditributed between 100% and 188%, thus 120.6% average
        makeAbility("Dismember cape", 25, 3, (100 * 1 + 88 * (1.88+1)/2)/188 * 8/5, null, null, null, true); //100/188 chance of hitting 100%, 88/188 chance of being uniformly ditributed between 100% and 188%, thus 120.6% average
        makeAbility("Fury", 9, 6, (0.75+0.82+0.89)*0.6);
        //makeAbility("Fury shorter", 9, 5, (0.75+0.82+0.89)*0.6);
        //makeAbility("Fury shortest", 9, 3, (0.75+0.82)*0.6);
        makeAbility("Cleave", 12, 1.88*0.6); //2h
        makeAbility("Decimate", 12, 1.88*0.6); //dw


        //magic
        makeAbility("Wrack", 5, 3, 0.94*0.6, 1.88*0.6);
        makeAbility("Dragon Breath", 17, 1.128);
        makeAbility("Impact", 25, 3, 0.6, 5);
        makeAbility("Combust", 25, 3, 1.205957, null, null, null, true);
        makeAbility("Chain", 17, 0.6);
        makeAbility("Concentrated Blast", 9, 6, 1.476);
        makeAbility("Sonic Wave", 9, 0.942);
        makeAbility("Corruption Blast", 25, 3, 2.0, null, null, null, true);

        //ranged
        makeAbility("Piercing Shot", 5, 3, 0.94*0.6, 1.88*0.6);
        makeAbility("Snipe", 17, 6, 1.72);
        makeAbility("Dazing Shot", 9, 0.942);
        makeAbility("Binding Shot", 25, 3, 0.6, 5);
        makeAbility("Needle Strike", 9, 3, 1.57*0.6, null, null, 1.07, false);
        makeAbility("Fragmentation Shot", 25, 3, 1.205957, null, null, null, true);
        makeAbility("Ricochet", 17, 0.6);
        makeAbility("Corruption Shot", 25, 3, 2.0, null, null, null, true);

        //defence
        // tier 90 + 99 stats: 864 mh weapon ability damage + 247 from 99 strength + 451 shield armour value + 99 from 99 defence
        // normal ability damage: 864 + 247
        makeAbility("Bash", 25, (864 + 247 + 451 + 99)/(864 + 247) * 0.6);
        // tier 90 + 99 stats: 864 mh weapon ability damage + 245 defender ability damage + 247 from 99 strength + 216 defender armour value + 99 from 99 defence
        // normal ability damage: 864 + 245 + 247
        makeAbility("Bash defender", 25, (864 + 245 + 247 + 216 + 99)/(864 + 245 + 247) * 0.6);
        makeAbility("Anticipation", 40, 0);
        makeAbility("Provoke", 17, 0);
        makeAbility("Freedom", 50, 0);
        //constitution
        makeAbility("Sacrifice", 50, 0.6);
        //if (SLAYER) {
            makeAbility("Tuska's Wrath Slayer", 200, 100*99/1666); // (Slayer)
        //} else {
            makeAbility("Tuska's Wrath", 25, 0.7); // (Non-Slayer)
        //}
    }

    private static void makeAbility(String name, int cooldown, double damage) {
        abilities.put(name, new Ability(name, cooldown, 3, damage));
    }
    private static void makeAbility(String name, int cooldown, int duration, double damage) {
        abilities.put(name, new Ability(name, cooldown, duration, damage));
    }
    private static void makeAbility(String name, int cooldown, int duration, double damage, int stuns) {
        abilities.put(name, new Ability(name, cooldown, duration, damage, stuns));
    }
    private static void makeAbility(String name, int cooldown, int duration, double damage, double stunbuff) {
        abilities.put(name, new Ability(name, cooldown, duration, damage, stunbuff));
    }
    private static void makeAbility(String name, Integer cooldown, Integer duration, Double damage, Integer stundur, Double stundmg, Double nextbuff, boolean isbleed) {
        abilities.put(name, new Ability(name, cooldown, duration, damage, stundur, stundmg, nextbuff, isbleed, 8));
    }
    private static void makeAbility(String name, Integer cooldown, Integer duration, Double damage, Integer stundur, Double stundmg, Double nextbuff, boolean isbleed, int adrenaline) {
        abilities.put(name, new Ability(name, cooldown, duration, damage, stundur, stundmg, nextbuff, isbleed, adrenaline));
    }


    public Optimiser2(String n, String... abilsin) {
        note = n;
        abils = new Ability[abilsin.length];//String[abilsin.length];

        int i = 0;
        for (String s : abilsin) {
            if (abilities.containsKey(s)) {
                if (SLAYER && s.equalsIgnoreCase("Tuska's Wrath"))
                    abils[i++] = abilities.get(s + " Slayer");
                else
                    abils[i++] = abilities.get(s);
            }
        }

        bar = run();
        optimised = true;
    }

    public Bar run() {
        int i = 0, broken = 0, working = 0;
        int[] bar;
        Bar curr = null;
        Bar best = new Bar();
        barsdone = new HashMap<String,Boolean>();
        for(Iterator<int[]> it = new PermIterator(abils.length); it.hasNext() && i < MAX_ITER; i++) {
            //for(Iterator<int[]> it = new SepaPnkIterator(abilities.length, MAX_ABILS); it.hasNext() && i < MAX_ITER; i++) {
            bar = it.next();
            if (!checkbar(bar)) {
            //if (true) {
                curr = calcrevo(reorder(bar), best.val, bar);
                if (curr.val == 0 || curr.err) {
                    broken++;
                }
                else {
                    working++;
                }

                barsdone.put(curr.key(),true);
                if (curr.val - best.val > 0.00000000001) { //if val > best, with a small threshold for rounding issues
                    best = curr;
                } else if (curr.val >= best.val && curr.used.size() < best.used.size()) { //if val>best AND val shorter than best
                    best = curr;
                }
            }
        }
        print(note + "\tBest bar: " + best.toString() + "\t\t" + "AADPT: " + best.val + "\t\t" + "Unique bars: " + barsdone.size() + "; Working: " + working + "; Broken bars: " + broken);
        return best;
    }


    public boolean checkbar(int[] bar) {
        boolean out;
        String b = "";
        for (int i : bar) {
            b += i;
        }
        for (int i = 1; i < bar.length; i++) {
            if (barsdone.containsKey(b.substring(0,i))) {
                return true;
            }
        }
        return false;
    }


    //turn int[] into corresponding Ability[]
    public Ability[] reorder(int[] order) {
        //make sure to create a new array, don't reorder the base array
        Ability[] out = new Ability[order.length];
        for (int i = 0; i < order.length; i++) {
            out[i] = abils[order[i]];
        }

        return out;
    }

    //calculate the damage per tick of the revolution bar given, and only print it if it a new best
    public static Bar calcrevo(Ability[] bar, double max, int[] inbar) {
        int time = 0, incr = 0, stundur=0, adrenaline = 0;
        double damage = 0, nextbuff = 1;
        Ability next;
        boolean forcedabilused = false;
        Bar out = new Bar();

        //reset all ability cooldowns and used status
        for (Ability a : abilities.values())
            a.reset();

        //test for TICKS
        while (time < TICKS) {
            next = null;
            String barstate = "";
            //reduce cooldown of all abilities by duration of previous ability
            for (Ability a : bar) {
                a.reducecooldown(incr);
            }
            stundur = Math.max(0,stundur-incr);


            //for (int i = 0; i < bar.length && i < MAX_ABILS; i++) {
             //   barstate += "[" + bar[i].cd + "]  ";
            //}

            //determine next ability to be used
            //for (Ability a :  bar) {
            for (int i = 0; i < bar.length; i++) {
                if (i < 9 &&  i >= MAX_ABILS) {
                    out.err = true;
                    return out;
                }
                Ability a = bar[i];
                if (a.canUse(adrenaline)) {
                    next = a;
                    a.used = true;
                    break;
                }
            }

            //if there is no next ability, we have a broken revobar
            if (next == null) {
                out.err = true;
                return out;
            }
            if (next.name.equalsIgnoreCase(FORCED_ABIL)) {
                forcedabilused = true;
                //log.info("Used " + FORCED_ABIL + " at t=" + time);
            }

            //bleeds can't get the buff
            if (next.isbleed) {
                nextbuff = 1;
            }

            //print("Used: " + next.name + "\t\t" + barstate);

            //activate the ability
            next.putoncooldown(); //put it on cooldown
            incr = next.duration; //set advancement (ticks)
            time += incr; //advance time by duration
            damage += (stundur > 0 ? next.stundmg : next.damage) * nextbuff; //increment damage
            if (STUNS && next.stuns)
                stundur = next.stundur;

            nextbuff = next.nextbuff;
        }

        double dps = damage/time;
        //print("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps+"  max:"+max);

        if (dps > max) {
            //print("Bar "+Arrays.toString(bar)+" (stuns: "+STUNS+")  damage: "+damage+" ticks "+time+"  DPT: "+dps+"\nUsed abilities:");
            //print(dps);
            String s = "";
            for (int i = 0; i < bar.length; i++)
                if (bar[i].used) {
                    s += bar[i]+",";
                }
            //print(s);
        }

        for (int i = 0; i < bar.length; i++) {
            if (bar[i].used) {
                out.used.add(bar[i]);
                out.usedints.add(inbar[i]);
            }
        }

        out.val = dps;
        if (FORCED_ENABLED && !forcedabilused)
            out.val = 0;
        return out;
    }

    public Cycle cyclerevo(Ability[] bar) {
        String s = "";
        int time = 0, incr = 0, next, used = 0, stundur = 0;
        double damage = 0;

        for (Ability a : abils)
            a.reset();

        //test for TICKS
        while (time < CYCLE_TICKS) {
            next = -1;

            //reduce cooldown of all abilities by duration of previous ability
            for (Ability a : abils)
                a.reducecooldown(incr);
            stundur = Math.max(0, stundur - incr);

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
                //print("not enough abilities provided for a continuous revobar");
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

        double dps = damage / time;
        //print("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps);

        /*if (dps > max) {
            print("Bar "+Arrays.toString(bar)+" damage: "+damage+" ticks "+time+"  DPT: "+dps+"\nUsed abilities:");
            //print(dps);
            for (Ability a : bar)
                if (a.used)
                    print(a);

            return s;
        }
        return "";
        }*/

        return (new Cycle(bar, used, s, STUNS));
    }

    public static Optimiser2 optimise(String note, String... abils) {
        /*log.info("Settings:\nMAX_ABILS = "+MAX_ABILS +
                "\nTICKS = "+TICKS+
                "\nMAX_ITER = "+MAX_ITER+
                "\nSTUNS = "+STUNS+
                "\nFORCE_ENABLED = "+FORCED_ENABLED+
                "\nFORCED_ABIL = "+FORCED_ABIL);*/
        if (abils.length == 0) {
            print("No abilities passed to " + note);
            return null;
        }
        return new Optimiser2(note, abils);
    }

    public static Bar testBar(String... abilsin) {
        Ability[] abils = new Ability[abilsin.length];
        int[] inbar = new int[abilsin.length];
        int i = 0;
        for (String s : abilsin) {
            if (abilities.containsKey(s)) {
                inbar[i] = i;
                abils[i++] = abilities.get(s);
            }
        }
        Bar bar =  calcrevo(abils,0,inbar);
        if (bar.err) {
            print("Bar (" + Arrays.toString(abilsin) + ") does not work");
        } else {
            print("bar: " + Arrays.toString(abilsin) + "\t\t" + "AADPT: " + bar.val);
        }
        return bar;
    }


    public static void nl() {
        OUT.println();
    }
    public static void print(String s) {
        OUT.println(s);
    }

    public static void main(String args[]) {
        setup();
        MAX_ABILS = 9;
        STUNS = false;
        FORCED_ENABLED = false;
        SLAYER = false;
        //new Optimiser2("Slice", "Punish", "Sacrifice");
        //new Optimiser2("Slice", "Punish", "Sacrifice");
        //new Optimiser2("Slice", "Punish");

        /*
            "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury"

            , "Havoc", "Decimate"
            , "Cleave", "Smash"

            , "Barge"

            "Wrack", "Dragon Breath", "Impact", "Combust", "Chain"
            , "Concentrated Blast"
            , "Sonic Wave"
            , "Corruption Blast"

            "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet"
            , "Needle Strike"
            , "Dazing Shot"
            , "Corruption Shot"

            , "Bash"
            , "Anticipation"
            , "Freedom"
            , "Sacrifice"
            , "Tuska's Wrath"
        */
/*
        STUNS = false;
        optimise("Two-handed melee for noobs", "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Smash");
        optimise("Dual-weilded magic for noobs", "Wrack", "Dragon Breath", "Impact", "Combust", "Concentrated Blast");
        optimise("Two-handed magic for noobs", "Wrack", "Dragon Breath", "Impact", "Combust", "Sonic Wave");
        optimise("Dual-wielded melee for noobs", "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Havoc");
        optimise("Two-handed ranged for noobs", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Dazing Shot");
        optimise("One-handed magic with shield for noobs", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain", "Bash");
        optimise("Shieldbow for noobs", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Dazing Shot", "Bash");
        optimise("One-handed melee with shield for noobs", "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Bash");
        optimise("One-handed magic with shield", "Wrack", "Dragon Breath", "Impact", "Combust", "Sacrifice", "Bash");
        nl();
        optimise("Dual-wielded melee for noobs with defender", "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Havoc", "Decimate", "Bash defender");
        optimise("Dual-wielded melee", "Slice", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Havoc", "Decimate");
        optimise("Dual-wielded magic defender", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain", "Concentrated Blast", "Bash defender");
        optimise("Dual-wielded ranged defender", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Needle Strike", "Bash defender");
        optimise("Dual-wielded ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Needle Strike");
        */


        optimise("Two-handed melee", "Slice", "Backhand", "Sever", "Punish", "Dismember cape", "Fury", "Cleave", "Smash");
        optimise("Dual-wielded melee", "Slice", "Backhand", "Sever", "Punish", "Dismember cape", "Fury", "Havoc", "Decimate");
        optimise("One-handed melee", "Slice", "Backhand", "Sever", "Punish", "Dismember cape", "Fury", "Tuska's Wrath");
        optimise("One-handed melee with shield", "Slice", "Backhand", "Sever", "Punish", "Dismember cape", "Fury", "Bash");
        nl();/*
        optimise("Two-handed magic", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain", "Sonic Wave");
        optimise("Dual-wielded magic", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain", "Concentrated Blast");
        optimise("One-handed magic", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain");
        optimise("One-handed magic with shield", "Wrack", "Dragon Breath", "Impact", "Combust", "Chain", "Bash");
        nl();
        optimise("Two-handed ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Dazing Shot");
        optimise("Dual-wielded ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Needle Strike");
        optimise("One-handed ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet");
        optimise("One-handed ranged with shield", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Bash");
        optimise("Shieldbow", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Dazing Shot", "Bash");
        nl();
        */
        //testBar("Dragon Breath", "Combust", "Concentrated Blast", "Impact", "Chain");
        //testBar("Combust", "Dragon Breath", "Concentrated Blast", "Chain");
        FORCED_ENABLED = false;
        FORCED_ABIL = "Sacrifice";
        MAX_ABILS = 9;
        TICKS = 3000;
        //optimise("DW Mage no stun 5 abils", "Wrack", "Dragon Breath", "Combust", "Chain", "Concentrated Blast");
        //optimise("DW Mage no stun 5 abils, tw", "Wrack", "Dragon Breath", "Combust", "Chain", "Concentrated Blast", "Tuska's Wrath");
        //optimise("DW Mage no stun 5 abils, corr", "Wrack", "Dragon Breath", "Combust", "Chain", "Concentrated Blast", "Tuska's Wrath", "Corruption Blast");
        SLAYER = true;
        //optimise("Dual-weilded ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Needle Strike");
        //optimise("Dual-weilded ranged", "Piercing Shot", "Snipe", "Binding Shot", "Fragmentation Shot", "Ricochet", "Needle Strike", "Corruption Shot", "Tuska's Wrath");
        //testBar("Corruption Shot", "Needle Strike", "Snipe", "Tuska's Wrath Slayer", "Ricochet", "Fragmentation Shot", "Binding Shot", "Piercing Shot");
        //testBar("Corruption Shot","Needle Strike","Snipe","Fragmentation Shot","Binding Shot","Tuska's Wrath Slayer","Ricochet");
    }

}
