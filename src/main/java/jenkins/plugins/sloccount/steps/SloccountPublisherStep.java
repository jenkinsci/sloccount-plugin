package jenkins.plugins.sloccount.steps;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import hudson.Extension; 
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.sloccount.SloccountPublisher;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor ; 
import org.jenkinsci.plugins.workflow.steps.Step; 
import org.kohsuke.stapler.DataBoundConstructor; 
import org.kohsuke.stapler.DataBoundSetter;

import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.util.Set;
import java.io.Serializable;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

/**
 *
 * @author piomis
 */
public class SloccountPublisherStep extends Step implements Serializable {
    /** 
     * Archiving SLOC Count report
     */
    private String pattern = "";
    private String encoding = "";
    private boolean commentIsCode = true;
    /** 
     * Maximal number of last successful builds displayed in the trend graphs.
     * One or less means unlimited.
     */
    private int numBuildsInGraph = 100;

    /** Try to process the report files even if the build state is marked as failed. */
    private boolean ignoreBuildFailure = false;
    
    @DataBoundConstructor
    public SloccountPublisherStep(String pattern, String encoding,
            boolean commentIsCode, int numBuildsInGraph,
            boolean ignoreBuildFailure) {
        this.pattern = pattern;
        this.encoding = encoding;
        this.commentIsCode = commentIsCode;
        this.numBuildsInGraph = numBuildsInGraph;
        this.ignoreBuildFailure = ignoreBuildFailure;
    }
    
    public String getPattern() { 
        return pattern; 
    } 
    @DataBoundSetter  
    public void setPattern(String pattern) { 
        this.pattern = pattern==null? null:pattern; 
    }

    public String getEncoding() { 
        return encoding; 
    } 
    @DataBoundSetter  
    public void setEncoding(String encoding) { 
        this.encoding = encoding==null? null:encoding; 
    }

    public boolean getCommentIsCode() { 
        return commentIsCode; 
    } 
    @DataBoundSetter  
    public void setCommentIsCode(boolean commentIsCode) { 
        this.commentIsCode = commentIsCode; 
    }
 
    public int getNumBuildsInGraph() { 
        return numBuildsInGraph; 
    } 
    @DataBoundSetter  
    public void setNumBuildsInGraph(int numBuildsInGraph) { 
        this.numBuildsInGraph = numBuildsInGraph; 
    }
    
    public boolean getIgnoreBuildFailure() { 
        return ignoreBuildFailure; 
    } 
    @DataBoundSetter  
    public void setIgnoreBuildFailure(boolean ignoreBuildFailure) { 
        this.ignoreBuildFailure = ignoreBuildFailure; 
    }
    
    @Override
    public StepExecution start(StepContext context) throws Exception { 
       return new SloccountPublisherStepExecution(this, context); 
    } 

    @Extension 
    public static final class DescriptorImpl extends StepDescriptor { 
        /*public DescriptorImpl() { super(SloccountPublisherStepExecution.class); } */
 
 
        @Override 
        public String getFunctionName() { 
            return "sloccountPublish"; 
        } 

        @Override 
        public Set<? extends Class<?>> getRequiredContext() { 
           return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class); 
        } 

 
        @NonNull
        @Override 
        public String getDisplayName() { 
            return "Publish Sloccount reports"; 
        } 
     } 
    
    private static final class SloccountPublisherStepExecution extends SynchronousNonBlockingStepExecution<Void> {

        /** Serial version UID. */
        private static final long serialVersionUID = 1L;
        
        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final SloccountPublisherStep step;

        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final Run<?, ?> run; 
        
        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final TaskListener listener; 
        
        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final FilePath ws;
        
        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final Launcher launcher;


        SloccountPublisherStepExecution(SloccountPublisherStep step, StepContext context) throws IOException, InterruptedException { 
           super(context); 
           this.step = step;
           this.listener = context.get(TaskListener.class);
           this.run = context.get(Run.class);
           this.ws = context.get(FilePath.class);
           this.launcher = context.get(Launcher.class);
        } 


        @Override  
        protected Void run() throws Exception {  
            listener.getLogger().println("Running Sloccount Publisher step");  

            SloccountPublisher publisher = new SloccountPublisher(step.getPattern(),
                    step.getEncoding(), step.getCommentIsCode(),
                    step.getNumBuildsInGraph(), step.getIgnoreBuildFailure());  
            publisher.perform(this.run, this.ws, this.launcher, this.listener);  
            return null;  
        }    
    }
}