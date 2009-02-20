package hudson.plugins.sloccount.model;

import hudson.FilePath;
import hudson.plugins.sloccount.util.FileFinder;
import hudson.remoting.VirtualChannel;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

/**
 *
 * @author lordofthepigs
 */
public class SloccountParser implements FilePath.FileCallable<SloccountReport> {

    private boolean LOG_ENABLED = false;

    private final String encoding;
    private final String filePattern;
    private PrintStream logger;

    public SloccountParser(String encoding, String filePattern, PrintStream logger){
        this.logger = logger;
        this.filePattern = filePattern;
        this.encoding = encoding;
    }


    public SloccountReport invoke(java.io.File workspace, VirtualChannel channel) throws IOException {
        SloccountReport report = new SloccountReport();
        
        FileFinder finder = new FileFinder(this.filePattern);
        String[] found = finder.find(workspace);

        for(String fileName : found){
            this.parse(workspace, fileName, report);
        }

        report.simplifyNames();
        return report;
    }

    private void parse(java.io.File workspace, String fileName, SloccountReport report) throws IOException {
        java.io.File file = new java.io.File(workspace, fileName);
        InputStreamReader in = new InputStreamReader(new FileInputStream(file), encoding);
        this.parse(in, report);
        in.close();
    }

    private void parse(Reader reader, SloccountReport report) throws IOException {
        BufferedReader in = new BufferedReader(reader);

        String line;
        while((line = in.readLine()) != null){
            this.parseLine(line, report);
        }

        if(LOG_ENABLED){
            this.logger.println("Root folder is: " + report.getRootFolder());
        }
    }

    private void parseLine(String line, SloccountReport report){
        String[] tokens = line.split("\t");

        if(tokens.length != 4){
            // line is not a line count report line, ignore
            if(LOG_ENABLED){
                logger.println("Ignoring line: " + line);
            }
            return;
        }

        if(LOG_ENABLED){
            logger.println("Parsing line: " + line);
        }

        int lineCount = Integer.parseInt(tokens[0]);
        String languageName = tokens[1];
        String filePath = tokens[3];

        if(LOG_ENABLED){
            logger.println("lineCount: " + lineCount);
            logger.println("language : " + languageName);
            logger.println("file     : " + filePath);
        }

        report.add(filePath, languageName, lineCount);
    }
}
