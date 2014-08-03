package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountReportStatistics;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author lordofthepigs
 */
public class SloccountChartBuilder implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private SloccountChartBuilder(){
    }

    public static JFreeChart buildChart(SloccountBuildAction action,
            int numBuildsInGraph){
        String strLines = Messages.Sloccount_Trend_LinesTotal();

        JFreeChart chart = ChartFactory.createStackedAreaChart(null, null,
                strLines, buildDataset(action, numBuildsInGraph),
                PlotOrientation.VERTICAL, true, false, true);

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

        SloccountAreaRenderer renderer = new SloccountAreaRenderer(action.getUrlName());
        plot.setRenderer(renderer);

        return chart;
    }

    private static CategoryDataset buildDataset(SloccountBuildAction lastAction,
            int numBuildsInGraph){
        DataSetBuilder<String, NumberOnlyBuildLabel> builder = new DataSetBuilder<String, NumberOnlyBuildLabel>();
        Set<String> allLanguages = new HashSet<String>();

        SloccountBuildAction action = lastAction;
        int numBuilds = 0;

        // numBuildsInGraph <= 1 means unlimited
        while(action != null && (numBuildsInGraph <= 1 || numBuilds < numBuildsInGraph)){
            SloccountResult result = action.getResult();
            if(result != null){
                NumberOnlyBuildLabel buildLabel = new NumberOnlyBuildLabel(action.getBuild());

                allLanguages.addAll(result.getStatistics().getAllLanguages());
                Set<String> remainingLanguages = new HashSet<String>(allLanguages);

                for(SloccountLanguageStatistics l : result.getStatistics().getStatistics()){
                    builder.add(l.getLineCount(), l.getName(), buildLabel);
                    remainingLanguages.remove(l.getName());
                }
                
                for(String language : remainingLanguages) {
                    // Language disappeared
                    builder.add(0, language, buildLabel);
                }

                ++numBuilds;
            }

            action = action.getPreviousAction();
        }

        return builder.build();
    }
    
    public static JFreeChart buildChartDelta(SloccountBuildAction action,
            int numBuildsInGraph){

        String strLinesDelta = Messages.Sloccount_Trend_LinesDelta();

        JFreeChart chart = ChartFactory.createStackedAreaChart(null, null,
                strLinesDelta, buildDatasetDelta(action, numBuildsInGraph),
                PlotOrientation.VERTICAL, true, false, true);

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

        SloccountAreaRenderer renderer = new SloccountAreaRenderer(action.getUrlName());
        plot.setRenderer(renderer);

        return chart;
    }

    private static CategoryDataset buildDatasetDelta(SloccountBuildAction lastAction,
            int numBuildsInGraph){
        DataSetBuilder<String, NumberOnlyBuildLabel> builder = new DataSetBuilder<String, NumberOnlyBuildLabel>();
        Set<String> allLanguages = new HashSet<String>();
        SloccountBuildAction action = lastAction;

        // Initial languages from the first action
        if(action != null && action.getResult() != null) {
            allLanguages.addAll(action.getResult().getStatistics().getAllLanguages());
        }

        int numBuilds = 0;

        // numBuildsInGraph <= 1 means unlimited
        while(action != null && (numBuildsInGraph <= 1 || numBuilds < numBuildsInGraph)){
            SloccountBuildAction previousAction = action.getPreviousAction();
            SloccountResult result = action.getResult();
            SloccountReportStatistics previousStatistics = null;

            if(result != null){
                NumberOnlyBuildLabel buildLabel = new NumberOnlyBuildLabel(action.getBuild());

                if(previousAction != null && previousAction.getResult() != null){
                    previousStatistics = previousAction.getResult().getStatistics();
                } else {
                    // This will produce zero delta for the first build
                    previousStatistics = result.getStatistics();
                }

                allLanguages.addAll(previousStatistics.getAllLanguages());
                Set<String> remainingLanguages = new HashSet<String>(allLanguages);

                for(SloccountLanguageStatistics current : result.getStatistics().getStatistics()){
                    SloccountLanguageStatistics previous = previousStatistics.getLanguage(current.getName());

                    builder.add(current.getLineCount() - previous.getLineCount(),
                            current.getName(), buildLabel);

                    remainingLanguages.remove(current.getName());
                }

                for(String language : remainingLanguages) {
                    SloccountLanguageStatistics previous
                            = previousStatistics.getLanguage(language);

                    // Language disappeared (current - previous = 0 - previous)
                    builder.add(-previous.getLineCount(), language, buildLabel);
                }

                ++numBuilds;
            }

            action = previousAction;
        }

        return builder.build();
    }
}
