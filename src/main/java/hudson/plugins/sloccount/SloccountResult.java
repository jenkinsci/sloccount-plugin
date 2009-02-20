package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.plugins.sloccount.model.File;
import hudson.plugins.sloccount.model.FileFilter;
import hudson.plugins.sloccount.model.SloccountReport;
import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class SloccountResult implements Serializable {

    private SloccountReport report;
    private AbstractBuild owner;

    public SloccountResult(SloccountReport report, AbstractBuild<?,?> owner){
        this.report = report;
        this.owner = owner;
    }

    public SloccountReport getReport(){
        return report;
    }

    public AbstractBuild<?,?> getOwner(){
        return owner;
    }

    public SloccountResult getLanguageResult(String language){
        SloccountReport filtered = new SloccountReport(this.report, new LanguageFileFilter(language));
        return new SloccountResult(filtered, this.owner);
    }

    public SloccountResult getFolderResult(String jumbledFolder){
        String folder = jumbledFolder.replace("|", System.getProperty("file.separator"));
        System.out.println("filtering for folder: " + folder);
        SloccountReport filtered = new SloccountReport(this.report, new FolderFileFilter(folder));
        return new SloccountResult(filtered, this.owner);
    }

    private static class LanguageFileFilter implements FileFilter {
        private String language;

        public LanguageFileFilter(String language){
            this.language = language;
        }

        public boolean include(File file) {
            return file.getLanguage().equals(this.language);
        }
    }

    private static class FolderFileFilter implements FileFilter {
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
}
