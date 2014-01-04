package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.SloccountLanguageStatistics;
import hudson.plugins.sloccount.util.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lordofthepigs
 */
public class ReportSummary  implements Serializable {

    private ReportSummary(){

    }

    public static String createReportSummary(List<SloccountLanguageStatistics> current,
            List<SloccountLanguageStatistics> previous){
        StringBuilder builder = new StringBuilder();

        if(current != null){
            String strLines     = Messages.Sloccount_ReportSummary_Lines();
            String strFiles     = Messages.Sloccount_ReportSummary_Files();
            String strAnd       = Messages.Sloccount_ReportSummary_and();
            String strIn        = Messages.Sloccount_ReportSummary_in();
            String strLanguages = Messages.Sloccount_ReportSummary_Languages();

            builder.append("<a href=\"" + SloccountBuildAction.URL_NAME + "\">");
            builder.append(StringUtil.grouping(getLineCount(current)));
            
            if(previous != null) {
                printDifference(getLineCount(current), getLineCount(previous), builder);
            }

            builder.append(" " + strLines + "</a> " + strIn + " ");
            builder.append(StringUtil.grouping(getFileCount(current)));
            
            if(previous != null) {
                printDifference(getFileCount(current), getFileCount(previous), builder);
            }

            builder.append(" " + strFiles + " " + strAnd + " ");
            builder.append(StringUtil.grouping(getLanguageCount(current)));
            
            if(previous != null) {
                printDifference(getLanguageCount(current), getLanguageCount(previous), builder);
            }

            builder.append(" " + strLanguages + ".");
        }
        
        return builder.toString();
    }

    public static String createReportSummaryDetails(List<SloccountLanguageStatistics> current,
            List<SloccountLanguageStatistics> previous){
        
        StringBuilder builder = new StringBuilder();

        if(current != null){
            
            for(SloccountLanguageStatistics language : current){
                
                SloccountLanguageStatistics previousLanguage = null;
                
                if(previous != null) {
                    previousLanguage = getLanguage(previous, language.getName());
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

    private static int getLineCount(List<SloccountLanguageStatistics> statistics)
    {
        int lineCount = 0;

        for(SloccountLanguageStatistics it : statistics) {
            lineCount += it.getLineCount();
        }

        return lineCount;
    }
    
    private static int getFileCount(List<SloccountLanguageStatistics> statistics)
    {
        int fileCount = 0;

        for(SloccountLanguageStatistics it : statistics) {
            fileCount += it.getFileCount();
        }

        return fileCount;
    }
    
    private static int getLanguageCount(List<SloccountLanguageStatistics> statistics)
    {
        return statistics.size();
    }
    
    static SloccountLanguageStatistics getLanguage(List<SloccountLanguageStatistics> statistics, String name)
    {
        for(SloccountLanguageStatistics it : statistics) {
            if(it.getName().equals(name)) {
                return it;
            }
        }
        
        return new SloccountLanguageStatistics(name, 0, 0);
    }
}
