package hudson.plugins.sloccount;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.sloccount.model.SloccountPublisherReport;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.model.SloccountParser;
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

    /** Subdirectory of build results directory where source files are stored. */
    public static final String BUILD_SUBDIR = "sloccount-plugin";

    private static final String DEFAULT_PATTERN = "**/sloccount.sc";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String pattern;
    private final String encoding;
    
    @DataBoundConstructor
    public SloccountPublisher(String pattern, String encoding){
       
        super();
        
        this.pattern = pattern;
        this.encoding = encoding;
    }

    @Override
    public Action getProjectAction(AbstractProject<?,?> project){
        return new SloccountProjectAction(project);
    }

    protected boolean canContinue(final Result result) {
        return result != Result.ABORTED && result != Result.FAILURE;
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener){
        FilePath workspace = build.getWorkspace();
        PrintStream logger = listener.getLogger();
        SloccountParser parser = new SloccountParser(this.getRealEncoding(), this.getRealPattern(), logger);
        SloccountPublisherReport report;

        try{
            if(this.canContinue(build.getResult())){
                report = workspace.act(parser);
            }else{
                // generate an empty report
                // TODO: Replace this empty report with the last valid one?
                report = new SloccountPublisherReport();
            }
        }catch(IOException ioe){
            ioe.printStackTrace(logger);
            return false;

        }catch(InterruptedException ie){
            ie.printStackTrace(logger);
            return false;
        }

        SloccountResult result = new SloccountResult(report.getStatistics(),
        		getRealEncoding(), null, build);
        
        SloccountBuildAction buildAction = new SloccountBuildAction(build, result);
        
        build.addAction(buildAction);

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
    private void copyFilesToBuildDirectory(List<File> sourceFiles,
            File rootDir, VirtualChannel channel) throws IOException,
            InterruptedException{
        File destDir = new File(rootDir, BUILD_SUBDIR);

        if(!destDir.exists() && !destDir.mkdir()){
            throw new IOException(
                    "Creating directory for copy of workspace files failed: "
                            + destDir.getAbsolutePath());
        }

        for(File sourceFile : sourceFiles){
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
}
