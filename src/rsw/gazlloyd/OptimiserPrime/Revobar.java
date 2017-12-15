package rsw.gazlloyd.OptimiserPrime;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gaz-l on 11/11/2017.
 */
 //temporarily commented out so compilation is successful for Optimiser2/artifacts
public class Revobar {
    public ArrayList<Ability> bar;
    public ArrayList<Ability> usedBar;
    public double revoval;
    boolean revocalced = false;


    public Revobar(String... str) {
        bar = new ArrayList<>();
        for (String s : str) {
            bar.add(Ability.abilities.get(s));
        }
    }

    public Revobar(ArrayList<Ability> b) {
        bar = b;
    }


    public double calcRevo() {
        if (revocalced)
            return revoval;
        double totalDamage = 0;
        double adrenaline = 0;

        //TODO make into settings
        int maximumTicks = 3000;
        boolean stuns = false;

        //make sure everything is reset
        for (Ability a : bar)
            a.reset();

        int totalTicks = 0;
        Ability ability;

        int adrenBuffedTicks = 0, damageBuffedTicks = 0;
        double adrenBuff = 0, damageBuff = 1;
        double buffNextAbil = 1;

        int stunned = 0;


        //main loop
        while (totalTicks < maximumTicks) {
            //reset ability to null (autoattack)
            ability = null;

            //find the next ability to be used
            for (Ability abil : bar) {
                if (abil.canUse(adrenaline)) {
                    ability = abil;
                    break;
                }
            }
            if (ability == null) {
                //TODO support autoattacks
                revocalced = true;
                revoval = -1;
                return -1;
            }

            //perform the ability


            double abilityDamage = ability.damage;
            if (stuns && stunned > 0) {
                abilityDamage = ability.stun_damage;
            }
            if (!ability.is_bleed) {
                abilityDamage *= buffNextAbil * damageBuff;
            }
            buffNextAbil = 1;
            totalDamage += abilityDamage;


            adrenaline = Math.min(Math.max(adrenaline+ability.adrenaline, 0), 100);
            //TODO adrenaline-buffed abilities (potentially a large rewrite to actually model damage distribution)

            totalTicks += ability.duration;


            damageBuff = ability.damageBuff;
            damageBuffedTicks = ability.damageBuffDur;


            ability.putOnCooldown();
            ability.used = true;


            //decrement cooldowns
            for (Ability abil : bar) {
                abil.reduceCooldown(ability.duration);
            }
            damageBuffedTicks = Math.max(0, damageBuffedTicks-ability.duration);
            if (damageBuffedTicks == 0) {
                damageBuff = 1;
            }

        }

        usedBar = new ArrayList<>();
        for (Ability a : bar) {
            if (a.used) {
                usedBar.add(a);
            }
        }

        revoval = totalDamage / totalTicks;
        revocalced = true;
        return revoval;
    }


    @Override
    public String toString() {
        if (!revocalced) calcRevo();
        return usedBar.toString() + "\t" + revoval;
    }

}
