package rsw.gazlloyd.Optimiser.util;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Gareth Lloyd on 07/07/2016.
 */
public class FileHandler {
    public BufferedReader br;
    public PrintStream out;
    public String out_str = "default out stream";
    public File inFile, outFile;
    Logger log = Logger.getAnonymousLogger();

    private void setupInFile(File f) {
        inFile = f;
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
    }
    private void setupOutFile(File f) {
        outFile = f;
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
            }
            log.info("Out stream set to file " + out_str);
        }
    }


    public FileHandler(File inFile, File outFile) {
        setupInFile(inFile);
        setupOutFile(outFile);
    }

    public FileHandler(File inFile) {
        setupInFile(inFile);
    }

    public void done() {
        try {
            br.close();
        } catch (IOException e) {
            log.severe("IO Exception when closing buffered reader!");
            e.printStackTrace();
        }
        out.close();
    }

}
