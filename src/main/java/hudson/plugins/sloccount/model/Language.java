package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public class Language extends FileContainer implements Countable {

    private String name;
   
    public Language(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
