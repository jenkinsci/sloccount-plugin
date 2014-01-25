package hudson.plugins.sloccount;

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
     */
    public SloccountDiffLanguage(String name, int lineCount,
            int lineCountDelta, int fileCount, int fileCountDelta) {
        super(lineCount, lineCountDelta, fileCount, fileCountDelta);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
