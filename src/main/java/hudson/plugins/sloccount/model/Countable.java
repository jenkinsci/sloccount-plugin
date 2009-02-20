package hudson.plugins.sloccount.model;

/**
 *
 * @author lordofthepigs
 */
public interface Countable {

    public int getLineCount();

    public String getName();

}
