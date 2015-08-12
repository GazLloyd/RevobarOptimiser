package Optimiser;

/**
 * Created by Gareth Lloyd on 12/08/2015.
 */ //ability class
class Ability {
    public String name;
    public int cooldown; //in ticks
    public int duration; //in tic2ks
    public double damage; //average - usually (max+0.2*max)/2==0.6*max
    public int cd = 0;
    boolean used = false;
    int usedcount = 0;
    boolean stuns = false;
    int stundur = 0;
    double stundmg = 0;

    //full constructor, with dur and buff
    public Ability(String name, int cooldown, int duration, double damage, int stundur, double stundmg) {
        this(name,cooldown,duration,damage,stundur);
        this.stundmg = stundmg;

    }

    //stundur
    public Ability(String name, int cooldown, int duration, double damage, int stundur) {
        this(name,cooldown,duration,damage);
        this.stundur = stundur;
        this.stuns = true;
    }

    //buff
    public Ability(String name, int cooldown, int duration, double damage, double stundmg) {
        this(name,cooldown,duration,damage);
        this.stundmg = stundmg;
    }

    //basic
    public Ability(String name, int cooldown, int duration, double damage) {
        this.name = name;
        this.cooldown = cooldown;
        this.duration = duration;
        this.damage = damage;
        this.stundmg = damage; //gets overwritten in other constructors
    }

    public void putoncooldown() {
        cd = cooldown;
    }

    public void reducecooldown(int c) {
        cd = Math.max(0,cd-c);
    }

    public void reset() {
        cd = 0;
        used = false;
        usedcount = 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
