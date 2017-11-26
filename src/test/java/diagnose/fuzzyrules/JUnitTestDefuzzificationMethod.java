package diagnose.fuzzyrules;

import diagnose.configuration.JUnitTestFuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
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

public class JUnitTestDefuzzificationMethod  extends TestCase {

	private DefuzzificationMethod meanOfMax = null;
	public void setUp() throws Exception
	{
		JUnitTestFuzzySetFactory.setFactory();
		meanOfMax = new MeanOfMax();
	}

	public void tearDown()
	{
		meanOfMax = null;
	}
	
	public void testDefuzzificationMethod_test1() 
		throws FuzzySystemException, ConfigurationException
	{
		ResultFuzzySet set = this.getResultSet();
		FuzzyValue c = meanOfMax.calculate(set);

		assertTrue("Validating value "+5d+", but it was return "+c.getSharpValue().getValue(), c.getSharpValue().getValue().doubleValue() == 5d);
	}

	private ResultFuzzySet getResultSet() throws ConfigurationException
	{
		FuzzySet set = null;
		ResultFuzzySet total = new ResultFuzzySet();

		set = FuzzySetFactory.instance().createFuzzySetByType("ascendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setFuzzyValues("4/0,5/0.5");
		total.add(set);

		set = FuzzySetFactory.instance().createFuzzySetByType("descendent-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setFuzzyValues("4/0.5,5/0");
		total.add(set);

		set = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setFuzzyValues("4/0,5/1,6/0");
		total.add(set);

		set = FuzzySetFactory.instance().createFuzzySetByType("trapezoide-set");
		set.setMax(SharpValue.valueOf(""+10));
		set.setFuzzyValues("4/0,5/0.5,6/0.5,8/0");
		total.add(set);

		return total;
	}

}