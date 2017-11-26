package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.Metric;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.RFC;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class JUnitTestRFC extends TestCase {

	private static String workingDirectory;
	private static ApplicationCompiler compiler;

	public void setUp() throws Exception
	{
		workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
		compiler = JUnitTestApplicationCompiler.setUpApplicationCompiler();
		return;
	}
	
	public void tearDown()
	{
		JUnitTestApplicationCompiler.deleteTestFile(workingDirectory);
		workingDirectory = null;
		compiler = null;
	}
	
	public void testClass() throws Exception
	{
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, "TestClass.java",
				getJavaClassesList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();
		Metric metric = new RFC();
		assertTrue(javaApplication.getClassObject("TestClass") != null);
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject("TestClass"));
		System.out.println(report);
		assertTrue("validating number of methods "+report.getValue(), report.getValue() == 10d);
		assertTrue(report.getDetail().size() == 10);
	}

	public void testInterface() throws Exception
	{
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, "TestClass.java",
				getJavaInterfaceList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();
		Metric metric = new RFC();
		assertTrue(javaApplication.getClassObject("TestClass") != null);
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject("TestClass"));
		assertTrue("validate value "+report.getValue(), report.getValue() == 0d);
		assertTrue(report.getDetail().size() == 0);
	}

	public static String getJavaClassesList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("RfcTest");
        String content = bundle.getString("class");
        
        return content;
	}

	public static String getJavaInterfaceList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("RfcTest");
        return bundle.getString("interface");
	}	
}
