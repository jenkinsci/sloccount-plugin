
package hudson.plugins.sloccount.model;

/**
 * Module name is present in the third column of SLOCCount utility output.
 * Modules are often the top level directories. 
 * 
 * @author Michal Turek
 */
public class Module extends FileContainer {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** The module name. */
    private String name;

    /**
     * Constructor.
     * 
     * @param name the module name
     */
    public Module(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
