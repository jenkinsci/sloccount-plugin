package hudson.plugins.sloccount;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.util.ChartUtil;
import java.io.IOException;
import java.io.Serializable;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author lordofthepigs
 */
public class SloccountProjectAction implements Action, Serializable {
    
    public static final String URL_NAME = "sloccountResult";

    public static final int CHART_WIDTH = 500;
    public static final int CHART_HEIGHT = 200;

    public AbstractProject<?,?> project;

    public SloccountProjectAction(final AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getIconFileName() {
        return "/plugin/sloccount/icons/sloccount-24.png";
    }

    public String getDisplayName() {
        return Messages.Sloccount_ProjectAction_Name();
    }

    public String getUrlName() {
        return URL_NAME;
    }
    
    /**
     *
     * Redirects the index page to the last result.
     *
     * @param request
     *            Stapler request
     * @param response
     *            Stapler response
     * @throws IOException
     *             in case of an error
     */
    public void doIndex(final StaplerRequest request, final StaplerResponse response) throws IOException {
        AbstractBuild<?, ?> build = getLastFinishedBuild();
        if (build != null) {
            response.sendRedirect2(String.format("../%d/%s", build.getNumber(), SloccountBuildAction.URL_NAME));
        }
    }

    /**
     * Returns the last finished build.
     *
     * @return the last finished build or <code>null</code> if there is no
     *         such build
     */
    public AbstractBuild<?, ?> getLastFinishedBuild() {
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        while (lastBuild != null && (lastBuild.isBuilding() || lastBuild.getAction(SloccountBuildAction.class) == null)) {
            lastBuild = lastBuild.getPreviousBuild();
        }
        return lastBuild;
    }

    public final boolean hasValidResults() {
        AbstractBuild<?, ?> build = getLastFinishedBuild();
        if (build != null) {
            SloccountBuildAction resultAction = build.getAction(SloccountBuildAction.class);
            if (resultAction != null) {
                return resultAction.getPreviousResult() != null;
            }
        }
        return false;
    }

    /**
     * Display the trend map. Delegates to the the associated
     * {@link ResultAction}.
     *
     * @param request
     *            Stapler request
     * @param response
     *            Stapler response
     * @throws IOException
     *             in case of an error
     */
    public void doTrendMap(final StaplerRequest request, final StaplerResponse response) throws IOException {
        AbstractBuild<?,?> lastBuild = this.getLastFinishedBuild();
        SloccountBuildAction lastAction = lastBuild.getAction(SloccountBuildAction.class);

        ChartUtil.generateClickableMap(
                request,
                response,
                SloccountChartBuilder.buildChart(lastAction),
                CHART_WIDTH,
                CHART_HEIGHT);
    }

    /**
     * Display the trend graph. Delegates to the the associated
     * {@link ResultAction}.
     *
     * @param request
     *            Stapler request
     * @param response
     *            Stapler response
     * @throws IOException
     *             in case of an error in
     *             {@link ResultAction#doGraph(StaplerRequest, StaplerResponse, int)}
     */
    public void doTrend(final StaplerRequest request, final StaplerResponse response) throws IOException {
        AbstractBuild<?,?> lastBuild = this.getLastFinishedBuild();
        SloccountBuildAction lastAction = lastBuild.getAction(SloccountBuildAction.class);

        ChartUtil.generateGraph(
                request,
                response,
                SloccountChartBuilder.buildChart(lastAction),
                CHART_WIDTH,
                CHART_HEIGHT);
    }
}
