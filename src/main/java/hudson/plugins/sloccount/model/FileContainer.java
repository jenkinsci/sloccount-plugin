package hudson.plugins.sloccount.model;

import hudson.plugins.sloccount.util.StringUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public abstract class FileContainer implements Countable, Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private Map<String, File> files = new LinkedHashMap<String, File>();
    private int lineCount = 0;
    private int commentCount = 0;

    public File getFile(String name){
        return this.files.get(name);
    }

    public List<File> getFiles(){
        return new ArrayList<File>(this.files.values());
    }

    public int getFileCount(){
        return this.files.size();
    }
    
    public String getFileCountString() {
        return StringUtil.grouping(getFileCount());
    }

    public void addFile(File file){
        this.files.put(file.getName(), file);
        this.lineCount += file.getLineCount();
        this.commentCount += file.getCommentCount();
    }

    public int getLineCount(){
        return this.lineCount;
    }

    public String getLineCountString() {
        return StringUtil.grouping(getLineCount());
    }
    
    public int getCommentCount(){
        return this.commentCount;
    }

    public String getCommentCountString() {
        return StringUtil.grouping(getCommentCount());
    }
}
