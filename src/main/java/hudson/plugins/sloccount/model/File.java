package hudson.plugins.sloccount.model;

import hudson.plugins.sloccount.util.StringUtil;
import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class File implements Countable, Serializable {

    private String name;
    private String language;
    private int lineCount;

    public File(String name, String language, int lineCount){
        this.name = name;
        this.language = language;
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

    public void simplifyName(String rootPath){
        this.name = this.name.substring(rootPath.length());
    }
}
