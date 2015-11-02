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
    
    /** Comments count in the newer report. */
    private final int commentCount;

    /** Difference of comments count between current and previous report. */
    private final int commentCountDelta;

    /**
     * Constructor.
     * 
     * @param lineCount
     *            lines count in the newer report
     * @param lineCountDelta
     *            difference of lines count between current and previous
     *            report
     * @param fileCount
     *            files count in the newer report
     * @param fileCountDelta
     *            difference of files count between current and previous
     *            report
     * @param commentCount
     *            comments count in the newer report
     * @param commentCountDelta
     *            difference of comments count between current and previous
     *            report
     */
    public SloccountDiff(int lineCount,
            int lineCountDelta, int fileCount, int fileCountDelta, int commentCount, int commentCountDelta) {
        this.lineCount = lineCount;
        this.lineCountDelta = lineCountDelta;
        this.fileCount = fileCount;
        this.fileCountDelta = fileCountDelta;
        this.commentCount = commentCount;
        this.commentCountDelta = commentCountDelta;
    }

    /**
     * Compare two instances using lines count, descendant.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SloccountDiff o) {
        return o.lineCount - lineCount;
    }

    // Solve FindBugs warning "SloccountDiff defines compareTo(SloccountDiff)
    // and uses Object.equals()"
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lineCount;
        return result;
    }

    // Solve FindBugs warning "SloccountDiff defines compareTo(SloccountDiff)
    // and uses Object.equals()"
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SloccountDiff other = (SloccountDiff) obj;
        if (lineCount != other.lineCount)
            return false;
        return true;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getLineCountDelta() {
        return lineCountDelta;
    }
    
    public int getCommentCount() {
        return commentCount;
    }

    public int getCommentCountDelta() {
        return commentCountDelta;
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
    
    public String getCommentCountString() {
        return StringUtil.grouping(commentCount);
    }

    public String getCommentCountDeltaString() {
        if(commentCountDelta == 0) {
            return "";
        }

        // Negative prefix '-' is added automatically
        String result = StringUtil.grouping(commentCountDelta);
        return (commentCountDelta > 0) ? "+" + result : result;
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
