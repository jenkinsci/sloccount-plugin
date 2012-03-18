package hudson.plugins.sloccount;

import hudson.plugins.sloccount.model.SloccountReportTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Andr� Wegm�ller
 */
@RunWith(Suite.class)
@SuiteClasses({
	SloccountReportTest.class
})
public class SloccountTestSuite {
}
