package rsw.gazlloyd.bestiary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 15/06/2016.
 */
public class bestiarytest {

    static String test_url = "http://services.runescape.com/m=itemdb_rs/bestiary/beastData.json?beastid=89";
    static String base_url = "http://services.runescape.com/m=itemdb_rs/bestiary/beastData.json?beastid=";
    static File outfile = new File("D:\\Gaz\\bestiary.json");
    static URL url;
    static HttpURLConnection req;
    static BufferedReader br;
    static BufferedWriter bw;
    static Gson gson;
    static Logger log = Logger.getAnonymousLogger();

    public static void setup() {
        log.info("Setting up GSON");
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }



    // combine methods for simplicity
    private static void getandwrite(int i) {
        writejson(i, getjson(i));
        log.info("Wrote JSON for id " + i);
    }

    // fetch the JSON from the url; return the FullMob
    private static Mob getjson(int i) {
        Mob mob;
        try {
            url = new URL(base_url + i);
            req = (HttpURLConnection) url.openConnection();
            req.connect();

            mob = gson.fromJson(new InputStreamReader((InputStream) req.getContent()), FullMob.class);
        } catch (MalformedURLException e) {
            log.severe("Malformed URL exception for id " + i);
            e.printStackTrace();
            return new NullMob(i);
        } catch (IOException e) {
            log.severe("IO Exception when fetching JSON for id " + i);
            e.printStackTrace();
            return new NullMob(i);
        } catch (Exception e) {
            log.severe("Generic other exception when fetching JSON for id " + i);
            e.printStackTrace();
            return new NullMob(i);
        }

        if (mob == null) {
            return new NullMob(i);
        }
        return mob;
    }

    // write the Mob to json in a file
    // passing in i for diagnostics
    private static void writejson(int i, Mob mob) {
        try {
            String str = gson.toJson(mob);
            bw.write(str);
            bw.write(",");
            bw.newLine();
        }
        catch (IOException e) {
            log.severe("IO Exception when writing JSON for id " + i);
            e.printStackTrace();
            return;
        }
        catch (Exception e) {
            log.severe("Generic other exception when writing JSON for id " + i);
            e.printStackTrace();
            return;
        }

    }

    private static void flush(int i) {
        try {
            bw.flush();
        }
        catch (IOException e) {
            log.severe("IO exception when flushing stream at id "+i);
            e.printStackTrace();
        }
        catch (Exception e) {
            log.severe("Generic other exception when flushing stream at id "+i);
            e.printStackTrace();
        }
    }

    // gets data for ids from START to END (both inclusive)
    public static void run(int start, int end) {
        log.info("Running for ids " + start + " to " + end);
        //make the file (overwrite anything already there)
        try {
            outfile.createNewFile();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile)));
            log.info("Created file and opened stream");
            bw.write("["); //construct an array around the entire list of objects
            bw.newLine();
        }
        catch (IOException e) {
            log.severe("IO exception when creating/opening file");
            e.printStackTrace();
            return;
        }

        // loop of all the id
        for (int i = start; i<=end; i++) {
            getandwrite(i);
            // wait a second to try to avoid ratelimits (if there are any)
            if (i % 200 == 0) {
                flush(i);
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                // don't care if interrupted, just have a warn
                log.warning("Sleeping interrupted at i=" + i);
            }
        }

        // finish off the file
        try {
            bw.newLine();
            bw.write("]"); //close that array
            bw.close();
        }
        catch (IOException e) {
            log.severe("IO exception when closing file");
            e.printStackTrace();
            return;
        }

        log.info("Done!");

    }

    private static void consoletest(int i) {
        log.info("Running console test for id "+i);
        Mob test = getjson(i);
        System.out.println(test);
        System.out.println(gson.toJson(test));
    }


    public static void main(String args[]) {
        // TODO handling for ints passed via main; make into runnable jar
        setup();

        run(20001, 24000);
        //consoletest(14416);

        //System.out.println(gson.toJson("hello it's me"));
    }

}