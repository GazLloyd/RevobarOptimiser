package rsw.gazlloyd.Optimiser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Gareth Lloyd on 12/08/2015.
 */ //ability class
public class Ability {
    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public String name;
    public int cooldown; //in ticks
    public int duration; //in tic2ks
    public double damage; //average - usually (max+0.2*max)/2==0.6*max
    public int adrenaline = 8;
    public int cd = 0;
    public ImageIcon img;
    boolean used = false;
    int usedcount = 0;
    boolean stuns = false;
    int stundur = 0;
    double stundmg = 0;
    double nextbuff = 1;
    boolean isbleed = false;

    public Ability(String name, Integer cooldown, Integer duration, Double damage, Integer stundur, Double stundmg, Double nextbuff, boolean isbleed, int adrenaline) {
        this(name, cooldown, duration, damage);
        if (stundur != null)
            this.stundur = stundur;
        if (stundmg != null)
            this.stundmg = stundmg;
        if (nextbuff != null)
            this.nextbuff = nextbuff;
        this.isbleed = isbleed;
        this.adrenaline = adrenaline;
    }

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

    //nextbuff
    public Ability(String name, int cooldown, int duration, double damage, boolean nb, double nextBuff) {
        this(name,cooldown,duration,damage);
        this.nextbuff = nextBuff;
    }

    //basic
    public Ability(String name, int cooldown, int duration, double damage) {
        this.name = name;
        this.cooldown = cooldown;
        this.duration = duration;
        this.damage = damage;
        this.stundmg = damage; //gets overwritten in other constructors

        this.img = createImageIcon("/resources/"+name+".png", name);
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

    public boolean canUse(int adren) {
        if (cd > 0)
            return false;
        if (adrenaline > 0)
            return true;
        if (adrenaline == -15 && adren > 50)
            return true;
        if (adrenaline == -100 && adren == 100)
            return true;
        return false;
    }


    @Override
    public String toString() {
        return name;
    }


    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
