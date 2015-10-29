package hudson.plugins.sloccount.model;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test case for Unix and Windows directory separators used in SloccountReport.
 * 
 * @author Michal Turek
 */
public class SloccountReportTest {
    @Test
    public void testExtractFolder() {
        Assert.assertEquals("/test/",
                SloccountReport.extractFolder("/test/file.java"));

        Assert.assertEquals("/cygdrive/c/test/",
                SloccountReport.extractFolder("/cygdrive/c/test/file.java"));

        Assert.assertEquals("c:/test/",
                SloccountReport.extractFolder("c:/test/file.java"));

        Assert.assertEquals("",
                SloccountReport.extractFolder("file.java"));

        Assert.assertEquals("",
                SloccountReport.extractFolder(""));

        Assert.assertEquals("test/",
                SloccountReport.extractFolder("test/file.java"));

        Assert.assertEquals("/test/",
                SloccountReport.extractFolder("/test/"));

        // It searches the separator from right
        Assert.assertEquals("/",
                SloccountReport.extractFolder("/test"));

        Assert.assertEquals("/",
                SloccountReport.extractFolder("/"));
    }
    
    @Test
    public void testFileSeparator_Linux() {
        SloccountReport report = new SloccountReport();
        Assert.assertEquals("", report.getRootFolder());

        report.add("/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar/fubar", report.getRootFolder());

        report.add("/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar/fubar", report.getRootFolder());

        report.add("/foo/bar/fubar/dir/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar/fubar", report.getRootFolder());

        report.add("/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar/fubar", report.getRootFolder());

        report.add("/foo/bar/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar", report.getRootFolder());

        report.add("/foo/bar/dir/test.java", "java", "", 42, 50);
        Assert.assertEquals("/foo/bar", report.getRootFolder());
    }

    @Test
    public void testFileSeparator_WindowsSlash() {
        SloccountReport report = new SloccountReport();
        Assert.assertEquals("", report.getRootFolder());

        report.add("C:/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:/foo/bar/fubar/dir/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:/foo/bar/fubar/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:/foo/bar/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar", report.getRootFolder());

        report.add("C:/foo/bar/dir/test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar", report.getRootFolder());
    }

    @Test
    public void testFileSeparator_WindowsBackSlash() {
        SloccountReport report = new SloccountReport();
        Assert.assertEquals("", report.getRootFolder());

        report.add("C:\\foo\\bar\\fubar\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:\\foo\\bar\\fubar\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:\\foo\\bar\\fubar\\dir\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:\\foo\\bar\\fubar\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar/fubar", report.getRootFolder());

        report.add("C:\\foo\\bar\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar", report.getRootFolder());

        report.add("C:\\foo\\bar\\dir\\test.java", "java", "", 42, 50);
        Assert.assertEquals("C:/foo/bar", report.getRootFolder());
    }
}
