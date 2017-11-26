package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import junit.framework.TestCase;

import java.util.Hashtable;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestInferenceAlgorithm  extends TestCase {
	

	public void setUp() throws Exception
	{
		JUnitTestFuzzySetFactory.setFactory();
	}

	public void tearDown()
	{
		return;
	}

	public void testLineAscInference() throws Exception
	{
		FuzzyRuleBasedEngine engine = new FuzzyRuleBasedEngine();
		engine.setInferenceMethod(new ClippingMethod());
		engine.setFuzzySets(getFuzzySetTable("ascendent-set", "5,6"));
		engine.setInferenceMethod(new ClippingMethod());
		FuzzySet s = engine.inference(getRule("id"), getInput("0.7"));
		assertTrue(s instanceof FuzzySetAscendent);
		assertTrue(s.getValues().get(0).getSharpValue().getValue().equals(5d));
		assertTrue(s.getHighestDegree().getValue() == 0.7d);
	}

	private Hashtable<String, FuzzySet> getFuzzySetTable(String type, String values) throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType(type);
		set.setMax(SharpValue.valueOf(""+10));
		set.setId("id");
		set.setLabel("id");
		set.setStringValues(values);
		Hashtable<String, FuzzySet> h = new Hashtable<String, FuzzySet>();
		h.put("id", set);
		return h;
	}
	private SharpValue getInput(String input) throws ConfigurationException
	{
		return SharpValue.valueOf(input);
	}

	private FuzzyRule getRule(String id)
	{
		FuzzyRule r = new FuzzyRule(id);
		r.setThenCondition(id);
		return r;
	}

	public void testLineDescInference() throws Exception
	{
		FuzzyRuleBasedEngine engine = new FuzzyRuleBasedEngine();
		engine.setFuzzySets(getFuzzySetTable("descendent-set", "5,6"));
		engine.setInferenceMethod(new ClippingMethod());
		FuzzySet s = engine.inference(getRule("id"), getInput("0.5"));
		assertTrue(s instanceof FuzzySetDescendent);
		assertTrue(s.getValues().get(1).getSharpValue().getValue().equals(6d));
		assertTrue(s.getHighestDegree().getValue() == 0.5d);
	}

	public void testTriangleInference() throws Exception
	{
		FuzzyRuleBasedEngine engine = new FuzzyRuleBasedEngine();
		engine.setInferenceMethod(new ClippingMethod());
		engine.setFuzzySets(getFuzzySetTable("triangle-set", "5,6,7"));

		FuzzySet s = engine.inference(getRule("id"), getInput("0.5"));
		assertTrue(s instanceof FuzzySetTrapezoide);
		assertTrue(s.getValues().get(0).getSharpValue().getValue().equals(5d));
		assertTrue(s.getValues().get(3).getSharpValue().getValue().equals(7d));
		assertTrue(s.getHighestDegree().getValue() == 0.5d);
	}

	public void testTriangleInference2() throws Exception
	{
		FuzzyRuleBasedEngine engine = new FuzzyRuleBasedEngine();
		engine.setInferenceMethod(new ClippingMethod());
		engine.setFuzzySets(getFuzzySetTable("triangle-set", "5,6,7"));
		FuzzySet s = engine.inference(getRule("id"), getInput("1"));
		assertTrue(s instanceof FuzzySetTriangle);
		assertTrue(s.getValues().get(0).getSharpValue().getValue().equals(5d));
		assertTrue(s.getValues().get(2).getSharpValue().getValue().equals(7d));
		assertTrue(s.getHighestDegree().getValue() == 1d);
	}

	public void testTrapezoideInference() throws Exception
	{
		FuzzyRuleBasedEngine engine = new FuzzyRuleBasedEngine();
		engine.setInferenceMethod(new ClippingMethod());
		engine.setFuzzySets(getFuzzySetTable("trapezoide-set", "3,4,5,6"));
		FuzzySet s = engine.inference(getRule("id"), getInput("0.6"));
		assertTrue(s instanceof FuzzySetTrapezoide);
		assertTrue(s.getValues().get(0).getSharpValue().getValue().equals(3d));
		assertTrue(s.getValues().get(3).getSharpValue().getValue().equals(6d));
		assertTrue(s.getHighestDegree().getValue() == 0.6d);

	}
}