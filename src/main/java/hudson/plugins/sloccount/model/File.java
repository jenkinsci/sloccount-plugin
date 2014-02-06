package hudson.plugins.sloccount.model;

import hudson.plugins.sloccount.util.StringUtil;
import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class File implements Countable, Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private String name;
    private final String language;

    /** The part. */
    private final String part;

    private final int lineCount;

    public File(String name, String language, String part, int lineCount){
        this.name = name;
        this.language = language;
        this.part = part;
        this.lineCount = lineCount;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public String getLineCountString() {
        return StringUtil.grouping(getLineCount());
    }
    
    public String getName() {
        return this.name;
    }

    public String getLanguage(){
        return this.language;
    }

    /**
     * Get the part.
     * 
     * @return the part
     */
    public String getPart(){
        return part;
    }

    public void simplifyName(String rootPath){
        this.name = this.name.substring(rootPath.length());
    }
}
