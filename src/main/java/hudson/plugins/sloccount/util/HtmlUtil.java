package hudson.plugins.sloccount.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * HTML related utilities.
 * 
 * @author Michal Turek
 */
public class HtmlUtil {
    /**
     * Encode string so it can be used as URL.
     * 
     * @param url
     *            the input string
     * @return the string encoded as URL or the input string on error.
     * @see URLEncoder#encode(String, String)
     */
    public static String urlEncode(String url) {
        try {
            // "%20" instead of "+" to be compatible with decoder inside Jenkins
            // "C/C++ Header" -> "C%2FC%2B%2B+Header" -> "C/C+++Header"
            return URLEncoder.encode(url, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            // Should never happen, UTF-8 should be always defined
            return url;
        }
    }
}
