package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.Api;
import hudson.model.ModelObject;
import hudson.plugins.sloccount.model.File;
import hudson.plugins.sloccount.model.FileFilter;
import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.model.SloccountReportStatistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lordofthepigs
 */
public class SloccountResult implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    private transient SloccountReport report;

    private final AbstractBuild<?,?> owner;

    /** The statistics. */
    private SloccountReportStatistics statistics;

    /** The encoding that was active at the time of the build. */
    private final String encoding;

    private final boolean commentIsCode;

    public SloccountResult(SloccountReportStatistics statistics, String encoding,
            boolean commentIsCode,
            SloccountReport report, AbstractBuild<?,?> owner){
        this.statistics = statistics;
        this.encoding = encoding;
        this.commentIsCode = commentIsCode;
        this.report = report;
        this.owner = owner;
    }

    public SloccountReport getReport() {
        return lazyLoadReport();
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
        return statistics;
    }

    /**
     * Convert legacy data in format of plugin version 1.10 to the new one that
     * uses statistics.
     * 
     * @return this with optionally updated data
     */
    protected Object readResolve() {
        if (report != null && statistics == null) {
            List<SloccountLanguageStatistics> languages = new ArrayList<SloccountLanguageStatistics>();

            for(Language language : report.getLanguages()){
                languages.add(new SloccountLanguageStatistics(language.getName(),
                        language.getLineCount(), language.getFileCount(), language.getCommentCount()));
            }

            statistics = new SloccountReportStatistics(languages);
        }

        // Just for sure
        if (statistics == null) {
            statistics = new SloccountReportStatistics(new ArrayList<SloccountLanguageStatistics>());
        }

        return this;
    }

    /**
     * Lazy load report data if they are not already loaded.
     */
    private SloccountReport lazyLoadReport() {
        if(report != null) {
            return report;
        }

        java.io.File destDir = new java.io.File(owner.getRootDir(),
                SloccountPublisher.BUILD_SUBDIR);

        if (!destDir.exists()) {
            return new SloccountReport();
        }

        String realEncoding = (encoding != null && !encoding.isEmpty())
                ? encoding : SloccountPublisher.DEFAULT_ENCODING;

        SloccountParser parser = new SloccountParser(realEncoding, null, null,
                commentIsCode);
        return parser.parseFiles(destDir.listFiles());
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
        return new BreadCrumbResult(filtered, this.owner, language, commentIsCode);
    }

    /**
     * Get result for a specific module.
     * 
     * @param module the module
     * @return the result
     */
    public SloccountResult getModuleResult(String module){
        SloccountReport filtered = new SloccountReport(this.getReport(),
                new ModuleFileFilter(module));

        return new BreadCrumbResult(filtered, owner, module, commentIsCode);
    }

    public SloccountResult getFolderResult(String folder){
        SloccountReport filtered = new SloccountReport(this.getReport(), new FolderFileFilter(folder));
        return new BreadCrumbResult(filtered, this.owner, folder, commentIsCode);
    }

    /**
     * Gets the remote API for the build result.
     *
     * @return the remote API
     */
    public Api getApi() {
        return new Api(getStatistics());
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

    /**
     * File filter for modules.
     * 
     * @author Michal Turek
     */
    private static class ModuleFileFilter implements FileFilter, Serializable {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        /** The module name. */
        private String module;

        /**
         * Constructor.
         * 
         * @param module the module
         */
        public ModuleFileFilter(String module){
            this.module = module;
        }

        public boolean include(File file) {
            return file.getModule().equals(module);
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

    private static class BreadCrumbResult extends SloccountResult implements ModelObject {
        /** Serial version UID. */
        private static final long serialVersionUID = 0L;

        private String displayName = null;
        
        public BreadCrumbResult(SloccountReport report, AbstractBuild<?,?> owner,
                                String displayName, boolean commentIsCode){
            super(null, null, commentIsCode, report, owner);
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}
