package hudson.plugins.sloccount.util;

/**
 * Utility
 * 
 * @author ssogabe
 */
public class StringUtil {

    private StringUtil() {
        //
    }

    public static String grouping(int value) {
        return String.format("%,d", value);
    }

}
