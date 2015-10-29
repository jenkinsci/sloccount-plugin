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
    private static final long serialVersionUID = 1L;

    /** The language statistics. */
    private final Map<String, LanguageStatistics> statistics = new HashMap<String, LanguageStatistics>();

    /** The list of files from which the original report was created. */
    private final List<SlaveFile> sourceFiles = new LinkedList<SlaveFile>();

    /**
     * Get the statistics.
     * 
     * @return the statistics
     */
    public SloccountReportStatistics getStatistics(){
        List<SloccountLanguageStatistics> ret = new LinkedList<SloccountLanguageStatistics>();

        for(Map.Entry<String, LanguageStatistics> it : statistics.entrySet()){
            ret.add(new SloccountLanguageStatistics(it.getKey(),
                    it.getValue().numLines, it.getValue().numFiles, it.getValue().numComments));
        }

        return new SloccountReportStatistics(ret);
    }

    /**
     * Get the source files.
     * 
     * @return the source files
     */
    public List<SlaveFile> getSourceFiles(){
        return Collections.unmodifiableList(sourceFiles);
    }

    /**
     * Add a source file.
     * 
     * @param sourceFile
     *            the source file
     */
    public void addSourceFile(File sourceFile){
        sourceFiles.add(new SlaveFile(sourceFile));
    }

    public void add(String filePath, String languageName, String moduleName, int lineCount, int commentCount){
        LanguageStatistics stat = statistics.get(languageName);

        if(stat == null){
            stat = new LanguageStatistics();
            statistics.put(languageName, stat);
        }

        stat.numLines += lineCount;
        stat.numComments += commentCount;
        ++stat.numFiles;
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
        
        /** The number of comments. */
        int numComments = 0;
    }

    /**
     * Helper class to store a file name and an absolute path relative to the
     * slave machine.
     * 
     * @author Michal Turek
     */
    public static class SlaveFile implements Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        /** The file name. */
        private final String name;

        /** The absolute path to the file. */
        private final String absolutePath;

        /**
         * Constructor.
         * 
         * @param file
         *            the file in the file system
         */
        public SlaveFile(File file) {
            this.name = file.getName();
            this.absolutePath = file.getAbsolutePath();
        }

        /**
         * Get the file name.
         * 
         * @return the file name
         */
        public String getName() {
            return name;
        }

        /**
         * Get the absolute path to the file.
         * 
         * @return the absolute path
         */
        public String getAbsolutePath() {
            return absolutePath;
        }
    }
}
