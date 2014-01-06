package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;
import hudson.plugins.sloccount.model.File;
import hudson.plugins.sloccount.model.FileFilter;
import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.plugins.sloccount.model.SloccountReport;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lordofthepigs
 */
public class SloccountResult implements Serializable {

    private SloccountReport report;
    private final AbstractBuild owner;
    
    /** The SLOCCount statistics. */
    private List<SloccountLanguageStatistics> statistics;
    
    /** The encoding that was active at the time of the build. */
    private final String encoding;
    
    public SloccountResult(List<SloccountLanguageStatistics> statistics, String encoding,
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
     * @return the statistics per language
     */
    public List<SloccountLanguageStatistics> getStatistics() {
        convertLegacyData();
        return Collections.unmodifiableList(statistics);
    }
    
    /**
     * Convert legacy data in format of sloccount plugin version 1.10
     * to the new one that uses statistics.
     */
    private void convertLegacyData() {
        if(statistics != null) {
            return;
        }

        statistics = new LinkedList<SloccountLanguageStatistics>();
        
        if(report != null) {
            for(Language language : report.getLanguages()){
                statistics.add(new SloccountLanguageStatistics(language.getName(),
                        language.getLineCount(), language.getFileCount()));
            }
        }
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

        SloccountParser parser = new SloccountParser(encoding, null, null);
        report = parser.parseFiles(destDir.listFiles());
    }

    /**
     * Check that the result is empty.
     * 
     * @return true if the result is empty, otherwise false
     */
    public boolean isEmpty() {
        if(statistics != null) {
            return statistics.isEmpty();
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
        String folder = jumbledFolder.replace("|", System.getProperty("file.separator"));
        SloccountReport filtered = new SloccountReport(this.getReport(), new FolderFileFilter(folder));
        return new BreadCrumbResult(filtered, this.owner, folder);
    }

    private static class LanguageFileFilter implements FileFilter, Serializable {
        private String language;

        public LanguageFileFilter(String language){
            this.language = language;
        }

        public boolean include(File file) {
            return file.getLanguage().equals(this.language);
        }
    }

    private static class FolderFileFilter implements FileFilter, Serializable {
        private String folder;

        public FolderFileFilter(String folder){
            this.folder = folder;
        }

        public boolean include(File file) {
            String separator = System.getProperty("file.separator");

            int index = file.getName().lastIndexOf(separator);
            String fileFolder = file.getName().substring(0, index);

            return this.folder.equals(fileFolder);
        }
    }

    private static class BreadCrumbResult extends SloccountResult implements ModelObject, Serializable {

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
