package hudson.plugins.sloccount.model;

import hudson.plugins.sloccount.util.HtmlUtil;

/**
 *
 * @author lordofthepigs
 */
public class Language extends FileContainer {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private String name;
   
    public Language(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getUrlName() {
        return HtmlUtil.urlEncode(name);
    }
}
