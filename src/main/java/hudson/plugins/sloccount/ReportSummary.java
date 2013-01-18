package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountReport;
import hudson.plugins.sloccount.util.StringUtil;

import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class ReportSummary  implements Serializable {

    private ReportSummary(){
        //super();
    }

    public static String createReportSummary(SloccountReport report, SloccountReport previous){
        StringBuilder builder = new StringBuilder();

        if((report != null)&&(previous != null)){

            String strLines     = Messages.Sloccount_ReportSummary_Lines();
            String strFiles     = Messages.Sloccount_ReportSummary_Files();
            String strAnd       = Messages.Sloccount_ReportSummary_and();
            String strLanguages = Messages.Sloccount_ReportSummary_Languages();

            builder.append("<a href=\"" + SloccountBuildAction.URL_NAME + "\">");
            builder.append(report.getLineCountString());
            printDifference(report.getLineCount(), previous.getLineCount(), builder);

            builder.append(" " + strLines + "</a> in ");
            builder.append(report.getFileCountString());
            printDifference(report.getFileCount(), previous.getFileCount(), builder);

            builder.append(" " + strFiles + " " + strAnd + " ");
            builder.append(report.getLanguageCountString());
            printDifference(report.getLanguageCount(), previous.getLanguageCount(), builder);

            builder.append(" " + strLanguages + ".");
        }
        
        return builder.toString();
    }

    public static String createReportSummaryDetails(SloccountReport report, SloccountReport previous){
        
        StringBuilder builder = new StringBuilder();

        if((report != null)&&(previous != null)){
            
            for(Language language : report.getLanguages()){
                
                Language previousLanguage = null;
                previousLanguage = previous.getLanguage(language.getName());
                
                appendLanguageDetails(language, previousLanguage, builder);
            }
        }
        
        return builder.toString();
    }

    private static void appendLanguageDetails(Language language, Language previous, StringBuilder builder){

        String strLines     = Messages.Sloccount_ReportSummary_Lines();
        String strFiles     = Messages.Sloccount_ReportSummary_Files();
       
        builder.append("<li>");
        builder.append("<a href=\"");
        builder.append(SloccountBuildAction.URL_NAME);
        builder.append("/languageResult/");
        builder.append(language.getName());
        builder.append("\">");
        builder.append(language.getName());
        builder.append("</a> : ");
        builder.append(language.getLineCountString());
        if(previous != null){
            printDifference(language.getLineCount(), previous.getLineCount(), builder);
        }
        builder.append(" " + strLines + " in ");
        builder.append(language.getFileCountString());
        if(previous != null){
            printDifference(language.getFileCount(), previous.getFileCount(), builder);
        }
        builder.append(" " + strFiles + ".</li>");
    }

    private static void printDifference(int current, int previous, StringBuilder builder){
        int difference = current - previous;
        builder.append(" (");

        if(difference >= 0){
            builder.append('+');
        }
        builder.append(StringUtil.grouping(difference));
        builder.append(")");
    }
}
