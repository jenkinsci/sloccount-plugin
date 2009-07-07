package hudson.plugins.sloccount.model;

import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class Folder extends FileContainer implements Countable, Serializable {

    private String name;

    public Folder(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void simplifyName(String rootPath){
        this.name = this.name.substring(rootPath.length());
    }

    public String getUrlName(){
        return this.name.replace(System.getProperty("file.separator"), "|");
    }
}
