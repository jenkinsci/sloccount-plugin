package hudson.plugins.sloccount.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lordofthepigs
 */
public class SloccountReport extends FileContainer implements Serializable {

    private Map<String, Folder> folders = new LinkedHashMap<String, Folder>();
    private Map<String, Language> languages = new LinkedHashMap<String, Language>();

    /** The longest folder path common to all folders. */
    private String[] rootFolderPath = null;
    private transient String fileSeparator;

    public SloccountReport(){
        super();
        this.fileSeparator = System.getProperty("file.separator");
        if(this.fileSeparator.equals("\\")){
            // escape the backslash if required (fileSeparator is used as a regex)
            this.fileSeparator = "\\\\";
        }
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

        String folderPath = this.extractFolder(filePath);
        
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

    private String extractFolder(String filePath){
        int index = filePath.lastIndexOf(this.fileSeparator);
        return filePath.substring(0, index);
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

    public Language getLanguage(String name){
        return this.languages.get(name);
    }

    public List<Language> getLanguages(){
        return new ArrayList<Language>(this.languages.values());
    }

    public int getLanguageCount(){
        return this.languages.size();
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
        String[] newFolderPath = newFolderName.split(this.fileSeparator);

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
            // no mismatch, the rootFolderPath remains unchanged
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

    public void simplifyNames(){
        String root = this.getRootFolder();
        for(File f : this.getFiles()){
            f.simplifyName(root);
        }
        for(Folder f : this.getFolders()){
            f.simplifyName(root);
        }
    }
}
