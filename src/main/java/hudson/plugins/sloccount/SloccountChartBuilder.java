package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import java.awt.Color;
import java.io.Serializable;

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

    private SloccountChartBuilder(){
        super();
    }

    public static JFreeChart buildChart(SloccountBuildAction action){
        JFreeChart chart = ChartFactory.createStackedAreaChart(null, null, "lines", buildDataset(action), PlotOrientation.VERTICAL, true, false, true);

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

    private static CategoryDataset buildDataset(SloccountBuildAction lastAction){
        DataSetBuilder<String, NumberOnlyBuildLabel> builder = new DataSetBuilder<String, NumberOnlyBuildLabel>();

        SloccountBuildAction action = lastAction;
        do{
            SloccountResult result = action.getResult();
            if(result != null){
                SloccountReport report = result.getReport();
                NumberOnlyBuildLabel buildLabel = new NumberOnlyBuildLabel(action.getBuild());

                for(Language l : report.getLanguages()){
                    builder.add(l.getLineCount(), l.getName(), buildLabel);
                }
            }

            action = action.getPreviousAction();
        }while(action != null);

        return builder.build();
    }
}
