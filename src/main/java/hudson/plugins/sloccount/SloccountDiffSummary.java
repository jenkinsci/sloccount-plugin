package hudson.plugins.sloccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountReportStatistics;

/**
 * Storage for differences between two report statistics.
 * 
 * @author Michal Turek
 */
public class SloccountDiffSummary extends SloccountDiff {
    /** Difference for each language. */
    private final List<SloccountDiffLanguage> languageDiffs;

    /**
     * Constructor.
     * 
     * @param languageDiffs
     *            difference for each language
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
     * @see #getDiffSummary(SloccountReportStatistics, SloccountReportStatistics)
     */
    private SloccountDiffSummary(List<SloccountDiffLanguage> languageDiffs,
            int lineCount, int lineCountDelta, int fileCount, int fileCountDelta, int commentCount, int commentCountDelta) {
        super(lineCount, lineCountDelta, fileCount, fileCountDelta, commentCount, commentCountDelta);

        // No copy, can be called only internally
        this.languageDiffs = languageDiffs;
        Collections.sort(this.languageDiffs);
    }

    /**
     * Compute summary diff from previous and current report statistics.
     * 
     * @param previous
     *            the previous report statistics, may be null
     * @param current
     *            the current report statistics
     * @return the differences
     */
    public static SloccountDiffSummary getDiffSummary(
            SloccountReportStatistics previous,
            SloccountReportStatistics current) {
        if(previous == null) {
            return getDiffSummary(current);
        }

        Set<String> languages = new HashSet<String>();
        languages.addAll(previous.getAllLanguages());
        languages.addAll(current.getAllLanguages());

        List<SloccountDiffLanguage> result = new ArrayList<SloccountDiffLanguage>();
        int lineCount = 0;
        int lineCountDelta = 0;
        int fileCount = 0;
        int fileCountDelta = 0;
        int commentCount = 0;
        int commentCountDelta = 0;

        for(String language: languages) {
            // Quadratic complexity can be optimized, but languages count is small
            SloccountLanguageStatistics curStats = current.getLanguage(language);
            SloccountLanguageStatistics prevStats = previous.getLanguage(language);

            result.add(new SloccountDiffLanguage(curStats.getName(),
                    curStats.getLineCount(),
                    curStats.getLineCount() - prevStats.getLineCount(),
                    curStats.getFileCount(),
                    curStats.getFileCount() - prevStats.getFileCount(),
                    curStats.getCommentCount(),
                    curStats.getCommentCount() - prevStats.getCommentCount()));

            lineCount += curStats.getLineCount();
            lineCountDelta += curStats.getLineCount() - prevStats.getLineCount();
            fileCount += curStats.getFileCount();
            fileCountDelta += curStats.getFileCount() - prevStats.getFileCount();
            commentCount += curStats.getCommentCount();
            commentCountDelta += curStats.getCommentCount() - prevStats.getCommentCount();
        }

        return new SloccountDiffSummary(result, lineCount, lineCountDelta,
                fileCount, fileCountDelta,
                commentCount, commentCountDelta);
    }

    /**
     * Compute summary difference only from current report statistics.
     * 
     * @param current
     *            the current report statistics
     * @return the differences, delta values will be set to zero
     */
    private static SloccountDiffSummary getDiffSummary(SloccountReportStatistics current) {
        if(current == null) {
            return getDiffSummary();
        }

        List<SloccountDiffLanguage> result = new ArrayList<SloccountDiffLanguage>();
        int lineCount = 0;
        int fileCount = 0;
        int commentCount = 0;

        for(SloccountLanguageStatistics language: current.getStatistics()) {
            result.add(new SloccountDiffLanguage(language.getName(),
                    language.getLineCount(), 0, language.getFileCount(), 0, language.getCommentCount(), 0));

            lineCount += language.getLineCount();
            fileCount += language.getFileCount();
            commentCount += language.getCommentCount();
        }

        return new SloccountDiffSummary(result, lineCount, 0, fileCount, 0, commentCount, 0);
    }

    /**
     * Compute summary difference only from current report statistics. Delta
     * values will be set to zero.
     *
     * @return empty object
     */
    private static SloccountDiffSummary getDiffSummary() {
        return new SloccountDiffSummary(
                Collections.<SloccountDiffLanguage>emptyList(), 0, 0, 0, 0, 0, 0);
    }

    /**
     * Get differences for each language.
     * 
     * @return the differences
     */
    public List<SloccountDiffLanguage> getLanguageDiffs() {
        return Collections.unmodifiableList(languageDiffs);
    }
}
