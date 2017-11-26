package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.metrics.DIT;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.Metric;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class JUnitTestDIT extends TestCase {

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
	
	public void testExecuteClass() throws MetricsException, ConfigurationException
	{
		String className = "TestClass";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java",
				getJavaClassesList(className));
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		Metric metric = new DIT();
		MetricReport report = null;

		report = metric.execute(javaApplication, javaApplication.getClassObject("ChildClass1"));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 2);
		//assertTrue(report.getDetail().size() == 2);

		report = metric.execute(javaApplication, javaApplication.getClassObject(className));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 0);

		report = metric.execute(javaApplication, javaApplication.getClassObject("ChildClass"));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 1);
	}

	public void testExecuteClass2() throws MetricsException, ConfigurationException
	{
		String className = "TestClass";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java", getJavaClassesList2(className));
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		Metric metric = new DIT();
		MetricReport report = null;

		report = metric.execute(javaApplication, javaApplication.getClassObject("ChildClass1"));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 2);
		//assertTrue(report.getDetail().size() == 2);
		
		report = metric.execute(javaApplication, javaApplication.getClassObject(className));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 0);
		//assertTrue(report.getDetail().size() == 0);

		report = metric.execute(javaApplication, javaApplication.getClassObject("ChildClass"));
		assertTrue("validate number of methods"+report.getValue(), report.getValue() == 1);
		//assertTrue(report.getDetail().size() == 1);
	}

	public void testExecuteInterface() throws MetricsException, ConfigurationException
	{
		String className = "TestInterface";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java", getJavaInterfaceList(className));
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		Metric metric = new DIT();
		MetricReport report = metric.execute(javaApplication, javaApplication.getFiles().get(0));
		assertTrue("validate number of methods "+report.getValue(), report.getValue() == 0d);
		assertTrue("expected 0 methods", report.getDetail().size() == 0);
	}

	public static String getJavaClassesList(String className) throws ConfigurationException
	{
		return " \n"+
		"import java.util.List;	\n"+
		"public class "+className+" {\n"+
		"	\n"+
		"    private String attributes = \"Something\";\n"+
		"    private Double attributed = 1.0;\n"+
		"    private Long attributel = 2L;\n"+
		"	 private List l;\n"+
		"    private class ChildClass extends "+className+"{}\n"+
		"    private class ChildClass1 extends ChildClass {}\n"+
		"}";
	}

	public static String getJavaClassesList2(String className) throws ConfigurationException
	{
		return " \n"+
		"import java.util.List;	\n"+
		"public class "+className+" extends Hello {\n"+
		"	\n"+
		"    private String attributes = \"Something\";\n"+
		"    private Double attributed = 1.0;\n"+
		"    private Long attributel = 2L;\n"+
		"	 private List l;\n"+
		"    private class ChildClass extends "+className+"{}\n"+
		"    private class ChildClass1 extends ChildClass {}\n"+
		"}";
	}

	public static String getJavaInterfaceList(String className) throws ConfigurationException
	{
		return " \n"+
		"public interface "+className+" {\n"+
		"	\n"+
		"    public String method1();\n"+
		"    public String method2();\n"+
		"    public String method3();\n"+
		"    public String method4();\n"+
		"}";
	}
}

