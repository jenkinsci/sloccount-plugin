package hudson.plugins.sloccount.model;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The publisher result that is transfered from slave computer to master.
 * 
 * @author Michal Turek
 */
public class SloccountPublisherReport implements Serializable,
        SloccountReportInterface {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** The language statistics. */
    private final Map<String, LanguageStatistics> statistics = new HashMap<String, LanguageStatistics>();

    /** The list of files from which the original report was created. */
    private final List<File> sourceFiles = new LinkedList<File>();

    /**
     * Get the statistics.
     * 
     * @return the statistics
     */
    public List<SloccountLanguageStatistics> getStatistics(){
        List<SloccountLanguageStatistics> ret = new LinkedList<SloccountLanguageStatistics>();

        for(Map.Entry<String, LanguageStatistics> it : statistics.entrySet()){
            ret.add(new SloccountLanguageStatistics(it.getKey(),
                    it.getValue().numLines, it.getValue().numFiles));
        }

        return ret;
    }

    /**
     * Get the source files.
     * 
     * @return the source files
     */
    public List<File> getSourceFiles(){
        return Collections.unmodifiableList(sourceFiles);
    }

    public void addSourceFile(File sourceFile){
        sourceFiles.add(sourceFile);
    }

    public void add(String filePath, String languageName, int lineCount){
        LanguageStatistics stat = statistics.get(languageName);

        if(stat == null){
            stat = new LanguageStatistics();
            statistics.put(languageName, stat);
        }

        stat.numLines += lineCount;
        ++stat.numFiles;
    }

    public void simplifyNames(){
        // Empty, method is not needed in this class
    }

    public String getRootFolder(){
        // Empty, method is not needed in this class
        return "";
    }

    /**
     * Helper class to store language statistics.
     * 
     * @author Michal Turek
     */
    public static class LanguageStatistics implements Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        /** The number of lines. */
        int numLines = 0;

        /** The number of files. */
        int numFiles = 0;
    }
}
