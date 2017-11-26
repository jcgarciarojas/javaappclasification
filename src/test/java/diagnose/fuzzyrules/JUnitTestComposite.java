package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestComposite extends TestCase {

	private Map<String, MetricReport> metricsReport;
	private Map<String, FuzzySet> sets;
	
	public void setUp() throws Exception
	{
		FuzzySetFactory.instance().setTypes(JUnitTestFuzzySetFactory.getSetTypes());
		ArrayList<Double> l = new ArrayList<Double>();
		l.add(4.5d);
		l.add(3d);
		l.add(6d);
		metricsReport = JUnitTestRuleBasedEngine.getMetricReport(l);

		Map<String, String> Map = new Hashtable<String, String>();
		Map.put("triangle-set", "4,5,6");
		Map.put("descendent-set", "4,5");
		Map.put("ascendent-set", "5,6");
		sets = JUnitTestRuleBasedEngine.getFuzzySets(Map);
		return;
	}

	public void tearDown()
	{
		return;
	}

	public void testIfComposite() throws ConfigurationException
	{
		OrComposite or = new OrComposite();
		or.addCompositeCondition(new ConditionComposite("m1","is", "id1"));

		AndComposite and = new AndComposite();
		or.addCompositeCondition(and);
		and.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		and.addCompositeCondition(new ConditionComposite("m3","is", "id3"));

		FuzzyRule rule = new FuzzyRule("rule1");
		Composite ifRule = new IfComposite();
		ifRule.addCompositeCondition(or);
		rule.setIfCondition(ifRule);
		rule.setThenCondition("id3");
		
		SharpValue set = rule.calculate(sets, metricsReport);
		assertTrue(set != null);
		assertTrue(set.compareTo(SharpValue.valueOf("0.5")) == 0);
		return;
	}
}
