package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.Action;
import jenkins.model.RunAction2;
import hudson.plugins.sloccount.model.SloccountReportStatistics;
import jenkins.tasks.SimpleBuildStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kohsuke.stapler.StaplerProxy;

/**
 *
 * @author lordofthepigs
 */
public class SloccountBuildAction implements RunAction2, StaplerProxy, SimpleBuildStep.LastBuildAction  {
    public static final String URL_NAME = "sloccountResult";

    private transient Run<?,?> build;
    private final SloccountResult result;
    private final int numBuildsInGraph;
    
    private transient List<SloccountProjectAction> projectActions;

    public SloccountBuildAction(SloccountResult result, int numBuildsInGraph){
        this.result = result;
        this.numBuildsInGraph = numBuildsInGraph;
    }

    public String getIconFileName() {
        return "/plugin/sloccount/icons/sloccount-24.png";
    }

    public String getDisplayName() {
        return Messages.Sloccount_SloccountResults();
    }

    public String getUrlName() {
        return URL_NAME;
    }
    
    @Override  
    public Collection<? extends Action> getProjectActions() {
        return this.projectActions;
    }

    private void buildProjectActions() {
        if (this.projectActions != null) {
            return;
        }

        this.projectActions = Collections.synchronizedList(new ArrayList<SloccountProjectAction>());
        this.projectActions.add(new SloccountProjectAction(this.build.getParent(), this.numBuildsInGraph));
    }

    /**
     * Get differences between two report statistics.
     * 
     * @return the differences
     */
    public SloccountDiffSummary getDiffSummary() {
        return SloccountDiffSummary.getDiffSummary(getPreviousStatistics(),
                result.getStatistics());
    }

    public SloccountResult getResult(){
        return this.result;
    }

    private SloccountReportStatistics getPreviousStatistics(){
        SloccountResult previous = this.getPreviousResult();
        if(previous == null){
            return null;
        }else{
           return previous.getStatistics();
        }
    }

    SloccountResult getPreviousResult(){
        SloccountBuildAction previousAction = this.getPreviousAction();
        SloccountResult previousResult = null;
        if(previousAction != null){
            previousResult = previousAction.getResult();
        }
        
        return previousResult;
    }

    /**
     * Get the previous valid and non-empty action.
     * 
     * @return the action or null
     */
    SloccountBuildAction getPreviousAction(){
        if(this.build == null){
            return null;
        }

        Run<?,?> previousBuild = this.build.getPreviousBuild();

        while(previousBuild != null){
            SloccountBuildAction action = previousBuild
                    .getAction(SloccountBuildAction.class);

            if (action != null) {
                SloccountResult result = action.getResult();
                
                if(result != null && !result.isEmpty()) {
                    return action;
                }
            }

            previousBuild = previousBuild.getPreviousBuild();
        }

        return null;
    }

    public Run<?,?> getBuild(){
        return this.build;
    }

    public Object getTarget() {
        return this.result;
    }
    
    @Override
    public void onLoad(Run<?,?> r)
    {
        this.build = r;
        this.result.setOwner(r);
        buildProjectActions();
    }
    
    @Override
    public void onAttached(Run<?,?> r)
    {
        this.build = r;
        buildProjectActions();
    }
}
