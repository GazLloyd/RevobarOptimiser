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
        for (Ability ab : a)
            ab.init();
        System.out.println(a.get(0));
    }
}

/*
  {
    "name": "Havoc",
    "duration": 3,
    "cooldown": 17,
    "maxdmg": 1.57
  }
]*/