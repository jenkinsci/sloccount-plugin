package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public interface FileFilter {

    public boolean include(File file);
    
}
