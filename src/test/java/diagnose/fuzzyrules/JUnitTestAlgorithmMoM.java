package diagnose.fuzzyrules;


import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.MeanOfMax;
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

public class JUnitTestAlgorithmMoM extends TestCase {
	
	private MeanOfMax d = null;
	public void setUp() throws Exception
	{
		JUnitTestFuzzySetFactory.setFactory();
		d = new MeanOfMax();
		return;
	}

	public void tearDown()
	{
		d = null;
		return;
	}

	public void testLineAscMOM() throws ConfigurationException, FuzzySystemException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("7,8");
		FuzzyValue result = d.getMaxOfMean(set);
		assertTrue(result.getSharpValue().getValue() == 9d);
	}

	public void testLineDescMOM() throws ConfigurationException, FuzzySystemException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("descendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("7,8");
		FuzzyValue result = d.getMaxOfMean(set);
		assertTrue(result.getSharpValue().getValue() == 3.5d);
	}

	public void testTriangleMOM() throws ConfigurationException, FuzzySystemException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("7,8,9");
		FuzzyValue result = d.getMaxOfMean(set);
		assertTrue(result.getSharpValue().getValue() == 8d);
	}

	public void testTrapezoideMOM() throws ConfigurationException, FuzzySystemException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("trapezoide-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("6,7,8,9");
		FuzzyValue result = d.getMaxOfMean(set);
		assertTrue(result.getSharpValue().getValue() == 7.5d);
	}

}