package diagnose.configuration;


import edu.umd.msswe.ece591.diagnose.configuration.StructureReader;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestStructureReader extends TestCase  {
	
	private StructureReader reader;
	private String workingDirectory;
	private List<File> dirs;

	public void setUp() throws ConfigurationException
	{
		workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
		JUnitTestApplicationCompiler.createTestFile(workingDirectory);
		dirs = new ArrayList<File>();
		dirs.add(new File(workingDirectory));
		reader = new StructureReader();
	}
	
	public void testStructureReader() throws ConfigurationException
	{
		List<File> files = reader.process(dirs, null);
		assertTrue(files.size()==1);
		
		for (File file: files)
		{
			assertTrue(file.getName().contains("java"));
		}
		return;
	}

	public void tearDown()
	{
		reader = null;
		JUnitTestApplicationCompiler.deleteTestFile(workingDirectory);
	}
}
