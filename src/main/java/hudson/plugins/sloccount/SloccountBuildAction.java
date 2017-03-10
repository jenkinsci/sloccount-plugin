package hudson.plugins.sloccount;

import hudson.model.Action;
import hudson.model.Run;
import hudson.plugins.sloccount.model.SloccountReportStatistics;

import java.io.Serializable;

import org.kohsuke.stapler.StaplerProxy;

/**
 *
 * @author lordofthepigs
 */
public class SloccountBuildAction implements Action, Serializable, StaplerProxy {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    public static final String URL_NAME = "sloccountResult";

    private Run<?,?> run;
    private SloccountResult result;

    public SloccountBuildAction(Run<?,?> run, SloccountResult result){
        this.run = run;
        this.result = result;
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
        if(this.run == null){
            return null;
        }

        Run<?,?> previousBuild = this.run.getPreviousBuild();

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

    public Run<?,?> getRun(){
        return this.run;
    }

    public Object getTarget() {
        return this.result;
    }
}
