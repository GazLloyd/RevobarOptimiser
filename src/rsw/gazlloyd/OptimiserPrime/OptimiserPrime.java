package rsw.gazlloyd.OptimiserPrime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    }
}
