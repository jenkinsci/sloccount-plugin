package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public class File implements Countable {

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
