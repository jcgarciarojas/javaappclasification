package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import junit.framework.TestCase;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestFuzzySet extends TestCase {
	

	public void setUp() throws Exception
	{
		FuzzySetFactory.instance().setTypes(JUnitTestFuzzySetFactory.getSetTypes());
	}

	public void tearDown()
	{
		return;
	}
	
	public void testFuzzySet_validate_asc() throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("4,5");
		assertTrue(set.getMax().getValue().equals(10D));
		assertTrue("invalid length "+set.getValueLength(),set.getValueLength() == 2);
		assertTrue(set instanceof FuzzySetAscendent);
	}

	public void testFuzzySet_validate_desc() throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("descendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("4,5");
		assertTrue(set.getMax().getValue().equals(10D));
		assertTrue("invalid length "+set.getValueLength(),set.getValueLength() == 2);
		assertTrue(set instanceof FuzzySetDescendent);
	}

	public void testFuzzySet_validate_triangle() throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("4,5,6");
		assertTrue(set.getMax().getValue().equals(10D));
		assertTrue("invalid length "+set.getValueLength(),set.getValueLength() == 3);
		assertTrue(set instanceof FuzzySetTriangle);
	}

	public void testFuzzySet_validate_trapezoide() throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("trapezoide-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setStringValues("4,5,6,7");
		assertTrue(set.getMax().getValue().equals(10D));
		assertTrue("invalid length "+set.getValueLength(),set.getValueLength() == 4);
		assertTrue(set instanceof FuzzySetTrapezoide);
	}

	public void testFuzzySet_outOfRange() throws ConfigurationException
	{
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		try{
			set.setMax(SharpValue.valueOf(""+10));
			set.setStringValues("-1,5");
			assertTrue("failed validating negative value", false);
		} catch(ConfigurationException e)
		{
			assertTrue("validate negative value", true);
		}

		try{
			set.setStringValues("5,25");
			assertTrue("failed validating value greater than max ", false);
		} catch(ConfigurationException e)
		{
			assertTrue("validate value greater than max", true);
		}

		try{
			set.setStringValues("5,4,6");
			assertTrue("failed validating value greater than max ", false);
		} catch(ConfigurationException e)
		{
			assertTrue("validate value greater than max", true);
		}

	}
}