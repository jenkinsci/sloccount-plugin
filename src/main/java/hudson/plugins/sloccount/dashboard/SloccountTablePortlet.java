package hudson.plugins.sloccount.dashboard;

import java.util.LinkedList;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.sloccount.Messages;
import hudson.plugins.sloccount.SloccountBuildAction;
import hudson.plugins.sloccount.SloccountResult;
import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountReportStatistics;
import hudson.plugins.sloccount.util.StringUtil;
import hudson.plugins.view.dashboard.DashboardPortlet;

/**
 * Dashboard portlet that shows a table with jobs and size of their code base.
 * 
 * @author Michal Turek
 */
public class SloccountTablePortlet extends DashboardPortlet {
    /**
     * Constructor.
     * 
     * @param name
     *            the name of the portlet
     */
    @DataBoundConstructor
    public SloccountTablePortlet(String name) {
        super(name);
    }

    /**
     * Get latest available SLOCCount statistics of a job.
     * 
     * @param job
     *            the job
     * @return the statistics, always non-null value
     */
    public SloccountReportStatistics getStatistics(Job<?, ?> job) {
        Run<?, ?> build = job.getLastBuild();

        while(build != null){
            SloccountBuildAction action = build.getAction(SloccountBuildAction.class);

            if (action != null) {
                SloccountResult result = action.getResult();

                if(result != null && !result.isEmpty()) {
                    return result.getStatistics();
                }
            }

            build = build.getPreviousBuild();
        }

        return new SloccountReportStatistics(new LinkedList<SloccountLanguageStatistics>());
    }

    /**
     * Format an integer to contain a thousands separator.
     * 
     * @return the formatted string
     */
    public String grouping(int value) {
        return StringUtil.grouping(value);
    }

    /**
     * Extension point registration.
     * 
     * @author Michal Turek
     */
    @Extension(optional = true)
    public static class SloccountTableDescriptor extends Descriptor<DashboardPortlet> {
        @Override
        public String getDisplayName() {
            return Messages.Sloccount_Portlet_Name();
        }
    }
}
