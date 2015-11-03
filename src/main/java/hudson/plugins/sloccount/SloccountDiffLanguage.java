package hudson.plugins.sloccount;

import hudson.plugins.sloccount.util.HtmlUtil;

/**
 * Storage for differences of a language between two reports.
 * 
 * @author Michal Turek
 */
public class SloccountDiffLanguage extends SloccountDiff {
    /** Language name. */
    private final String name;

    /**
     * Constructor.
     * 
     * @param name
     *            language name
     * @param lineCount
     *            lines count in the newer report
     * @param lineCountDelta
     *            difference of lines count between current and previous
     *            report
     * @param fileCount files
     *            count in the newer report
     * @param fileCountDelta
     *            difference of files count between current and previous
     *            report
     * @param commentCount
     *            comments count in the newer report
     * @param commentCountDelta
     *            difference of comments count between current and previous
     *            report
     */
    public SloccountDiffLanguage(String name, int lineCount,
            int lineCountDelta, int fileCount, int fileCountDelta, int commentCount, int commentCountDelta) {
        super(lineCount, lineCountDelta, fileCount, fileCountDelta, commentCount, commentCountDelta);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
    	return HtmlUtil.urlEncode(name);
    }
}
