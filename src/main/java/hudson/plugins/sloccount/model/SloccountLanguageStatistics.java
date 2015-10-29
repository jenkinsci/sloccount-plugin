package hudson.plugins.sloccount.model;

import java.io.Serializable;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Statistic of one language. The class is thread safe.
 * 
 * @author Michal Turek
 */
@ExportedBean
public class SloccountLanguageStatistics implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** The language name. */
    private final String name;

    /** Number of lines in this language. */
    private final int lineCount;

    /** Number of files containing this language. */
    private final int fileCount;
    
    /** Number of comments containing this language. */
    private final int commentCount;

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
            int fileCount, int commentCount){
        this.name = languageName;
        this.lineCount = lineCount;
        this.fileCount = fileCount;
        this.commentCount = commentCount;
    }

    /**
     * Get the language name.
     * 
     * @return the name
     */
    @Exported(name="name")
    public String getName(){
        return name;
    }

    /**
     * Get number of lines in this language.
     * 
     * @return the number of lines
     */
    @Exported(name="lines")
    public int getLineCount(){
        return lineCount;
    }

    /**
     * Get number of files containing this language.
     * 
     * @return the number of files
     */
    @Exported(name="files")
    public int getFileCount(){
        return fileCount;
    }
    
    /**
     * Get number of comments containing this language.
     * 
     * @return the number of comments
     */
    @Exported(name="comments")
    public int getCommentCount(){
        return commentCount;
    }
}
