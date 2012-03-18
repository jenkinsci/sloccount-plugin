package hudson.plugins.sloccount.model;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Test;

/**
 * This test checks if the path logic works on linux and windows.
 * @author André Wegmüller
 */
public class SloccountReportTest {

	private static final String ORIGINAL_FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static final String LINUX_PATH = "/usr/bar/jenkins/jobs/Foo/workspace";
	
	private static final String LINUX_SEPARATOR = "/";
	
	private static final String WINDOWS_PATH = "C:\\Users\\Bar\\jenkins\\jobs\\Foo\\workspace";
	
	private static final String WINDOWS_SEPARATOR = "\\";
	
	@AfterClass
	public static void tearDownAfterClass() {
		setSeparator(ORIGINAL_FILE_SEPARATOR);
	}
	
	
	private static void setSeparator(String fileSeparator) {
		System.setProperty("file.separator", fileSeparator);
	}
	
	@Test
	public void testUpdateRootFolderPath_Linux() {
		setSeparator(LINUX_SEPARATOR);
		SloccountReport report = new SloccountReport();
		report.updateRootFolderPath(LINUX_PATH);
		Assert.assertEquals("/usr/bar/jenkins/jobs/Foo/workspace", report.getRootFolder());
	}
	
	@Test
	public void testExtractFolder_Linux() {
		setSeparator(LINUX_SEPARATOR);
		String folder = new SloccountReport().extractFolder(LINUX_PATH);
		Assert.assertEquals("/usr/bar/jenkins/jobs/Foo", folder);
	}
	
	@Test
	public void testFileSeparator_Windows() {
		setSeparator(WINDOWS_SEPARATOR);
		SloccountReport report = new SloccountReport();
		report.updateRootFolderPath(WINDOWS_PATH);
		Assert.assertEquals("C:/Users/Bar/jenkins/jobs/Foo/workspace", report.getRootFolder());
	}
	
	/**
	 * This test reproduces a problem which caused the plugin to crash in versions <= 1.6 (StringOutOfBoundsException).
	 */
	@Test
	public void testExtractFolder_Windows() {
		setSeparator(WINDOWS_SEPARATOR);
		String folder = new SloccountReport().extractFolder(WINDOWS_PATH);
		Assert.assertEquals("C:\\Users\\Bar\\jenkins\\jobs\\Foo", folder);
	}

}
