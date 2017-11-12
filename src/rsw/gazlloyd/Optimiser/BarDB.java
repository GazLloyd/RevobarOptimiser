package rsw.gazlloyd.Optimiser;

import rsw.gazlloyd.Optimiser.util.CombIterator;
import rsw.gazlloyd.Optimiser.util.FileHandler;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 06/07/2016.
 */
public class BarDB {
    public static HashMap<String,String> abilmap = new HashMap<>(), abilmaprev = new HashMap<>();
    public static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"; //jklmnopqrstuvwxyz0123456789";
    private static int i = 0;

    //initialisation
    static {
        setupMap();
    }

    private static String letterAt(int i) {
        try {
            return letters.substring(i,i+1);
        }
        catch(IndexOutOfBoundsException e) {
            return "";
        }
    }

    private static void addAbil(String str) {
        String letter = letterAt(i);
        abilmap.put(letter,str);
        abilmaprev.put(str,letter);
        i++;
    }

    public static void setupMap() {
        // Attack, Strength, Magic, Ranged, Defence, Constitution
        // then alphabetical
        String[] abils = {
                "Backhand",
                "Havoc",
                "Sever",
                "Slice",
                "Smash",
                "Cleave",
                "Decimate",
                "Dismember",
                "Fury",
                "Punish",
                "Chain",
                "Combust",
                "Concentrated Blast",
                "Corruption Blast",
                "Dragon Breath",
                "Impact",
                "Sonic Wave",
                "Wrack",
                "Binding Shot",
                "Corruption Shot",
                "Dazing Shot",
                "Fragmentation Shot",
                "Needle Strike",
                "Ricochet",
                "Snipe",
                "Bash",
                "Sacrifice",
                "Tuska's Wrath",
                "Bash defender",
        };

        for (String str : abils) {
            addAbil(str);
        }
    }

    // turn collection of letters (from combination generator) into arraylist of abilities
    public static ArrayList<String> convert(Collection<String> coll) {
        ArrayList<String> ret = new ArrayList<>(coll.size());
        for (String str : coll) {
            ret.add(abilmap.get(str));
        }
        return ret;
    }

    // turn optimised Bar into arraylist of letters
    public static ArrayList<String> unconvert(Bar bar) {
        ArrayList<String> ret = new ArrayList<>(bar.used.size());
        for (Ability abil : bar.used) {
            ret.add(abilmaprev.get(abil.toString()));
        }
        return ret;
    }

    public static void printMapping() {
        printMapping(System.out);
    }
    public static void printMappingLua() {
        printMappingLua(System.out);
    }

    public static void printMapping(PrintStream out) {
        for (String l : letters.split("")) {
            out.println(l + "\t=\t" + abilmap.get(l));
        }
    }
    public static void printMappingLua(PrintStream out) {
        out.println("return {");
        for (String l : letters.split("")) {
            out.printf("\t[\"%s\"] = \"%s\",%n", l, abilmap.get(l));
        }
        out.println("}");
    }


    FileHandler fh;
    BufferedReader br;
    PrintStream out;
    Logger log = Logger.getAnonymousLogger();

    public BarDB(File fin, File fout) {
        fh = new FileHandler(fin, fout);
        br = fh.br;
        out = fh.out;

        printMappingLua(out);
        printnl();

        process();
        fh.done();
    }
    private void process() {
        String line;
        try {
            while((line = br.readLine()) != null) {
                log.info("Read line " + line + ", working...");
                print("-- " + line);
                optall(Arrays.asList(line.split("")));
                printnl();
                printnl();
            }
        } catch (IOException e) {
            log.severe("IOException when reading file!");
            e.printStackTrace();
            return;
        }
    }



    private void print(String str) {
        out.println(str);
    }
    private void printnl() {
        out.println();
    }


    public ArrayList<String> optone(Collection<String> coll) {
        String[] a = {"a"}; //type array
        return unconvert((Optimiser2.optimise(coll.toString(), convert(coll).toArray(a))).bar);
    }

    public void optall(List<String> lets) {
        Collection<String> coll;
        ArrayList<String> opted;
        if (lets.size() < 2) {
            log.severe("too few abilities chosen");
            return;
        }
        for (int i = 2; i <= lets.size(); i++) {
            for (CombIterator<String> iter = new CombIterator<>(i, lets); iter.hasNext(); ) {
                coll = iter.next();
                opted = optone(coll);
                if (opted.size() == 0) {
                    continue; // skip empty bars
                }
                print(String.format("\t[\"%s\"] = \"%s\",", String.join("", coll), String.join("", opted)));
            }
        }
    }

    public static void main(String[] args) {
        printMapping();
        printMappingLua(); /*
        Optimiser2 opt;
        String[] a = {"a"};
        List<String> strs = Arrays.asList(letters.split(""));
        System.out.println(strs.toString());
        List<String> strs2 = Arrays.asList("A", "B", "C", "D", "I");
        Collection<String> coll;
        for (CombIterator<String> iter = new CombIterator<>(3, strs2); iter.hasNext(); ) {
            coll = iter.next();
            System.out.println(coll);
            System.out.println(convert(coll));
            opt = Optimiser2.optimise(coll.toString(), convert(coll).toArray(a));
            System.out.println(unconvert(opt.bar));
        }*/

        new BarDB(new File("D:\\Gaz\\Docs\\Dropbox\\Raffle entries\\gaz's place to put stuff for no apparent reason\\bardb in.txt"), new File("D:\\Gaz\\Docs\\Dropbox\\Raffle entries\\gaz's place to put stuff for no apparent reason\\bardb out.txt"));

    }


}
