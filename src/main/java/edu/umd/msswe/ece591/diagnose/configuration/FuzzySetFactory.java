package edu.umd.msswe.ece591.diagnose.configuration;


import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetType;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzySetFactory {
	
	List<FuzzySetType> types;

	private static FuzzySetFactory instance = null;
	private FuzzySetFactory()
	{
		types = new  ArrayList<FuzzySetType>();
	}
	
	public static synchronized FuzzySetFactory instance()
	{
		if (instance == null)
		{
			instance = new FuzzySetFactory();
		}
		return instance;
	}
	
	/**
	 * this method merge two fuzzy sets
	 * @param fuzzySet1
	 * @param fuzzySet2
	 * @return
	 * @throws ConfigurationException
	 */
	public FuzzySet mergeFuzzySets(FuzzySet fuzzySet1, FuzzySet fuzzySet2) throws ConfigurationException
	{
		if (fuzzySet1.getMax().compareTo(fuzzySet2.getMax()) != 0)
			throw new ConfigurationException("Max value should be the same for the fuzzy sets");
		
		SharpValue zero= SharpValue.valueOf("0");
		SharpValue max = new SharpValue(Math.max(fuzzySet1.getMax().getValue(), fuzzySet1.getMax().getValue())); 

		List<FuzzyValue> sqValues1 = fuzzySet1.getSquareValues();
		List<FuzzyValue> sqValues2 = fuzzySet2.getSquareValues();

		FuzzyValue a = this.getMin(sqValues1.get(0), sqValues2.get(0));
		FuzzyValue b = this.getMin(sqValues1.get(1), sqValues2.get(1));
		FuzzyValue c = this.getMax(sqValues1.get(2), sqValues2.get(2));
		FuzzyValue d = this.getMax(sqValues1.get(3), sqValues2.get(3));
		
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(a);
		l.add(b);
		l.add(c);
		l.add(d);			
		if (a.getSharpValue().compareTo(zero)==0 && b.getSharpValue().compareTo(zero)==0)
		{
			l.remove(a);
			l.remove(b);
		}
		else if (c.getSharpValue().compareTo(max)==0 && d.getSharpValue().compareTo(max)==0)
		{
			l.remove(c);
			l.remove(d);
		}
		else if (b.getSharpValue().compareTo(c.getSharpValue())==0)
			l.remove(b);

		return createFuzzySetByValue(max, l);
	}
	
	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private FuzzyValue getMin(FuzzyValue value1, FuzzyValue value2)
	{
		return (value1.getSharpValue().compareTo(value2.getSharpValue()) < 0) ? value1 : value2; 
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private FuzzyValue getMax(FuzzyValue value1, FuzzyValue value2)
	{
		return (value1.getSharpValue().compareTo(value2.getSharpValue()) > 0) ? value1 : value2; 
	}
		
	/**
	 * 
	 * @param max
	 * @param values
	 * @return
	 * @throws ConfigurationException
	 */
	public FuzzySet createFuzzySetByValue(SharpValue max, List<FuzzyValue> values) throws ConfigurationException
	{
		FuzzySet newSet= null;
		for(FuzzySetType setType: types)
		{
			newSet = setType.getFuzzySet().cloneObject();
			newSet.setMax(max);
			if (this.setFuzzySet(newSet, values))
				break;
			else
				newSet = null;
			
		}
		return newSet;
	}
	
	/**
	 * 
	 * @param newSet
	 * @param values
	 * @return
	 */
	private boolean setFuzzySet(FuzzySet newSet, List<FuzzyValue> values)
	{
		boolean assigned = true; 
		try {
			newSet.setArrayValues(values);
		} catch (ConfigurationException e) {
			assigned = false;
		}		
		return assigned;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws ConfigurationException
	 */
	public FuzzySet createFuzzySetByType(String type) throws ConfigurationException
	{
		FuzzySet newSet = null;
		FuzzySet set = getFuzzySetType(type);
		if (set != null)
			newSet = set.cloneObject();
		
		return newSet;

	}
	
	/**
	 * 
	 * @param strType
	 * @return
	 */
	private FuzzySet getFuzzySetType(String strType)
	{
		FuzzySet set = null;
		for(FuzzySetType setType: types)
		{
			if (setType.getType() != null && setType.getType().equalsIgnoreCase(strType))
			{
				set = setType.getFuzzySet();
				break;
			}
		}
		return set;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<FuzzySetType> types) {
		this.types = types;
	}

}

