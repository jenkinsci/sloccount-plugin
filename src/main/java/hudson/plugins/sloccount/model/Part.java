
package hudson.plugins.sloccount.model;

/**
 * One of the top level directories that are passed to the SLOCCount
 * utility. It is present in the third column of SLOCCount output.
 * 
 * @author Michal Turek
 */
public class Part extends FileContainer {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** The part name. */
    private String name;

    /**
     * Constructor.
     * 
     * @param name the part name
     */
    public Part(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
