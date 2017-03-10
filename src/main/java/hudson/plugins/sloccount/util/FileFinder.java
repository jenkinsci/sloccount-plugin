package hudson.plugins.sloccount.util;

import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.jenkinsci.remoting.RoleChecker;

/**
 * Scans the workspace and finds all Java files.
 *
 * @author Ulli Hafner
 */
public class FileFinder implements FileCallable<String[]> {
    /** Generated ID. */
    private static final long serialVersionUID = 2970029366847565970L;
    /** The pattern to scan for. */
    private final String pattern;

    /**
     * Creates a new instance of {@link FileFinder}.
     *
     * @param pattern the ant file pattern to scan for
     */
    public FileFinder(final String pattern) {
        this.pattern = pattern;

    }

    /**
     * Returns an array with the filenames of the specified file pattern that have been
     * found in the workspace.
     *
     * @param workspace
     *            root directory of the workspace
     * @param channel
     *            not used
     * @return the filenames of all found files
     */
    public String[] invoke(final File workspace, final VirtualChannel channel) throws IOException {
        return find(workspace);
    }

    /**
     * Returns an array with the filenames of the specified file pattern that have been
     * found in the workspace.
     *
     * @param workspace
     *            root directory of the workspace
     * @return the filenames of all found files
     */
    public String[] find(final File workspace)  {
        try {
            FileSet fileSet = new FileSet();
            Project antProject = new Project();
            fileSet.setProject(antProject);
            fileSet.setDir(workspace);
            fileSet.setIncludes(pattern);

            return fileSet.getDirectoryScanner(antProject).getIncludedFiles();
        }
        catch (BuildException exception) {
            return new String[0];
        }
    }

    @Override
    public void checkRoles(RoleChecker roleChecker) throws SecurityException {

    }

}