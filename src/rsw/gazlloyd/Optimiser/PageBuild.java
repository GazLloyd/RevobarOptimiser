package rsw.gazlloyd.Optimiser;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 17/12/2015.
 */
public class PageBuild {

    BufferedReader br;
    PrintStream out;
    String out_str;
    Logger log = Logger.getAnonymousLogger();

    public PageBuild(File inFile, File outFile) {
        if (inFile != null && inFile.exists()) {
            try {
                br = new BufferedReader(new FileReader(inFile));
            } catch(Exception e) {
                log.severe("Couldn't open input file! Make sure the file exists and can be read!");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            log.severe("Input file "+inFile+" does not exist!");
            System.exit(1);
        }

        if (outFile != null && !outFile.exists()) {
            if (outFile.getParentFile().exists()) {
                try {
                    outFile.createNewFile();
                } catch (Exception e) {
                    log.severe("Couldn't create output file!");
                    e.printStackTrace();
                }
            }
        }
        if (outFile == null || !outFile.exists()) {
            log.severe("Out file doesn't exist!");
            System.exit(1);
        } else {
            try {
                out = new PrintStream(outFile);
                out_str = outFile.getPath();
            } catch (Exception e) {
                log.severe("Couldn't create output stream! Make sure the file exists and can be written to! Writing to console instead.");
                e.printStackTrace();
                out = System.out;
                out_str = "default out stream";
            }
            log.info("Out stream set to file " + out);
        }

        Optimiser2.setup();

        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@@")) {
                    String[] s = line.split("\t");
                    if (s[0].equalsIgnoreCase("@@nl")) {
                    } else if (s[0].equalsIgnoreCase("@@SET")) {
                        int i;
                        if (s[1].equalsIgnoreCase("stuns")) {
                            Optimiser2.STUNS = Boolean.parseBoolean(s[2]);
                            log.info("SET stuns to " + s[2]);
                        } else if (s[1].equalsIgnoreCase("slayer")) {
                            Optimiser2.SLAYER = Boolean.parseBoolean(s[2]);
                            log.info("SET slayer to " + s[2]);
                        } else if (s[1].equalsIgnoreCase("ticks")) {
                            if ((i = getInt(s[2])) > 0) {
                                Optimiser.TICKS = i;
                                log.info("SET ticks to " + s[2]);
                            }
                        } else if (s[1].equalsIgnoreCase("iter")) {
                            if ((i = getInt(s[2])) > 0) {
                                Optimiser.MAX_ITER = i;
                                log.info("SET iter to " + s[2]);
                            }
                        } else if (s[1].equalsIgnoreCase("max")) {
                            if ((i = getInt(s[2])) > 0) {
                                Optimiser.MAX_ABILS = i;
                                log.info("SET max to " + s[2]);
                            }
                        } else if (s[1].equalsIgnoreCase("force")) {
                            if (s[2].equalsIgnoreCase("off")) {
                                Optimiser2.FORCED_ENABLED = false;
                                log.info("SET force to off");
                            } else {
                                Optimiser2.FORCED_ENABLED = true;
                                Optimiser2.FORCED_ABIL = s[2];
                                log.info("SET force to " + s[2]);
                            }
                        }

                    } else if (s[0].equalsIgnoreCase("@@optimise")) {
                        Optimiser2 opt = Optimiser2.optimise(s[1], s[4].split(","));
                        if (opt.bar.val > 0) {
                            out.println("{{Revolution AADPT row|cbclass="+s[2]+"|hand="+s[3]+"|bar="+opt.bar.toString()+"|note="+s[1]+"}}");
                        } else {
                            out.println("{{Revolution AADPT NA|"+s[1]+"}}");
                        }
                    }
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) {
            log.severe("IOException when reading file!");
            e.printStackTrace();
            return;
        }
        out.flush();
        out.close();
        log.info("Done!");
    }
    private static int getInt(String val) {
        int ret;
        try {
            ret = Integer.parseInt(val);
        }
        catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }
}
