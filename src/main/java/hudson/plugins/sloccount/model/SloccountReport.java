package hudson.plugins.sloccount.model;

import hudson.plugins.sloccount.util.StringUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lordofthepigs
 */
public class SloccountReport extends FileContainer implements SloccountReportInterface {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private Map<String, Folder> folders = new LinkedHashMap<String, Folder>();
    private Map<String, Language> languages = new LinkedHashMap<String, Language>();

    /** The longest folder path common to all folders. */
    private String[] rootFolderPath = null;

    public SloccountReport(){
        super();
    }

    public SloccountReport(SloccountReport old, FileFilter filter){
        this();
        for(File f : old.getFiles()){
            if(filter.include(f)){
                this.add(f.getName(), f.getLanguage(), f.getLineCount());
            }
        }
    }

    public void add(String filePath, String languageName, int lineCount){
        String folderPath = extractFolder(filePath);
        
        File file = new File(filePath, languageName, lineCount);
        this.addFile(file);

        Folder folder = this.getFolder(folderPath);
        if(folder == null){
            folder = new Folder(folderPath);
            this.addFolder(folder);
        }
        folder.addFile(file);

        Language language = this.getLanguage(languageName);
        if(language == null){
            language = new Language(languageName);
            this.addLanguage(language);
        }
        language.addFile(file);
    }

    /**
     * Extract directory part of a path. The method searches the directory
     * separator from right in the following order: unix '/', windows '\'.
     * 
     * Examples of input and output:
     * (empty string) - (empty string)
     * file.java - (empty string)
     * /test/file.java - /test
     * /cygdrive/c/test/file.java - /cygdrive/c/test
     * c:\test\file.java - c:\test
     * /test/ - /test
     * /test - (empty string) ... is it file or directory?
     * 
     * @param filePath
     *            the path containing folders and file name
     * @return the path without the file name; if no separator is found in
     *            the input path an empty string will be returned
     */
    public static String extractFolder(String filePath){
        // Try Unix separator
        int index = filePath.lastIndexOf("/");

        if(index != -1) {
            return filePath.substring(0, index);
        }

        // Try Windows separator
        index = filePath.lastIndexOf("\\");

        if(index != -1) {
            return filePath.substring(0, index);
        }

        // No separator found, probably only a file name
        return "";
    }

    public Folder getFolder(String name){
        return this.folders.get(name);
    }

    public List<Folder> getFolders(){
        return new ArrayList<Folder>(folders.values());
    }

    public int getFolderCount(){
        return this.folders.size();
    }

    public String getFolderCountString(){
        return StringUtil.grouping(this.folders.size());
    }

    public Language getLanguage(String name){
        return this.languages.get(name);
    }

    public List<Language> getLanguages(){
        return new ArrayList<Language>(this.languages.values());
    }

    public int getLanguageCount(){
        return this.languages.size();
    }

    public String getLanguageCountString() {
        return StringUtil.grouping(getLanguageCount());
    }
    
    public void addFolder(Folder folder){
        this.folders.put(folder.getName(), folder);

        this.updateRootFolderPath(folder.getName());
    }

    public void addLanguage(Language language){
        this.languages.put(language.getName(), language);
    }

    public String getRootFolder(){
        if(this.rootFolderPath == null){
            // this can happen if no report files were found to match the pattern
            return "";
            
        }else{
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < this.rootFolderPath.length; i++){
                if(i > 0){
                    builder.append("/");
                }
                builder.append(this.rootFolderPath[i]);
            }
            return builder.toString();
        }
    }

    private void updateRootFolderPath(String newFolderName){
        // Unix directory separator
        String[] newFolderPath = newFolderName.split("/");

        if(newFolderPath.length == 1){
            // Windows directory separator '\'
            newFolderPath = newFolderName.split("\\\\");
        }

        if(this.rootFolderPath == null){
            this.rootFolderPath = newFolderPath;
        }else{
            for(int i = 0; i < this.rootFolderPath.length && i < newFolderPath.length; i ++){
                if(!this.rootFolderPath[i].equals(newFolderPath[i])){
                    // we have found the first mismatch between the current rootFolderPath and the new folder
                    // the new root is the subpath of the root up the folder right before the mismatch
                    String[] newRoot = new String[i];
                    System.arraycopy(this.rootFolderPath, 0, newRoot, 0, i);
                    this.rootFolderPath = newRoot;
                    return;
                }
            }
            if (newFolderPath.length < this.rootFolderPath.length) {
                this.rootFolderPath = newFolderPath;
            }
        }
    }

    public File getLongestFile(){
        File longest = null;
        for(File f : this.getFiles()){
            if(longest == null || f.getLineCount() > longest.getLineCount()){
                longest = f;
            }
        }
        return longest;
    }

    public Folder getLongestFolder(){
        Folder longest = null;
        for(Folder f : this.getFolders()){
            if(longest == null || f.getLineCount() > longest.getLineCount()){
                longest = f;
            }
        }
        return longest;
    }

    public Language getLongestLanguage(){
        Language longest = null;
        for(Language l : this.getLanguages()){
            if(longest == null || l.getLineCount() > longest.getLineCount()){
                longest = l;
            }
        }
        return longest;
    }

    /**
     * Simplify the names.
     */
    public void simplifyNames(){
        String root = this.getRootFolder();
        for(File f : this.getFiles()){
            f.simplifyName(root);
        }
        for(Folder f : this.getFolders()){
            f.simplifyName(root);
        }
    }

    public String getName() {
        return "SlocCount Report";
    }
}
