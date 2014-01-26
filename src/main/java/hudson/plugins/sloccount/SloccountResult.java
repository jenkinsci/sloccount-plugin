package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;
import hudson.plugins.sloccount.model.File;
import hudson.plugins.sloccount.model.FileFilter;
import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.model.SloccountReportStatistics;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lordofthepigs
 */
public class SloccountResult implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private SloccountReport report;
    private final AbstractBuild<?,?> owner;

    /** The statistics. */
    private SloccountReportStatistics statistics;

    /** The encoding that was active at the time of the build. */
    private final String encoding;

    public SloccountResult(SloccountReportStatistics statistics, String encoding,
            SloccountReport report, AbstractBuild<?,?> owner){
        this.statistics = statistics;
        this.encoding = encoding;
        this.report = report;
        this.owner = owner;
    }

    public SloccountReport getReport() {
        lazyLoad();
        return report;
    }

    public AbstractBuild<?,?> getOwner() {
        return owner;
    }

    /**
     * Get the statistics.
     * 
     * @return the statistics, always non-null value
     */
    public SloccountReportStatistics getStatistics() {
        convertLegacyData();
        return statistics;
    }

    /**
     * Convert legacy data in format of sloccount plugin version 1.10
     * to the new one that uses statistics.
     * 
     * If statistics are null for any reason, the object will be created.
     * Statistics will be always non-null after this method is called (side
     * effect).
     */
    private void convertLegacyData() {
        if(statistics != null) {
            return;
        }

        List<SloccountLanguageStatistics> languages = new LinkedList<SloccountLanguageStatistics>();

        if(report != null) {
            for(Language language : report.getLanguages()){
                languages.add(new SloccountLanguageStatistics(language.getName(),
                        language.getLineCount(), language.getFileCount()));
            }
        }

        statistics = new SloccountReportStatistics(languages);
    }

    /**
     * Lazy load report data if they are not already loaded.
     */
    private void lazyLoad() {
        if(report != null) {
            return;
        }

        java.io.File destDir = new java.io.File(owner.getRootDir(),
                SloccountPublisher.BUILD_SUBDIR);

        if (!destDir.exists()) {
            report = new SloccountReport();
            return;
        }

        String realEncoding = (encoding != null && !encoding.isEmpty())
                ? encoding : SloccountPublisher.DEFAULT_ENCODING;

        SloccountParser parser = new SloccountParser(realEncoding, null, null);
        report = parser.parseFiles(destDir.listFiles());
    }

    /**
     * Check that the result is empty.
     * 
     * @return true if the result is empty, otherwise false
     */
    public boolean isEmpty() {
        if(statistics != null) {
            return statistics.getLineCount() <= 0;
        }

        // Legacy data in format of sloccount plugin version 1.10 and less
        if(report != null) {
            return report.getLineCount() <= 0;
        }

        return true;
    }

    public SloccountResult getLanguageResult(String language){
        SloccountReport filtered = new SloccountReport(this.getReport(), new LanguageFileFilter(language));
        return new BreadCrumbResult(filtered, this.owner, language);
    }

    public SloccountResult getFolderResult(String jumbledFolder){
        String folder = jumbledFolder.replace("|", SloccountReport.DIRECTORY_SEPARATOR);
        SloccountReport filtered = new SloccountReport(this.getReport(), new FolderFileFilter(folder));
        return new BreadCrumbResult(filtered, this.owner, folder);
    }

    private static class LanguageFileFilter implements FileFilter, Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        private String language;

        public LanguageFileFilter(String language){
            this.language = language;
        }

        public boolean include(File file) {
            return file.getLanguage().equals(this.language);
        }
    }

    private static class FolderFileFilter implements FileFilter, Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        private String folder;

        public FolderFileFilter(String folder){
            this.folder = folder;
        }

        public boolean include(File file) {
            String fileFolder = SloccountReport.extractFolder(file.getName());
            return this.folder.equals(fileFolder);
        }
    }

    private static class BreadCrumbResult extends SloccountResult implements ModelObject, Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        private String displayName = null;
        
        public BreadCrumbResult(SloccountReport report, AbstractBuild<?,?> owner, String displayName){
            super(null, null, report, owner);
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}
