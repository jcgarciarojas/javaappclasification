package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.Builder;
import edu.umd.msswe.ece591.diagnose.configuration.ConfigBuilder;
import edu.umd.msswe.ece591.diagnose.configuration.SystemConfiguration;
import edu.umd.msswe.ece591.diagnose.configuration.XmlReader;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.Composite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.ConditionComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
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

public class JUnitTestConfigBuilder extends TestCase {
	
	private Builder configBuilder;

	public void setUp() throws ConfigurationException
	{
		configBuilder = new ConfigBuilder(new XmlReader(new File("test_config_system.xml")));
	}

	public void tearDown()
	{
		configBuilder = null;
	}
	


	public void testConfigBuilder_buildParts()throws ConfigurationException
	{
		configBuilder.build();
		configBuilder.buildParts();
		Object obj = configBuilder.getObject();
		assertTrue(obj != null);
		assertTrue(obj instanceof SystemConfiguration);

		SystemConfiguration config = (SystemConfiguration)obj;
		List<FuzzyRule> rules = config.getFuzzyRules();
		List<OOMetric> metrics = config.getOoMetricsForClasses();

		assertTrue(rules.size()>0);
		assertTrue(metrics.size()>0);

		FuzzyRule rule = rules.get(0);
		for (Composite cond:rule.getConditions())
		{
			ConditionComposite c = (ConditionComposite)cond;
			String setId = c.getFuzzySetId();
			assertTrue(config.getFuzzySet(setId) != null);
			String metricId = c.getMetricId();
			OOMetric metric = (OOMetric)config.getOoMetric(metricId);
			assertTrue( metric!= null);
		}
		
	}

}
