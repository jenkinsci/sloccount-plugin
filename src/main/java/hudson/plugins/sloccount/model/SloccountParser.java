package hudson.plugins.sloccount.model;

import hudson.FilePath;
import hudson.plugins.sloccount.model.cloc.ClocReport;
import hudson.plugins.sloccount.util.FileFinder;
import hudson.remoting.VirtualChannel;

import javax.xml.bind.JAXBException;
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
public class SloccountParser implements
        FilePath.FileCallable<SloccountPublisherReport> {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    private static final boolean LOG_ENABLED = false;

    private final String encoding;
    private final String filePattern;
    private transient PrintStream logger = null;
    private final boolean commentIsCode;

    public SloccountParser(String encoding, String filePattern, PrintStream logger,
                           boolean commentIsCode) {
        this.logger = logger;
        this.filePattern = filePattern;
        this.encoding = encoding;
        this.commentIsCode = commentIsCode;
    }


    public SloccountPublisherReport invoke(java.io.File workspace, VirtualChannel channel) throws IOException {
        SloccountPublisherReport report = new SloccountPublisherReport();

        FileFinder finder = new FileFinder(this.filePattern);
        String[] found = finder.find(workspace);

        for(String fileName : found){
            this.parse(new java.io.File(workspace, fileName), report);
            report.addSourceFile(new java.io.File(workspace, fileName));
        }

        return report;
    }

    /**
     * Parse a list of input files. All errors are silently ignored.
     *
     * @param files
     *            the files
     * @return the content of the parsed files in form of a report
     */
    public SloccountReport parseFiles(java.io.File[] files) {
        SloccountReport report = new SloccountReport();

        for (java.io.File file : files) {
            try {
                parse(file, report);
            } catch (IOException e) {
                // Silently ignore, there is still a possibility that other
                // files can be parsed successfully
            }
        }

        report.simplifyNames();
        return report;
    }

    private void parse(java.io.File file, SloccountReportInterface report) throws IOException {
        try {
            // Try cloc report file first, XML has precise structure
            ClocReport.parse(file).toSloccountReport(report, commentIsCode);
        } catch (JAXBException e) {
            if(LOG_ENABLED && (this.logger != null)){
                this.logger.println("Parsing of cloc format unsuccessful, trying SLOCCount format: " + e);
            }

            // Try SLOCCount report file
            InputStreamReader in = null;

            try {
                in = new InputStreamReader(new FileInputStream(file), encoding);
                this.parse(in, report);
            } finally {
                if(in != null) {
                    in.close();
                }
            }
        }
    }

    private void parse(Reader reader, SloccountReportInterface report) throws IOException {
        BufferedReader in = new BufferedReader(reader);

        String line;
        while((line = in.readLine()) != null){
            this.parseLine(line, report);
        }

        if(LOG_ENABLED && (this.logger != null)){
            this.logger.println("Root folder is: " + report.getRootFolder());
        }
    }

    private void parseLine(String line, SloccountReportInterface report){
        String[] tokens = line.split("\t");

        if(tokens.length != 4){
            // line is not a line count report line, ignore
            if(LOG_ENABLED && (this.logger != null)){
                logger.println("Ignoring line: " + line);
            }
            return;
        }

        if(LOG_ENABLED && (this.logger != null)){
            logger.println("Parsing line: " + line);
        }

        int lineCount = Integer.parseInt(tokens[0]);
        String languageName = tokens[1];
        String moduleName = tokens[2];
        String filePath = tokens[3];

        if(LOG_ENABLED && (this.logger != null)){
            logger.println("lineCount: " + lineCount);
            logger.println("language : " + languageName);
            logger.println("file : " + filePath);
            logger.println("module : " + moduleName);
        }

        report.add(filePath, languageName, moduleName, lineCount, 0);
    }
}