package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.plugins.sloccount.model.SloccountReport;
import java.io.Serializable;
import org.kohsuke.stapler.StaplerProxy;

/**
 *
 * @author lordofthepigs
 */
public class SloccountBuildAction implements Action, Serializable, StaplerProxy {

    public static final String URL_NAME = "sloccountResult";

    private AbstractBuild<?,?> build;
    private SloccountResult result;

    public SloccountBuildAction(AbstractBuild<?,?> build, SloccountResult result){
        this.build = build;
        this.result = result;
    }

    public String getIconFileName() {
        return "/plugin/sloccount/icons/sloccount-24.png";
    }

    public String getDisplayName() {
        return "SLOCCount";
    }

    public String getUrlName() {
        return URL_NAME;
    }

    public String getSummary(){
        
        String retVal = "";
        
        if(this.result != null){
            
            retVal =  ReportSummary.createReportSummary(this.result.getReport(), this.getPreviousReport());
        }
        
        return retVal;
    }

    public String getDetails(){
        
        String retVal = "";
        
        if(this.result != null){
            retVal =  ReportSummary.createReportSummaryDetails(this.result.getReport(), this.getPreviousReport());
        }
        
        return retVal;
    }

    public SloccountResult getResult(){
        return this.result;
    }

    private SloccountReport getPreviousReport(){
        SloccountResult previous = this.getPreviousResult();
        if(previous == null){
            return null;
        }else{
           return previous.getReport();
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

    SloccountBuildAction getPreviousAction(){
        
        if(this.build != null){
            
            AbstractBuild<?,?> previousBuild = this.build.getPreviousBuild();
            
            if(previousBuild != null){
                
                return previousBuild.getAction(SloccountBuildAction.class);
            }
        }
        return null;
    }

    AbstractBuild<?,?> getBuild(){
        return this.build;
    }

    public Object getTarget() {
        return this.result;
    }

}
