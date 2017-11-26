package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MHF;
import edu.umd.msswe.ece591.diagnose.metrics.Metric;
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

public class JUnitTestMHF extends TestCase {

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

		Metric metric = new MHF();
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject(className));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() > 0.45);
		assertTrue("expected 1 method but it is "+report.getDetail().size(), report.getDetail().size() == 8);
		System.out.println(report);
		report = metric.execute(javaApplication);
		assertTrue("validate number of methods"+report.getValue(), report.getValue() > 0.45);
		System.out.println(report);
	}

	public void testExecuteInterface() throws MetricsException, ConfigurationException
	{
		String className = "TestClass";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java",
				getJavaInterfaceList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		Metric metric = new MHF();
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject(className));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 0d);
		assertTrue("expected 1 method", report.getDetail().size() == 0d);
	}
	
	public static String getJavaClassesList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("MhfTest");
        String content = bundle.getString("class");
        
        return content;
	}

	public static String getJavaInterfaceList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("MhfTest");
        return bundle.getString("interface");
	}
	
}

