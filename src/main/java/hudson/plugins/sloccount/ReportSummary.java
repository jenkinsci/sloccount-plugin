package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.Language;
import hudson.plugins.sloccount.model.SloccountReport;

import java.io.Serializable;

/**
 *
 * @author lordofthepigs
 */
public class ReportSummary  implements Serializable {

    private ReportSummary(){
        super();
    }

    public static String createReportSummary(SloccountReport report, SloccountReport previous){
        StringBuilder builder = new StringBuilder();

        builder.append("<a href=\"" + SloccountBuildAction.URL_NAME + "\">");
        builder.append(report.getLineCount());
        if(previous != null){
            printDifference(report.getLineCount(), previous.getLineCount(), builder);
        }
        builder.append(" lines</a> in ");
        builder.append(report.getFileCount());
        if(previous != null){
            printDifference(report.getFileCount(), previous.getFileCount(), builder);
        }
        builder.append(" files and ");
        builder.append(report.getLanguageCount());
        if(previous != null){
            printDifference(report.getLanguageCount(), previous.getLanguageCount(), builder);
        }
        builder.append(" languages.");

        return builder.toString();
    }

    public static String createReportSummaryDetails(SloccountReport report, SloccountReport previous){
        StringBuilder builder = new StringBuilder();

        for(Language language : report.getLanguages()){
            Language previousLanguage = null;
            if(previous != null){
                previousLanguage = previous.getLanguage(language.getName());
            }
            appendLanguageDetails(language, previousLanguage, builder);
        }
        return builder.toString();
    }

    public static void appendLanguageDetails(Language language, Language previous, StringBuilder builder){
        builder.append("<li>");
        builder.append("<a href=\"");
        builder.append(SloccountBuildAction.URL_NAME);
        builder.append("/languageResult/");
        builder.append(language.getName());
        builder.append("\">");
        builder.append(language.getName());
        builder.append("</a> : ");
        builder.append(language.getLineCount());
        if(previous != null){
            printDifference(language.getLineCount(), previous.getLineCount(), builder);
        }
        builder.append(" lines in ");
        builder.append(language.getFileCount());
        if(previous != null){
            printDifference(language.getFileCount(), previous.getFileCount(), builder);
        }
        builder.append(" files.</li>");
    }

    private static void printDifference(int current, int previous, StringBuilder builder){
        int difference = current - previous;
        builder.append(" (");

        if(difference >= 0){
            builder.append('+');
        }
        builder.append(difference);
        builder.append(")");
    }
}
