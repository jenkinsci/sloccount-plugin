package hudson.plugins.sloccount.model;

import java.io.File;

/**
 * Generic parsing results.
 * 
 * @author Michal Turek
 */
public interface SloccountReportInterface {
    /**
     * Add lines count.
     * 
     * @param filePath
     *            the file
     * @param languageName
     *            the language name
     * @param lineCount
     *            the line count
     */
    void add(String filePath, String languageName, int lineCount);

    /**
     * Add a source file.
     * 
     * @param sourceFile
     *            the source file
     */
    void addSourceFile(File sourceFile);

    /**
     * Simplify the names.
     */
    void simplifyNames();

    /**
     * Get the root folder.
     * 
     * @return the root folder
     */
    String getRootFolder();
}
