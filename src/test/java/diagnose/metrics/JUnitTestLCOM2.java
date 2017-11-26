package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.LCOM2;
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

public class JUnitTestLCOM2 extends TestCase {

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
		Metric metric = new LCOM2();
		assertTrue(javaApplication.getClassObject("TestClass") != null);
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject("TestClass"));
		assertTrue("validating number of methods "+report.getValue(), report.getValue() > 0.8);
		//assertTrue(report.getDetail().size() == 3);
	}

	public void testInterface() throws Exception
	{
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, "TestClass.java",
				getJavaInterfaceList());
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();
		Metric metric = new LCOM2();
		assertTrue(javaApplication.getClassObject("TestClass") != null);
		MetricReport report = metric.execute(javaApplication, javaApplication.getClassObject("TestClass"));
		assertTrue(report.getValue() == 0d);
		//assertTrue(report.getDetail().size() == 0);
	}


	public static String getJavaClassesList() throws ConfigurationException
	{
		ResourceBundle bundle = ResourceBundle.getBundle("Lcom2Test");
        String content = bundle.getString("class");
        
        return content;
	}

	public static String getJavaInterfaceList() throws ConfigurationException
	{
		return " \n"+
		"public interface TestClass {\n"+
		"	\n"+
		"    public String method1();\n"+
		"    public String method2();\n"+
		"    public String method3();\n"+
		"    public String method4();\n"+
		"}";
	}
}
