package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestRulesEngine extends TestCase {
	
	FuzzyRulesEngine engine;

	public void setUp() throws Exception
	{
		engine = mock(FuzzyRulesEngine.class);
		JUnitTestFuzzySetFactory.setFactory();
	}

	public void tearDown()
	{
		engine = null;
	}

	public void testEngine() throws ConfigurationException, FuzzySystemException
	{
		List<FuzzyRule> fuzzyRules = getFuzzyRules();
		Map<String, MetricReport> inputs=getMetricsInputs();

		SharpValue matchingDegrees = getSharpValues();
		when(engine.fuzzyMatching(fuzzyRules.get(0), inputs)).thenReturn(matchingDegrees);

		FuzzySet inferedSet = getFuzzySet();
		when(engine.inference(fuzzyRules.get(0), matchingDegrees)).thenReturn(inferedSet);

		List<FuzzySet> inferedSets = new ArrayList<FuzzySet>();
		inferedSets.add(inferedSet);

		ResultFuzzySet setResult = getFuzzyResult();
		when(engine.combination(inferedSets)).thenReturn(setResult);

		FuzzyValue defuzzificationValue = new FuzzyValue(getSharpValue());
		when(engine.defuzzification(setResult)).thenReturn(defuzzificationValue);

		FuzzyValue result = engine.execute(fuzzyRules, inputs);
		assertTrue(result.getSharpValue().compareTo(defuzzificationValue.getSharpValue()) == 0);
	}

	public static List<FuzzyRule> getFuzzyRules() throws ConfigurationException
	{
		List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
		rules.add(getFuzzyRule());
		return rules;
	}
	public static FuzzyRule getFuzzyRule() throws ConfigurationException
	{
		return JUnitTestRuleBasedEngine.getAndFuzzyRule();
	}
	public static FuzzySet getFuzzySet() throws ConfigurationException
	{
		return JUnitTestRuleBasedEngine.setUpFuzzySet("id", "trapezoide-set", "7,8,9,10");
	}
	public static List<FuzzySet> getFuzzySets()throws ConfigurationException
	{
		List<FuzzySet> l = new ArrayList<FuzzySet>();
		l.add(getFuzzySet());
		return l;
	}
	public static SharpValue getSharpValues() throws ConfigurationException
	{
		return (getSharpValue());
	}
	public static SharpValue getSharpValue() throws ConfigurationException
	{
		return SharpValue.valueOf("10");
	}
	public static ResultFuzzySet getFuzzyResult() throws ConfigurationException
	{
		ResultFuzzySet r = new ResultFuzzySet();
		r.add(getFuzzySet());
		return r;
	}
	public static Map<String, MetricReport> getMetricsInputs()
	{
		MetricReport r = new MetricReport();
		r.setValue(8d);
		Map<String, MetricReport> h = new Hashtable<String, MetricReport>();
		h.put("m1", r);
		return h;
	}

	public void testRulesEngine_HighResult() throws ConfigurationException, FuzzySystemException
	{
		Hashtable <String, MetricReport> m = new Hashtable<String, MetricReport>();

		//test medium
		Hashtable<String, FuzzySet> sets = new Hashtable<String, FuzzySet>();
		FuzzySet set1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set1.setMax(SharpValue.valueOf("100"));
		set1.setId("id1");
		set1.setStringValues("60,70,80");
		sets.put("id1",set1);
		MetricReport report1 = new MetricReport();
		report1.setValue(75);
		m.put("m1", report1);


		FuzzySet set2 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set2.setMax(SharpValue.valueOf("20"));
		set2.setId("id2");
		set2.setStringValues("12,14,16");
		sets.put("id2",set2);
		MetricReport report2 = new MetricReport();
		report2.setValue(15.5);
		m.put("m2", report2);


		FuzzySet result1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		result1.setMax(SharpValue.valueOf("100"));
		result1.setId("id3");
		result1.setStringValues("60,70,80");
		sets.put("id3",result1);

		List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
		FuzzyRule rule1 = new FuzzyRule("rule1");
		IfComposite IF = new IfComposite();
		AndComposite and = new AndComposite();
		and.addCompositeCondition(new ConditionComposite("m1", "is", "id1"));
		and.addCompositeCondition(new ConditionComposite("m2", "is", "id2"));
		IF.addCompositeCondition(and);
		rule1.setIfCondition(IF);
		rule1.setThenCondition("id3");
		rules.add(rule1);

		//test high
		FuzzySet set10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set10.setMax(SharpValue.valueOf("100"));
		set10.setId("id10");
		set10.setStringValues("70,80");
		sets.put("id10",set10);
		MetricReport report3 = new MetricReport();
		report3.setValue(78);
		m.put("m10", report3);

		FuzzySet set20 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set20.setMax(SharpValue.valueOf("20"));
		set20.setId("id20");
		set20.setStringValues("14,16");
		sets.put("id20",set20);
		MetricReport report4 = new MetricReport();
		report4.setValue(15.8);
		m.put("m20", report4);

		FuzzySet result10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		result10.setMax(SharpValue.valueOf("100"));
		result10.setId("id30");
		result10.setStringValues("70,80");

		sets.put("id30",result10);

		FuzzyRule rule2 = new FuzzyRule("rule2");
		IfComposite IF2 = new IfComposite();
		AndComposite and2 = new AndComposite();
		and2.addCompositeCondition(new ConditionComposite("m10", "is", "id10"));
		and2.addCompositeCondition(new ConditionComposite("m20", "is", "id20"));
		IF2.addCompositeCondition(and2);
		rule2.setIfCondition(IF2);
		rule2.setThenCondition("id30");
		rules.add(rule2);

		FuzzyRulesEngine engine = new FuzzyRuleBasedEngine();
		engine.setFuzzySets(sets);
		engine.setInferenceMethod(new ClippingMethod());
		engine.setDefuzzificationMethod(new MeanOfMax());
		FuzzyValue result = engine.execute(rules, m);
		assertTrue(result.getSharpValue().getValue().doubleValue() == 89d);

	}


	public void testRulesEngine_MediumResult() throws ConfigurationException, FuzzySystemException
	{
		Hashtable <String, MetricReport> m = new Hashtable<String, MetricReport>();

		//test medium
		Hashtable<String, FuzzySet> sets = new Hashtable<String, FuzzySet>();
		FuzzySet set1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set1.setMax(SharpValue.valueOf("100"));
		set1.setId("id1");
		set1.setStringValues("60,70,80");
		sets.put("id1",set1);
		MetricReport report1 = new MetricReport();
		report1.setValue(67);
		m.put("m1", report1);


		FuzzySet set2 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set2.setMax(SharpValue.valueOf("20"));
		set2.setId("id2");
		set2.setStringValues("12,14,16");
		sets.put("id2",set2);
		MetricReport report2 = new MetricReport();
		report2.setValue(13.8);
		m.put("m2", report2);


		FuzzySet result1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		result1.setMax(SharpValue.valueOf("100"));
		result1.setId("id3");
		result1.setStringValues("60,70,80");
		sets.put("id3",result1);

		List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
		FuzzyRule rule1 = new FuzzyRule("rule1");
		IfComposite IF1 = new IfComposite();
		AndComposite and1 = new AndComposite();
		and1.addCompositeCondition(new ConditionComposite("m1", "is", "id1"));
		and1.addCompositeCondition(new ConditionComposite("m2", "is", "id2"));
		IF1.addCompositeCondition(and1);
		rule1.setIfCondition(IF1);
		rule1.setThenCondition("id3");
		rules.add(rule1);

		//test high
		FuzzySet set10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set10.setMax(SharpValue.valueOf("100"));
		set10.setId("id10");
		set10.setStringValues("70,80");
		sets.put("id10",set10);
		MetricReport report3 = new MetricReport();
		report3.setValue(75);
		m.put("m10", report3);

		FuzzySet set20 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set20.setMax(SharpValue.valueOf("20"));
		set20.setId("id20");
		set20.setStringValues("14,16");
		sets.put("id20",set20);
		MetricReport report4 = new MetricReport();
		report4.setValue(15.5);
		m.put("m20", report4);

		FuzzySet result10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		result10.setMax(SharpValue.valueOf("100"));
		result10.setId("id30");
		result10.setStringValues("70,80");

		sets.put("id30",result10);

		FuzzyRule rule2 = new FuzzyRule("rule2");
		IfComposite IF2 = new IfComposite();
		AndComposite and2 = new AndComposite();
		and2.addCompositeCondition(new ConditionComposite("m10", "is", "id10"));
		and2.addCompositeCondition(new ConditionComposite("m20", "is", "id20"));
		IF2.addCompositeCondition(and2);
		rule2.setIfCondition(IF2);
		rule2.setThenCondition("id30");
		rules.add(rule2);

		FuzzyRulesEngine engine = new FuzzyRuleBasedEngine();
		engine.setFuzzySets(sets);
		engine.setInferenceMethod(new ClippingMethod());
		engine.setDefuzzificationMethod(new MeanOfMax());
		FuzzyValue result = engine.execute(rules, m);
		assertTrue(result.getSharpValue().getValue().doubleValue() == 70d);

	}

	public void testRulesEngine_MediumResultSameMetrics() throws ConfigurationException, FuzzySystemException
	{
		Hashtable <String, MetricReport> m = new Hashtable<String, MetricReport>();

		//test medium
		Hashtable<String, FuzzySet> sets = new Hashtable<String, FuzzySet>();
		FuzzySet set1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set1.setMax(SharpValue.valueOf("100"));
		set1.setId("id1");
		set1.setStringValues("60,70,80");
		sets.put("id1",set1);
		MetricReport report1 = new MetricReport();
		report1.setValue(67);
		m.put("m1", report1);


		FuzzySet set2 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set2.setMax(SharpValue.valueOf("20"));
		set2.setId("id2");
		set2.setStringValues("12,14,16");
		sets.put("id2",set2);
		MetricReport report2 = new MetricReport();
		report2.setValue(13.8);
		m.put("m2", report2);


		FuzzySet result1 = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		result1.setMax(SharpValue.valueOf("100"));
		result1.setId("id3");
		result1.setStringValues("60,70,80");
		sets.put("id3",result1);

		List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
		FuzzyRule rule1 = new FuzzyRule("rule1");
		IfComposite IF1 = new IfComposite();
		AndComposite and1 = new AndComposite();
		and1.addCompositeCondition(new ConditionComposite("m1", "is", "id1"));
		and1.addCompositeCondition(new ConditionComposite("m2", "is", "id2"));
		IF1.addCompositeCondition(and1);
		rule1.setIfCondition(IF1);
		rule1.setThenCondition("id3");
		rules.add(rule1);

		//test high
		FuzzySet set10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set10.setMax(SharpValue.valueOf("100"));
		set10.setId("id10");
		set10.setStringValues("70,80");
		sets.put("id10",set10);

		FuzzySet set20 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set20.setMax(SharpValue.valueOf("20"));
		set20.setId("id20");
		set20.setStringValues("14,16");
		sets.put("id20",set20);

		FuzzySet result10 = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		result10.setMax(SharpValue.valueOf("100"));
		result10.setId("id30");
		result10.setStringValues("70,80");

		sets.put("id30",result10);

		FuzzyRule rule2 = new FuzzyRule("rule2");
		IfComposite IF2 = new IfComposite();
		AndComposite and2 = new AndComposite();
		and2.addCompositeCondition(new ConditionComposite("m10", "is", "id10"));
		and2.addCompositeCondition(new ConditionComposite("m20", "is", "id20"));
		IF2.addCompositeCondition(and2);
		rule2.setIfCondition(IF2);
		rule2.setThenCondition("id30");
		rules.add(rule2);

		FuzzyRulesEngine engine = new FuzzyRuleBasedEngine();
		engine.setFuzzySets(sets);
		engine.setInferenceMethod(new ClippingMethod());
		engine.setDefuzzificationMethod(new MeanOfMax());
		FuzzyValue result = engine.execute(rules, m);
		assertTrue(result.getSharpValue().getValue().doubleValue() == 70d);
	}
}
