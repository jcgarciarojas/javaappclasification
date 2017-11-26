package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.metrics.CBO;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
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

public class JUnitTestCBO extends TestCase {

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
	
	public void testExecuteAbstractClass() throws MetricsException, ConfigurationException
	{
		String className = "TestClass";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java",
				getJavaClassesList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		CBO metric = new CBO();
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject(className));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 2d);
		//assertTrue("expected 1 method", report.getDetail().size() == 2);
	}

	public void testExecuteInterface() throws MetricsException, ConfigurationException
	{
		String className = "TestInterface";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory,
				className+".java", getJavaInterfaceList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		CBO metric = new CBO();
		MetricReport report = metric.execute(javaApplication, javaApplication.getFiles().get(0));
		assertTrue("validate number of methods "+report.getValue(), report.getValue() == 0d);
		assertTrue("expected 0 methods", report.getDetail().size() == 0);
	}

	public static String getJavaClassesList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("CobTest");
        String content = bundle.getString("class");
        
        return content;
	}

	public static String getJavaInterfaceList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("CobTest");
        return bundle.getString("interface");
	}
}

