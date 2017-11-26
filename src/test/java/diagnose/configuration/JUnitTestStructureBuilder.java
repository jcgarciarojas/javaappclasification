package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.Builder;
import edu.umd.msswe.ece591.diagnose.configuration.StructureBuilder;
import edu.umd.msswe.ece591.diagnose.configuration.StructureReader;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import junit.framework.TestCase;

import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestStructureBuilder extends TestCase {
	
	private Builder structureBuilder;
	private static String workingDirectory;

	public void setUp() throws ConfigurationException
	{
		structureBuilder = setUpStructureBuilder();
	}

	public void tearDown()
	{
		structureBuilder = null;
		JUnitTestApplicationCompiler.deleteTestFile(workingDirectory);

	}
	
	public void testStructureBuilder_buildParts()throws ConfigurationException, MetricsException
	{
		structureBuilder.setParameters(getStructureParameters());
		structureBuilder.build();
		structureBuilder.buildParts();
		Object obj = structureBuilder.getObject();
		
		assertTrue(obj != null);
		assertTrue(obj instanceof JavaApplication);
		
		JavaApplication app = (JavaApplication)obj;
		List<JavaClassInfo> l = app.getFiles();
		assertTrue(l.size()>0);
		
		for(JavaClassInfo f: l)
		{
			assertTrue(f.getPath().indexOf(".java")> -1);
		}
	}

	public static Builder setUpStructureBuilder() throws ConfigurationException
	{
		workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
		JUnitTestApplicationCompiler.createTestFile(workingDirectory);
		ApplicationCompiler compiler = JUnitTestApplicationCompiler.setUpApplicationCompiler();
		return new StructureBuilder(compiler,
				new StructureReader());
	}
	
	/**
	 * This method returnthe following a String[] params: 
	 * String directories, String filters, String workingDirectory 
	 * 
	 * @return
	 */
	public static String[] getStructureParameters() throws ConfigurationException
	{
		String[] s = new String[4]; 
		s[0] = JUnitTestApplicationCompiler.getWorkingDirectory();
		s[1] = "";
		s[2] = JUnitTestApplicationCompiler.getWorkingDirectory();
		s[3] = "";
		
		return s;
	}


}