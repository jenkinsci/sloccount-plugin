package hudson.plugins.sloccount.model;

import java.io.Serializable;

/**
 * Statistic of one language. The class is thread safe.
 * 
 * @author Michal Turek
 */
public class SloccountLanguageStatistics implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** The language name. */
    private final String name;

    /** Number of lines in this language. */
    private final int lineCount;

    /** Number of files containing this language. */
    private final int fileCount;

    /**
     * Constructor initializing members.
     * 
     * @param languageName
     *            the language
     * @param lineCount
     *            number of lines in this language
     * @param fileCount
     *            number of files containing this language
     */
    public SloccountLanguageStatistics(String languageName, int lineCount,
            int fileCount){
        this.name = languageName;
        this.lineCount = lineCount;
        this.fileCount = fileCount;
    }

    /**
     * Get the language name.
     * 
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Get number of lines in this language.
     * 
     * @return the number of lines
     */
    public int getLineCount(){
        return lineCount;
    }

    /**
     * Get number of files containing this language.
     * 
     * @return the number of files
     */
    public int getFileCount(){
        return fileCount;
    }
}
