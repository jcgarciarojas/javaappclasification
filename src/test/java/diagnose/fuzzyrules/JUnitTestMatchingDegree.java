package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import junit.framework.TestCase;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestMatchingDegree extends TestCase {
	

	public void setUp() throws Exception
	{
		JUnitTestFuzzySetFactory.setFactory();
		return;
	}

	public void tearDown()
	{
		return;
	}

	public void testMatchingLineAscInference_test1() throws Exception
	{
		SharpValue input,output = null;
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("5,6");

		input = new SharpValue(6d);

		output = set.getMatchingDegree(input);
		assertTrue("validating output equal to max degree ", output.getValue() == 1d);

		input = new SharpValue(5.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0.5d);

		input = new SharpValue(10d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 1d);

		input = new SharpValue(4d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);
	}

	public void testMatchingLineDescInference_test1() throws Exception
	{
		SharpValue input,output = null;
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("descendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("5,6");


		input = new SharpValue(4d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 1d);

		input = new SharpValue(5.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0.5d);

		input = new SharpValue(7d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);
	}

	public void testMatchingTriangleInference_test1() throws Exception
	{
		SharpValue input,output = null;
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("5,6,7");


		input = new SharpValue(7d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(4d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(8d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(6d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 1d);

		input = new SharpValue(5.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0.5d);

		input = new SharpValue(6.8d);
		output = set.getMatchingDegree(input);
		assertTrue(
				(output.getValue() > 0.1 && output.getValue() < 0.3));

	}

	public void testMatchingTrapezoideInference_test1() throws Exception
	{
		SharpValue input,output = null;
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("trapezoide-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("5,6,7,8");


		input = new SharpValue(6.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 1d);

		input = new SharpValue(5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(8d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(5.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0.5d);

		input = new SharpValue(4d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);

		input = new SharpValue(7.5d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0.5d);
		
		input = new SharpValue(9d);
		output = set.getMatchingDegree(input);
		assertTrue(output.getValue() == 0d);
	}

}
