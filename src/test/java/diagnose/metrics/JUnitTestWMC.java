package diagnose.metrics;

import diagnose.configuration.JUnitTestApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.WMC;
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

public class JUnitTestWMC extends TestCase {

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
	
	public void testExecuteAbstractClass() throws ConfigurationException
	{
		String className = "TestClass";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java", getJavaClassesList(className));
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		WMC metric = new WMC();
		MetricReport report = metric.execute(javaApplication, javaApplication.getFiles().get(0));
		assertTrue("validate number of methods "+report.getValue(), report.getValue() == 5d);
		//assertTrue("expected 4 methods", report.getDetail().size() == 5d);

		report = metric.execute(javaApplication);
		assertTrue("total metric "+report.getValue(), report.getValue() == 5d);

	}

	public void testExecuteInterface() throws ConfigurationException
	{
		String className = "TestInterface";
		List<File> l = JUnitTestApplicationCompiler.saveTestFile(workingDirectory, className+".java",
				getJavaInterfaceList(className));
		compiler.process(l, workingDirectory, null);
		JavaApplication javaApplication = compiler.getJavaApplicationObject();

		WMC metric = new WMC();
		MetricReport report = metric.execute(javaApplication, javaApplication.getFiles().get(0));
		assertTrue("validate number of methods "+report.getValue(), report.getValue() == 0d);
		assertTrue("expected 4 methods", report.getDetail().size() == 0);
	}

	public static String getJavaClassesList(String className) throws ConfigurationException
	{
		return " \n"+
		"public abstract class "+className+" {\n"+
		"	\n"+
		"    private String attributes = \"Something\";\n"+
		"    private Double attributed = 1.0;\n"+
		"    private Long attributel = 2L;\n"+
		"    \n"+
		"    public "+className+"()\n"+
		"    {\n"+
		"    attributes = null;\n"+
		"    }\n"+
		"    public "+className+"(String a)\n"+
		"    {\n"+
		"    attributes = a;\n"+
		"    }\n"+
		"    public String method1() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method1(String a) {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method2() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method3() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method4() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public abstract String method5();\n"+
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

