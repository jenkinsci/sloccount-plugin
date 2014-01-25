package hudson.plugins.sloccount;

import hudson.plugins.sloccount.util.StringUtil;

/**
 * Base class for diff storage.
 * 
 * @author Michal Turek
 */
public abstract class SloccountDiff implements Comparable<SloccountDiff> {
    /** Lines count in the newer report. */
    private final int lineCount;

    /** Difference of lines count between current and previous report. */
    private final int lineCountDelta;

    /** Files count in the newer report. */
    private final int fileCount;

    /** Difference of files count between current and previous report. */
    private final int fileCountDelta;

    /**
     * Constructor.
     * 
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
    public SloccountDiff(int lineCount,
            int lineCountDelta, int fileCount, int fileCountDelta) {
        this.lineCount = lineCount;
        this.lineCountDelta = lineCountDelta;
        this.fileCount = fileCount;
        this.fileCountDelta = fileCountDelta;
    }
    

    /**
     * Compare two instances using lines count, descendant.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SloccountDiff o) {
        return o.lineCount - lineCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getLineCountDelta() {
        return lineCountDelta;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getFileCountDelta() {
        return fileCountDelta;
    }

    public String getLineCountString() {
        return StringUtil.grouping(lineCount);
    }

    public String getLineCountDeltaString() {
        if(lineCountDelta == 0) {
            return "";
        }

        // Negative prefix '-' is added automatically
        String result = StringUtil.grouping(lineCountDelta);
        return (lineCountDelta > 0) ? "+" + result : result;
    }

    public String getFileCountString() {
        return StringUtil.grouping(fileCount);
    }

    public String getFileCountDeltaString() {
        if(fileCountDelta == 0) {
            return "";
        }

        // Negative prefix '-' is added automatically
        String result = StringUtil.grouping(fileCountDelta);
        return (fileCountDelta > 0) ? "+" + result : result;
    }
}
