package hudson.plugins.sloccount;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.model.SloccountParser;
import hudson.tasks.Publisher;
import java.io.IOException;
import java.io.PrintStream;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author lordofthepigs
 */
public class SloccountPublisher extends Publisher {

    public static final SloccountDescriptor DESCRIPTOR = new SloccountDescriptor();

    private static final String DEFAULT_PATTERN = "**/sloccount.sc";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String pattern;
    private final String encoding;
    
    public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }
    
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

        if(this.canContinue(build.getResult())){
            FilePath workspace = build.getProject().getWorkspace();
            PrintStream logger = listener.getLogger();
            SloccountParser parser = new SloccountParser(this.getRealEncoding(), this.getRealPattern(), logger);
            SloccountReport report;

            try{
                report = workspace.act(parser);
            
            }catch(IOException ioe){
                ioe.printStackTrace(logger);
                return false;
            
            }catch(InterruptedException ie){
                ie.printStackTrace(logger);
                return false;
            }

            SloccountResult result = new SloccountResult(report, build);
            SloccountBuildAction buildAction = new SloccountBuildAction(build, result);
            build.addAction(buildAction);
        }
        return true;
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
