package rsw.gazlloyd.OptimiserPrime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by gaz-l on 10/11/2017.
 */
public class OptimiserPrime {

    public InputStream getStream() {
        return getClass().getResourceAsStream("/resources/abilities.json");
    }

    public static void main(String[] args) {
        Gson g = new Gson();
        OptimiserPrime o = new OptimiserPrime();
        ArrayList<Ability> a = g.fromJson(new InputStreamReader(o.getStream()), new TypeToken<ArrayList<Ability>>(){}.getType());
        for (Ability ab : a) {
            ab.init();
            System.out.println(ab.toStringFull());
        }

        Revobar b = new Revobar("Slice", "Punish");
        System.out.println(b);
        b = new Revobar("Slice", "Havoc", "Punish");
        System.out.println(b);
        b = new Revobar("Assault", "Slice", "Punish");
        System.out.println(b);
        b = new Revobar("Assault", "Slice", "Havoc", "Punish");
        System.out.println(b);
        b = new Revobar("Berserk", "Assault", "Slice", "Havoc", "Punish");
        System.out.println(b);
        b = new Revobar("Berserk", "Slice", "Punish");
        System.out.println(b);

        System.out.println();
        new BarOpt("Berserk", "Assault", "Slice", "Havoc", "Punish", "Fury");

        System.out.println();
        new BarOpt("Slice", "Havoc", "Backhand", "Sever", "Punish", "Dismember", "Fury", "Decimate");
        System.out.println();


        BarOpt bar = new  BarOpt("Slice", "Havoc", "Sever", "Dismember", "Fury", "Decimate", "Forceful Backhand", "Destroy", "Assault", "Berserk");

        System.out.println(bar.best.adrenOverTime);



    }
}
