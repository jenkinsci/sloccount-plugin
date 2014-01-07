package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.model.SloccountReportStatistics;
import hudson.plugins.sloccount.util.StringUtil;

import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class ReportSummary  implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    private ReportSummary(){

    }

    public static String createReportSummary(SloccountReportStatistics current,
            SloccountReportStatistics previous){
        StringBuilder builder = new StringBuilder();

        if(current != null){
            String strLines     = Messages.Sloccount_ReportSummary_Lines();
            String strFiles     = Messages.Sloccount_ReportSummary_Files();
            String strAnd       = Messages.Sloccount_ReportSummary_and();
            String strIn        = Messages.Sloccount_ReportSummary_in();
            String strLanguages = Messages.Sloccount_ReportSummary_Languages();

            builder.append("<a href=\"" + SloccountBuildAction.URL_NAME + "\">");
            builder.append(StringUtil.grouping(current.getLineCount()));
            
            if(previous != null) {
                printDifference(current.getLineCount(), previous.getLineCount(), builder);
            }

            builder.append(" " + strLines + "</a> " + strIn + " ");
            builder.append(StringUtil.grouping(current.getFileCount()));
            
            if(previous != null) {
                printDifference(current.getFileCount(), previous.getFileCount(), builder);
            }

            builder.append(" " + strFiles + " " + strAnd + " ");
            builder.append(StringUtil.grouping(current.getLanguageCount()));
            
            if(previous != null) {
                printDifference(current.getLanguageCount(), previous.getLanguageCount(), builder);
            }

            builder.append(" " + strLanguages + ".");
        }

        return builder.toString();
    }

    public static String createReportSummaryDetails(SloccountReportStatistics current,
            SloccountReportStatistics previous){
        StringBuilder builder = new StringBuilder();

        if(current != null){

            for(SloccountLanguageStatistics language : current.getStatistics()){
                SloccountLanguageStatistics previousLanguage = null;

                if(previous != null) {
                    previousLanguage = previous.getLanguage(language.getName());
                }

                appendLanguageDetails(language, previousLanguage, builder);
            }
        }

        return builder.toString();
    }

    private static void appendLanguageDetails(SloccountLanguageStatistics current,
            SloccountLanguageStatistics previous, StringBuilder builder){

        String strLines     = Messages.Sloccount_ReportSummary_Lines();
        String strFiles     = Messages.Sloccount_ReportSummary_Files();
        String strIn        = Messages.Sloccount_ReportSummary_in();

        builder.append("<li>");
        builder.append("<a href=\"");
        builder.append(SloccountBuildAction.URL_NAME);
        builder.append("/languageResult/");
        builder.append(current.getName());
        builder.append("\">");
        builder.append(current.getName());
        builder.append("</a> : ");
        builder.append(StringUtil.grouping(current.getLineCount()));

        if(previous != null){
            printDifference(current.getLineCount(), previous.getLineCount(), builder);
        }

        builder.append(" " + strLines + " " + strIn + " ");
        builder.append(StringUtil.grouping(current.getFileCount()));
        
        if(previous != null){
            printDifference(current.getFileCount(), previous.getFileCount(), builder);
        }

        builder.append(" " + strFiles + ".</li>");
    }

    private static void printDifference(int current, int previous, StringBuilder builder){
        int difference = current - previous;

        if(difference > 0)
        {
            builder.append(" (+");
            builder.append(StringUtil.grouping(difference));
            builder.append(")");
        }
        else if(difference == 0)
        {
            // do nothing
        }
        else
        {
            builder.append(" ("); // minus sign is part of the difference variable (negative number)
            builder.append(StringUtil.grouping(difference));
            builder.append(")");
        }
    }
}
