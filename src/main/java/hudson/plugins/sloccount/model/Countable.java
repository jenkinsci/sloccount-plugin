package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public interface Countable {

    int getLineCount();

    String getLineCountString();
    
    String getName();

}
