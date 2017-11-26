package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetType;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestFuzzySetFactory extends TestCase {
	
	private FuzzySetFactory factory;

	public void setUp() throws ConfigurationException
	{
		setFactory();
		factory = FuzzySetFactory.instance();
	}
	
	public static void setFactory()throws ConfigurationException
	{
		FuzzySetFactory.instance().setTypes(getSetTypes());
	}
	
	public static List<FuzzySetType> getSetTypes() throws ConfigurationException
	{
		FuzzySetType type = null;
		List<FuzzySetType> l = new ArrayList<FuzzySetType>();
		
		type = new FuzzySetType("ascendent-set");
		type.setClassName("edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetAscendent");
		l.add(type);
		
		type = new FuzzySetType("descendent-set");
		type.setClassName("edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetDescendent");
		l.add(type);

		type = new FuzzySetType("triangle-set");
		type.setClassName("edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetTriangle");
		l.add(type);

		type = new FuzzySetType("trapezoide-set");
		type.setClassName("edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetTrapezoide");
		l.add(type);
		
		return l;
		
	}

	public void tearDown()
	{
		factory = null;
	}

	public void testMergeFuzzySets_lineAsc_test1() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("ascendent-set");
		set1.setMax(SharpValue.valueOf(""+10));
		set1.setStringValues("1,2");
		FuzzySet set2 = factory.createFuzzySetByType("ascendent-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("3,4");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 1d);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 2);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}

	public void testMergeFuzzySets_lineDesc_test1() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("descendent-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("1,2");
		FuzzySet set2 = factory.createFuzzySetByType("descendent-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("3,4");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 4);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}

	public void testMergeFuzzySets_Triangle_test1() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("triangle-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("1,3,5");
		FuzzySet set2 = factory.createFuzzySetByType("triangle-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("2,3,4");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 1);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getValues().get(2).getSharpValue().getValue().doubleValue() == 5);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}

	public void testMergeFuzzySets_Trapezoide_test1() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("trapezoide-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("1,3,5,7");
		FuzzySet set2 = factory.createFuzzySetByType("trapezoide-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("2,3,4,5");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 1);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getValues().get(2).getSharpValue().getValue().doubleValue() == 5);
		assertTrue(set.getValues().get(3).getSharpValue().getValue().doubleValue() == 7);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}

	public void testMergeFuzzySets_Trapezoide_test2() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("trapezoide-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("3,4,5,6");
		FuzzySet set2 = factory.createFuzzySetByType("ascendent-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("2,3");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 2);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}
	
	public void testMergeFuzzySets_Trapezoide_test3() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("trapezoide-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("3,4,5,6");
		FuzzySet set2 = factory.createFuzzySetByType("ascendent-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("2,3");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 2);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}

	public void testMergeFuzzySets_Trapezoide_test4() throws ConfigurationException
	{
		FuzzySet set1 = factory.createFuzzySetByType("trapezoide-set");
		set1.setMax(SharpValue.valueOf(""+""+10));
		set1.setStringValues("3,4,5,6");
		FuzzySet set2 = factory.createFuzzySetByType("triangle-set");
		set2.setMax(SharpValue.valueOf(""+""+10));
		set2.setStringValues("3,5,7");
		
		FuzzySet set = factory.mergeFuzzySets(set1, set2);
		assertTrue(set.getValues().get(0).getSharpValue().getValue().doubleValue() == 3);
		assertTrue(set.getValues().get(1).getSharpValue().getValue().doubleValue() == 4);
		assertTrue(set.getValues().get(2).getSharpValue().getValue().doubleValue() == 5);
		assertTrue(set.getValues().get(3).getSharpValue().getValue().doubleValue() == 7);
		assertTrue(set.getMax().getValue().doubleValue() == 10);
	}
}
