package hudson.plugins.sloccount;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author lordofthepigs
 */
public class SloccountPublisher extends Recorder implements Serializable {

    private static final String DEFAULT_PATTERN = "**/sloccount.sc";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String pattern;
    private final String encoding;
    
    @DataBoundConstructor
    public SloccountPublisher(String pattern, String encoding){
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
       
        SloccountResult result;
            
        PrintStream logger = listener.getLogger();
        SloccountReport report = new SloccountReport();
        SloccountParser parser = new SloccountParser(this.getRealEncoding(), this.getRealPattern(), logger, report);

        if(this.canContinue(build.getResult())){
            
            try{
                FilePath workspace = build.getWorkspace();
                report = workspace.act(parser);

            }catch(IOException ioe){
                ioe.printStackTrace(logger);
                return false;

            }catch(InterruptedException ie){
                ie.printStackTrace(logger);
                return false;
            }
            
        }else{
            
            // continue with empty report object
        }
  
        result = new SloccountResult(report, build);
        
        SloccountBuildAction buildAction = new SloccountBuildAction(build, result);
        
        build.addAction(buildAction);
        
        return true;
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
