package hudson.plugins.sloccount.model;

import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class Language extends FileContainer implements Countable, Serializable {

    private String name;
   
    public Language(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
