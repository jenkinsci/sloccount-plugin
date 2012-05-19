package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public class Language extends FileContainer {

    private String name;
   
    public Language(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
