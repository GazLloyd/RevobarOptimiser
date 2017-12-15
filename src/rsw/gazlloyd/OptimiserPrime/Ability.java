package rsw.gazlloyd.OptimiserPrime;

import java.util.HashMap;

/**
 * Created by gaz-l on 10/11/2017.
 */
public class Ability {
    public enum AbilityType {
        BASIC(8),
        THRESHOLD(-15),
        ULTIMATE(-100),
        SPECIAL(0);

        public int adren;
        AbilityType(int adren) {
            this.adren = adren;
        }

    }

    public static HashMap<String,Ability> abilities = new HashMap<>();


    public String name = "", type = "";
    public AbilityType abilityType;
    public int duration, cooldown, adrenaline, stun_duration = 0, damageBuffDur = 0;
    public double damage = 0, stun_damage = 0, mindmg = -1, maxdmg = -1, stnmin = -1, stnmax = 0, nextBuff = 1, damageBuff = 1;
    public boolean is_bleed = false;
    int cd = 0, usedcount = 0;
    boolean used = false;

    public void init() {
        if (mindmg == -1) {
            damage = maxdmg * 0.6;
        } else {
            damage = (mindmg + maxdmg ) / 2;
        }

        if (stnmax > 0) {
            if (stnmin == -1) {
                stun_damage = stnmax * 0.6;
            } else {
                stun_damage = ( stnmax + stnmin ) / 2;
            }
        }
        if (type.equalsIgnoreCase("basic")) {
            abilityType = AbilityType.BASIC;
            adrenaline = abilityType.adren;
        }
        else if (type.equalsIgnoreCase("threshold")) {
            abilityType = AbilityType.THRESHOLD;
            adrenaline = abilityType.adren;
        }
        else if (type.equalsIgnoreCase("ultimate")) {
            abilityType = AbilityType.ULTIMATE;
            adrenaline = abilityType.adren;
        }
        else if (type.equalsIgnoreCase("special"))
            abilityType = AbilityType.SPECIAL;
        else {
            abilityType = AbilityType.BASIC;
            adrenaline = abilityType.adren;
        }

        abilities.put(name, this);
    }

    public void reset() {
        cd = 0;
        usedcount = 0;
        used = false;
    }

    public boolean canUse(double adren) {
        return cd == 0 && (
                ( abilityType == AbilityType.BASIC ) ||
                ( abilityType == AbilityType.THRESHOLD && adren > 50) ||
                ( abilityType == AbilityType.ULTIMATE && adren == 100 )
                );
    }

    public void putOnCooldown() {
        cd = cooldown;
    }

    public void reduceCooldown(int v) {
        cd = Math.max(0, cd - v);
    }

    @Override
    public String toString() {
        return name;
    }
    public String toStringFull() {
        return String.format("%s\t%s\tDmg: %s\tCd: %s\tDur: %s\tAdren: %s\tStundur: %s\tStundmg: %s", name, abilityType, damage, cooldown, duration, adrenaline, stun_duration, stun_damage);
    }
}
