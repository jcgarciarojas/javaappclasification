package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import junit.framework.TestCase;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestLine extends TestCase {
	
	public void setUp() throws ConfigurationException
	{
		JUnitTestFuzzySetFactory.setFactory();
	}

	public void testGetIntersecionYPoint_positive() throws ConfigurationException
	{
		FuzzySet set =  FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		Double y = set.getIntersecionYPoint(5d, 1d, 0d, 7d, 3d);
		assertTrue("Validate output is expected", y == 2d);
	}

	public void testGetIntersecionYPoint_negative() throws ConfigurationException
	{
		FuzzySet set =  FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		Double y = set.getIntersecionYPoint(0d, 1d, 0d, 7d, 3d);
		assertTrue("Validate output is expected", y == -0.5d);
	}

	public void testGetIntersecionXPoint_positive() throws ConfigurationException
	{
		FuzzySet set =  FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		Double y = set.getIntersecionXPoint(2d, 1d, 0d, 7d, 3d);
		assertTrue("Validate output is expected", y == 5d);
	}
	
	public void testGetIntersecionXPoint_negative() throws ConfigurationException
	{
		FuzzySet set =  FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		Double y = set.getIntersecionXPoint(-0.5d, 1d, 0d, 7d, 3d);
		assertTrue("Validate output is expected", y == 0d);
	}
}
