package hudson.plugins.sloccount;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.sloccount.model.SloccountPublisherReport;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.plugins.sloccount.model.SloccountPublisherReport.SlaveFile;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;
import java.io.File;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author lordofthepigs
 */
public class SloccountPublisher extends Recorder implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** Subdirectory of build results directory where source files are stored. */
    public static final String BUILD_SUBDIR = "sloccount-plugin";

    private static final String DEFAULT_PATTERN = "**/sloccount.sc";
    static final String DEFAULT_ENCODING = "UTF-8";

    private final String pattern;
    private final String encoding;

    /** 
     * Maximal number of last successful builds displayed in the trend graphs.
     * One or less means unlimited.
     */
    private final int numBuildsInGraph;

	@DataBoundConstructor
    public SloccountPublisher(String pattern, String encoding,
            int numBuildsInGraph){
        super();
        this.pattern = pattern;
        this.encoding = encoding;
        this.numBuildsInGraph = numBuildsInGraph;
    }

    @Override
    public Action getProjectAction(AbstractProject<?,?> project){
        return new SloccountProjectAction(project, numBuildsInGraph);
    }

    protected boolean canContinue(final Result result) {
        return result != Result.ABORTED && result != Result.FAILURE;
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener){
        PrintStream logger = listener.getLogger();

        if (!canContinue(build.getResult())) {
            logger.println("[SLOCCount] Skipping results publication since the build is not successful");
            return true;
        }

        SloccountParser parser = new SloccountParser(this.getRealEncoding(), this.getRealPattern(), logger);
        SloccountPublisherReport report;

        try{
            report = build.getWorkspace().act(parser);
        }catch(IOException ioe){
            ioe.printStackTrace(logger);
            return false;
        }catch(InterruptedException ie){
            ie.printStackTrace(logger);
            return false;
        }

        if (report.getSourceFiles().size() == 0) {
            logger.format("[SLOCCount] No file is matching the input pattern: %s\n",
                    getRealPattern());
        }

        SloccountResult result = new SloccountResult(report.getStatistics(),
                getRealEncoding(), null, build);
        build.addAction(new SloccountBuildAction(build, result));

        try{
            copyFilesToBuildDirectory(report.getSourceFiles(),
                    build.getRootDir(), launcher.getChannel());
        }catch(IOException e){
            e.printStackTrace(logger);
            return false;
        }catch(InterruptedException e){
            e.printStackTrace(logger);
            return false;
        }

        return true;
    }

    /**
     * Copy files to a build results directory. The copy of a file will be
     * stored in plugin's subdirectory and a hashcode of its absolute path will
     * be used in its name prefix to distinguish files with the same names from
     * different directories.
     * 
     * @param sourceFiles
     *            the files to copy
     * @param rootDir
     *            the root directory where build results are stored
     * @param channel
     *            the communication channel
     * @throws IOException
     *             if something fails
     * @throws InterruptedException
     *             if something fails
     */
    private void copyFilesToBuildDirectory(List<SlaveFile> sourceFiles,
            File rootDir, VirtualChannel channel) throws IOException,
            InterruptedException{
        File destDir = new File(rootDir, BUILD_SUBDIR);

        if(!destDir.exists() && !destDir.mkdir()){
            throw new IOException(
                    "Creating directory for copy of workspace files failed: "
                            + destDir.getAbsolutePath());
        }

        for(SlaveFile sourceFile : sourceFiles){
            File masterFile = new File(destDir, Integer.toHexString(sourceFile
                    .hashCode()) + "_" + sourceFile.getName());

            if(!masterFile.exists()){
                FileOutputStream outputStream = new FileOutputStream(masterFile);
                new FilePath(channel, sourceFile.getAbsolutePath())
                        .copyTo(outputStream);
            }
        }
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    private String getRealEncoding(){
        if(this.getEncoding() == null || this.getEncoding().length() == 0){
            return DEFAULT_ENCODING;
        }else{
            return this.getEncoding();
        }
    }

    private String getRealPattern(){
        if(this.getPattern() == null || this.getPattern().length() == 0){
            return DEFAULT_PATTERN;
        }else{
            return this.getPattern();
        }
    }

    public String getPattern() {
        return pattern;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getNumBuildsInGraph() {
        return numBuildsInGraph;
    }
}
