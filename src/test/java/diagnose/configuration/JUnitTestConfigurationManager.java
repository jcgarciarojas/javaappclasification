package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.*;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;
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
 */

public class JUnitTestConfigurationManager extends TestCase {
	
	private ConfigurationManager factory;

	public void setUp() throws ConfigurationException
	{
		Builder structureBuilder = JUnitTestStructureBuilder.setUpStructureBuilder();
		Builder configBuilder = new ConfigBuilder(new XmlReader(new File("test_config_system.xml")));
		
		factory = new ConfigurationManager(structureBuilder, configBuilder);
	}

	public void tearDown()
	{
		factory = null;
	}
	
	public void testConfigBuilder()throws ConfigurationException
	{
		Object obj = factory.getConfig();
		assertTrue(obj instanceof SystemConfiguration);	

		SystemConfiguration config = (SystemConfiguration)obj;
		List<FuzzyRule> rules = config.getFuzzyRules();
		List<OOMetric> metrics = config.getOoMetricsForClasses();

		assertTrue(rules.size()>0);
		assertTrue(metrics.size()>0);	
	}

	public void testStructureBuilder()throws ConfigurationException
	{
		Object obj = factory.getStructure(JUnitTestStructureBuilder.getStructureParameters());
		assertTrue(obj instanceof JavaApplication);
		JavaApplication app = (JavaApplication)obj;
		List l = app.getFiles();
		assertTrue(l.size()>0);
	}	
}
