package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.configuration.XmlReader;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestXmlReader extends TestCase {
	
	private XmlReader xmlReader;

	public void setUp() throws ConfigurationException
	{
		xmlReader = new XmlReader(new File("test_config_system.xml"));
	}

	public void tearDown()
	{
		xmlReader = null;
	}
	
	public void testReadFuzzyRules() throws ConfigurationException
	{
		this.readFuzzyRules(xmlReader.readFuzzyRules());
	}
	
	private void readFuzzyRules(List<FuzzyRule> rules) throws ConfigurationException
	{
		assertTrue("Validate that there are rules available rules ", rules.size()>0);
		
		for(FuzzyRule rule: rules)
		{
			assertTrue("validate that rule contains and id ", rule.getId() != null && rule.getId().length() > 0);
			assertTrue("validate that there is at least on condition ", rule.getConditions().size() > 0);
			int count =0;
			for (Composite cond: rule.getConditions())
			{
				if(cond instanceof ConditionComposite)
					count++;
				testCond((ConditionComposite)cond);
			}
			assertTrue("There should be at least one condition ", count >= 1);
			String then = rule.getThenCondition();
			assertTrue("Validation then condition " , then != null);
		}
	}
	
	private void testCond(ConditionComposite cond) throws ConfigurationException
	{
		assertTrue("condition should have this value assigned "+cond, cond.getFuzzySetId() != null && cond.getFuzzySetId().length() >0); 
		assertTrue("condition should have this value assigned "+cond, cond.getMetricId() != null && cond.getMetricId().length() >0); 
	}

	public void testReadFuzzySets() throws ConfigurationException
	{
		xmlReader.readFuzzySetTypes();
		Hashtable<String, FuzzySet> sets = xmlReader.readFuzzySets();
		for(FuzzySet set: new ArrayList<FuzzySet>(sets.values()))
		{
			assertTrue("", set.getId() != null && set.getId().length() >0);
			assertTrue("", set.getLabel() != null && set.getLabel().length() >0);
			assertTrue("", set.getValueLength() > 0 );
			assertTrue("", set.getMax().getValue() > 0 );
			assertTrue("", set.getValues().size() >0);
			for(FuzzyValue value: set.getValues())
			{
				assertTrue(value.getDegree().getValue()<=1 && value.getDegree().getValue()>=0);
			}
		}
	}
	
	public void testReadOOMetrics() throws ConfigurationException
	{
		Hashtable<String, OOMetric> metrics = xmlReader.readOOMetrics();
		assertTrue("", metrics.size() > 0);
		for(OOMetric metric: new ArrayList<OOMetric>(metrics.values()))
		{
			assertTrue("", metric.getClassName() != null && metric.getClassName().length() >0);
			assertTrue("", metric.getDefinition() != null && metric.getDefinition().length() >0);
			assertTrue("", metric.getId() != null && metric.getId().length() >0);
			assertTrue("", metric.getName() != null && metric.getName().length() >0);
			assertTrue("", metric.getSolution() != null && metric.getSolution().length() >0);
		}
	}
	
	public void testReadSystemProperties() throws ConfigurationException
	{
		Properties p = xmlReader.readDiagnoseConfig();
		assertTrue(p.getProperty("home") != null && p.getProperty("home").length() > 0); 
		assertTrue(p.getProperty("output") != null && p.getProperty("output").length() > 0); 
		assertTrue(p.getProperty("outputFile") != null && p.getProperty("outputFile").length() > 0); 
		assertTrue(p.getProperty("workingDirectory") != null && p.getProperty("workingDirectory").length() > 0); 
		assertTrue(p.getProperty("sourcePath") != null && p.getProperty("sourcePath").length() > 0); 
	}
}
