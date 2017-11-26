package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import junit.framework.TestCase;

import java.util.*;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestRuleBasedEngine extends TestCase 
{

	private static FuzzySetFactory factory;
	private FuzzyRuleBasedEngine engine;
	public void setUp() throws Exception
	{
		factory = FuzzySetFactory.instance();
		factory.setTypes(JUnitTestFuzzySetFactory.getSetTypes());

		Map<String, FuzzySet> fuzzySets = setUpFuzzySets();
		engine = new FuzzyRuleBasedEngine();
		engine.setFuzzySets(fuzzySets);
		engine.setDefuzzificationMethod(new MeanOfMax());
		engine.setInferenceMethod(new ClippingMethod());
	}

	public void tearDown()
	{
		engine = null;
	}

	public static Map<String, FuzzySet> setUpFuzzySets() throws ConfigurationException
	{
		Map<String, String> Map = new Hashtable<String, String>();
		Map.put("ascendent-set", "7,8");
		Map.put("triangle-set", "8,9,10");
		return getFuzzySets(Map);
	}

	public void testFuzzyMatching_twovalidrules() throws ConfigurationException, FuzzySystemException
	{
		Vector<Double> v = new Vector<Double>();
		v.add(9d);
		v.add(7.5d);
		Map<String, MetricReport> inputs = getMetricReport(v);
		SharpValue output = this.processEngine(inputs);
		assertTrue(output != null && output.compareTo(SharpValue.valueOf("0")) != 0);
		return;
	}

	public void testFuzzyMatching_zerovaluerule() throws ConfigurationException, FuzzySystemException
	{
		Vector<Double> v = new Vector<Double>();
		v.add(5d);
		v.add(9.5d);
		Map<String, MetricReport> inputs = getMetricReport(v);
		SharpValue output = this.processEngine(inputs);
		assertTrue(output != null && output.compareTo(SharpValue.valueOf("0")) == 0);
		return;
	}

	private SharpValue processEngine(Map<String, MetricReport> inputs) throws ConfigurationException, FuzzySystemException
	{
		FuzzyRule rule = getAndFuzzyRule();
		engine.preProcess();
		return engine.fuzzyMatching(rule, inputs);
	}


	public void testInference_test1() throws ConfigurationException, FuzzySystemException
	{
		Vector<Double> v = new Vector<Double>();
		v.add(9.3d);
		v.add(7.5d);
		//id1 8,9,10 and id2 7,8
		//expecting m1 = 9.3 and m2 = 7.5
		Map<String, MetricReport> inputs = getMetricReport(v);
		//expecting output 0.7 and 0.5
		SharpValue output = this.processEngine(inputs);
		assertTrue("assert return "+output,output != null && output.compareTo(SharpValue.valueOf("0.5")) == 0);
		//if m1 is id1 and m2 is id2 then id2
		FuzzyRule rule = getAndFuzzyRule();
		FuzzySet set = engine.inference(rule, output);
		assertTrue(set.getValueLength() == 2);
		assertTrue(set.getHighestDegree().getValue().doubleValue() == 0.5);
		return;
	}

	public void testInference_test2() throws ConfigurationException, FuzzySystemException
	{
		Vector<Double> v = new Vector<Double>();
		v.add(9.3d);
		v.add(7.5d);
		//id1 8,9,10 and id2 7,8
		//expecting m1 = 9.3 and m2 = 7.5
		Map<String, MetricReport> inputs = getMetricReport(v);
		//expecting output 0.7 and 0.5

		FuzzyRule rule = getOrFuzzyRule();
		engine.preProcess();
		SharpValue output = engine.fuzzyMatching(rule, inputs);

		assertTrue("assert return "+output,output != null && output.compareTo(SharpValue.valueOf("0.5")) > 0 );


		rule = getAndOrFuzzyRule();
		engine.preProcess();
		output = engine.fuzzyMatching(rule, inputs);
		assertTrue("assert return "+output,output != null && output.compareTo(SharpValue.valueOf("0.5")) == 0 );

		rule = getOrAndFuzzyRule();
		engine.preProcess();
		output = engine.fuzzyMatching(rule, inputs);
		assertTrue("assert return "+output,output != null && output.compareTo(SharpValue.valueOf("0.5")) > 0 );

		return;
	}

	/**
	 *
	 * @param Map
	 * @return
	 * @throws ConfigurationException
	 */
	public static Map<String, FuzzySet> getFuzzySets(Map<String, String> map) throws ConfigurationException
	{
		Map<String, FuzzySet> sets = new Hashtable<String, FuzzySet>();
		FuzzySet fuzzySet = null;

		Iterator it = map.keySet().iterator();
		int i=1;
		while(it.hasNext())
		{
			String type = (String)it.next();
			fuzzySet = setUpFuzzySet("id"+i, type, map.get(type));
			sets.put("id"+i, fuzzySet);
			i++;
		}

		return sets;
	}

	public static FuzzySet setUpFuzzySet(String id, String type, String values) throws ConfigurationException
	{
		FuzzySet fuzzySet = FuzzySetFactory.instance().createFuzzySetByType(type);

		fuzzySet.setMax(SharpValue.valueOf(""+10));
		fuzzySet.setId(id);
		fuzzySet.setStringValues(values);
		return fuzzySet;
	}

	public static Map<String, MetricReport> getMetricReport(List<Double> values)
	{
		MetricReport report = null;
		Map<String, MetricReport> r = new Hashtable<String, MetricReport>();

		int i=1;
		for(Double d:values)
		{
			report = new MetricReport();
			report.setValue(d.doubleValue());
			r.put("m"+(i++), report);
		}
		return r;
	}

	public static FuzzyRule getAndFuzzyRule() throws ConfigurationException
	{

		FuzzyRule rule = new FuzzyRule("test");
		Composite IF = new IfComposite();
		Composite and = new AndComposite();
		and.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		and.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		IF.addCompositeCondition(and);

		rule.setIfCondition(IF);
		rule.setThenCondition("id2");
		return rule;
	}

	public static FuzzyRule getOrFuzzyRule() throws ConfigurationException
	{

		FuzzyRule rule = new FuzzyRule("test");
		Composite IF = new IfComposite();
		Composite or = new OrComposite();
		or.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		or.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		IF.addCompositeCondition(or);

		rule.setIfCondition(IF);
		rule.setThenCondition("id2");
		return rule;
	}


	public static FuzzyRule getAndOrFuzzyRule() throws ConfigurationException
	{

		FuzzyRule rule = new FuzzyRule("test");
		Composite IF = new IfComposite();
		Composite and = new AndComposite();
		and.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		and.addCompositeCondition(new ConditionComposite("m2","is", "id2"));

		Composite or = new OrComposite();
		or.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		or.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		and.addCompositeCondition(or);
		
		IF.addCompositeCondition(and);

		rule.setIfCondition(IF);
		rule.setThenCondition("id2");
		return rule;
	}

	public static FuzzyRule getOrAndFuzzyRule() throws ConfigurationException
	{
		
		FuzzyRule rule = new FuzzyRule("test");
		Composite IF = new IfComposite();
		Composite or = new OrComposite();
		Composite and = new AndComposite();
		and.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		and.addCompositeCondition(new ConditionComposite("m1","is", "id1"));
		or.addCompositeCondition(and);

		Composite and1 = new AndComposite();
		and1.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		and1.addCompositeCondition(new ConditionComposite("m2","is", "id2"));
		or.addCompositeCondition(and1);

		IF.addCompositeCondition(or);

		rule.setIfCondition(IF);
		rule.setThenCondition("id2");
		return rule;
	}
}
