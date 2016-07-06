package rsw.gazlloyd.Optimiser;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 22/08/2015.
 */
public class CmdOptimiser {
    public static Logger log;
    private static CommandLine cmd;
    public static void main (String[] args) {
        log = Logger.getAnonymousLogger();

        Options opt = new Options();

        //single bar optimisation
        opt.addOption("b", true, "Abils to optimise");
        opt.addOption("t", true, "Test a single bar's AADPT");

        opt.addOption("stun", false, "Stuns");
        opt.addOption("force", true, "Ability to force");
        opt.addOption("slay", false, "Slayer");
        opt.addOption("tick", true, "Number of simulation ticks");
        opt.addOption("max", true, "Maximum number of abilities");
        opt.addOption("iter", true, "Maximum number of iterations");

        //bulk testing
        opt.addOption("f", true, "Text file to load bars from");
        opt.addOption("page", true, "Page to build");
        opt.addOption("o", true, "Output filename");

        CommandLineParser cmdp = new BasicParser();
        try {
            cmd = cmdp.parse(opt, args);
        } catch (ParseException e) {
            log.severe("Error parsing options!");
            e.printStackTrace();
            System.exit(1);
            return; //tfw 'cmd might not have been initialised' without this
        }

        //single bar test
        if (cmd.hasOption("t")) {
            test(cmd.getOptionValue("t"), cmd.hasOption("stun"), getIntOpt("tick"));
        }
        //single bar optimise
        else if (cmd.hasOption("b")) {
            optimise(cmd.getOptionValue("b"), cmd.hasOption("stun"), getIntOpt("tick"), getIntOpt("iter"), cmd.getOptionValue("force"), getIntOpt("max"));
        }
        //bulk optimise
        else if (cmd.hasOption("f")) {
            File out = null;
            if (cmd.hasOption("o")) {
                out = new File(cmd.getOptionValue("o"));
            }
            bulk(new File(cmd.getOptionValue("f")), out);
        }
        else if (cmd.hasOption("page")) {
            new PageBuild(new File(cmd.getOptionValue("page")), new File(cmd.getOptionValue("o")));
        }
        else {
            log.severe("You didn't specify what to do! Use -t <bar> to find the AADPT of that bar, -b <abils> to optimise a single bar or -f <file> to bulk-optimise!");
        }
    }

    public static void bulk(File in, File out) {
        Optimiser2.setup();
        PrintStream outstream = System.out;
        BufferedReader r = null;

        if (in != null && in.exists()) {
            try {
                r = new BufferedReader(new FileReader(in));
            } catch(Exception e) {
                log.severe("Couldn't open input file! Make sure the file exists and can be read!");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            log.severe("Input file "+in+" does not exist!");
            System.exit(1);
        }

        if (out != null && !out.exists()) {
            if (out.getParentFile().exists()) {
                try {
                    out.createNewFile();
                } catch (Exception e) {
                    log.severe("Couldn't create output file!");
                    e.printStackTrace();
                }
            }
        }
        if (out == null || !out.exists()) {
            log.info("Out stream set to console, brace for spam!");
        } else {
            try {
                outstream = new PrintStream(out);
            } catch (Exception e) {
                log.severe("Couldn't create output stream! Make sure the file exists and can be written to! Writing to console instead.");
                e.printStackTrace();
                outstream = System.out;
            }
            log.info("Out stream set to file " + out);
        }

        Optimiser2.OUT = outstream;
        String line;
        try {
            while ((line = r.readLine()) != null) {
                String[] s = line.split("\t");
                if (line.equalsIgnoreCase("")) {
                    //do nothing
                } else if (s[0].equalsIgnoreCase("nl")) {
                    outstream.println();
                } else if (s[0].equalsIgnoreCase("SET")) {
                    int i;
                    if (s[1].equalsIgnoreCase("stuns")) {
                        Optimiser2.STUNS = Boolean.parseBoolean(s[2]);
                        outstream.println("SET stuns to "+s[2]);
                    }
                    else if (s[1].equalsIgnoreCase("slayer")) {
                        Optimiser2.SLAYER = Boolean.parseBoolean(s[2]);
                        outstream.println("SET slayer to "+s[2]);
                    }
                    else if (s[1].equalsIgnoreCase("ticks")) {
                        if ((i = getInt(s[2])) > 0) {
                            Optimiser.TICKS = i;
                            outstream.println("SET ticks to "+s[2]);
                        }
                    } else if (s[1].equalsIgnoreCase("iter")) {
                        if ((i = getInt(s[2])) > 0) {
                            Optimiser.MAX_ITER = i;
                            outstream.println("SET iter to "+s[2]);
                        }
                    } else if (s[1].equalsIgnoreCase("max")) {
                        if ((i = getInt(s[2])) > 0) {
                            Optimiser.MAX_ABILS = i;
                            outstream.println("SET max to "+s[2]);
                        }
                    } else if (s[1].equalsIgnoreCase("force")) {
                        if (s[2].equalsIgnoreCase("off")) {
                            Optimiser2.FORCED_ENABLED = false;
                            outstream.println("SET force to off");
                        } else {
                            Optimiser2.FORCED_ENABLED = true;
                            Optimiser2.FORCED_ABIL = s[2];
                            outstream.println("SET force to "+s[2]);
                        }
                    }

                } else {
                    //log.info(Arrays.toString(s[1].split(",")));
                    Optimiser2.optimise(s[0], s[1].split(","));
                }
            }
        } catch (IOException e) {
            log.severe("IOException when reading file!");
            e.printStackTrace();
            return;
        }
        outstream.flush();
        outstream.close();
        log.info("Done!");

    }


    public static void optimise(String bar, boolean stuns, int ticks, int iter, String forced, int max) {
        log.info("Entering optimise, params:\nbar: " + bar + "\nstuns: " + stuns + "\nticks: " + ticks + "\niter: " + iter + "\nforced: " + forced + "\nmax: " + max);
        Optimiser2.setup();
        if (ticks > 0) {
            Optimiser2.TICKS = ticks;
        }
        if (iter > 0) {
            Optimiser2.MAX_ITER = iter;
        }
        if (max > 0) {
            Optimiser2.MAX_ABILS = max;
        }
        if (forced != null) {
            Optimiser2.FORCED_ENABLED = true;
            Optimiser2.FORCED_ABIL = forced;
        }
        Optimiser2.STUNS = stuns;

        Optimiser2.optimise("Input abilities: " + bar, bar.split(","));
    }

    public static void test(String bar, boolean stuns, int ticks) {
        Optimiser2.setup();
        if (ticks > 0)
            Optimiser2.TICKS = ticks;
        Optimiser2.STUNS = stuns;
        Optimiser2.testBar(bar.split(","));
    }

    private static int getIntOpt(String opt) {
        int ret = 0;
        if (cmd.hasOption(opt)) {
            ret = getInt(cmd.getOptionValue(opt));
        }
        return ret;
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
