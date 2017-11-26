package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzySetTriangle extends FuzzySet {

	/**
	 * 
	 * @throws ConfigurationException
	 */
	public FuzzySetTriangle() throws ConfigurationException
	{
		super();
	}

	/**
	 * 
	 * @param max
	 * @throws ConfigurationException
	 */
	public FuzzySetTriangle(SharpValue max) throws ConfigurationException
	{
		super(max);
	}
	
	/**
	 * 
	 */
	public void setStringValues(String strValues) throws ConfigurationException 
	{
		List<SharpValue> l = this.getSharpValues(strValues);
		if (l.size() != 3)
			throw new ConfigurationException("triangle-set must have three values "+l);
		
		this.getValues().add(new FuzzyValue(this.validateValue(l.get(0)), SharpValue.valueOf("0")));
		this.getValues().add(new FuzzyValue(this.validateValue(l.get(1)), SharpValue.valueOf("1")));
		this.getValues().add(new FuzzyValue(this.validateValue(l.get(2)), SharpValue.valueOf("0")));
	}
	
	/**
	 * 
	 */
	public void setArrayValues(List<FuzzyValue> values) throws ConfigurationException 
	{
		if (values.size() != 3)
			throw new ConfigurationException("size of values for this fuzzys set must be three ");
		
		if(values.get(0).getDegree().compareTo(SharpValue.valueOf("0")) != 0)
			throw new ConfigurationException("degree of the first element in this fuzzy set must be zero ");

		if(values.get(1).getDegree().compareTo(SharpValue.valueOf("0")) == 0)
			throw new ConfigurationException("degree of second element in this fuzzy set must be different to zero ");
		
		if(values.get(2).getDegree().compareTo(SharpValue.valueOf("0")) != 0)
			throw new ConfigurationException("degree of the last element in this fuzzy set must be zero ");

		for(FuzzyValue fuzzyValue: values)
			this.validateValue(fuzzyValue.getSharpValue());
		
		super.setValues(values);
	}
	
	public List<FuzzyValue> getSquareValues()
	{
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(this.getValues().get(0));//a
		l.add(this.getValues().get(1));//b
		l.add(this.getValues().get(1));//c
		l.add(this.getValues().get(2));//d
		return l;
	}
	
	public List<FuzzyValue> getPeakValues()
	{
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(this.getValues().get(1));//b
		l.add(this.getValues().get(1));//b
		return l;
	}

	/**
	 * 
	 */
	public SharpValue getMatchingDegree(SharpValue input) throws ConfigurationException
	{
		Double y = null;

		double x = input.getValue();
		double x1 = this.getValues().get(0).getSharpValue().getValue();
		double y1 = this.getValues().get(0).getDegree().getValue();
		double x2 = this.getValues().get(1).getSharpValue().getValue();
		double y2 = this.getValues().get(1).getDegree().getValue();
		double x3 = this.getValues().get(2).getSharpValue().getValue();
		double y3 = this.getValues().get(2).getDegree().getValue();

		if (x <= x1) y = 0D;
		else if (x == x2) y = 1D;
		else if (x >= x3) y = 0D;
		else if(x > x1 && x < x2)
			y = this.getIntersecionYPoint(x, x1, y1, x2, y2);
		else if(x > x2 && x < x3)
			y = this.getIntersecionYPoint(x, x2, y2, x3, y3);
		else 
		{
			y = this.getIntersecionYPoint(x, x1, y1, x2, y2);
			throw new ConfigurationException("Error calculating intersection for input "+
					input + " in fuzzy set "+this.toString());
		}
		
		return new SharpValue(y);
	}
	
	/**
	 * 
	 */
	public List<FuzzyValue> getMatchingInput(SharpValue inputDegree) throws ConfigurationException
	{
		Double y = inputDegree.getValue();
		double x1 = this.getValues().get(0).getSharpValue().getValue();
		double y1 = this.getValues().get(0).getDegree().getValue();
		double x2 = this.getValues().get(1).getSharpValue().getValue();
		double y2 = this.getValues().get(1).getDegree().getValue();
		double x3 = this.getValues().get(2).getSharpValue().getValue();
		double y3 = this.getValues().get(2).getDegree().getValue();

		SharpValue x21 = new SharpValue(this.getIntersecionXPoint(y, x1, y1, x2, y2));
		SharpValue x31 = new SharpValue(this.getIntersecionXPoint(y, x2, y2, x3, y3));
		
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(this.getValues().get(0));
		if (x21.compareTo(x31) != 0)
		{
			l.add(new FuzzyValue(x21, inputDegree));
			l.add(new FuzzyValue(x31, inputDegree));
		} else
		{
			l.add(new FuzzyValue(x21, inputDegree));
		}
		l.add(this.getValues().get(2));
		return l;
	}	
	/**
	 * 
	 */
	public FuzzySet cloneObject() throws ConfigurationException
	{
		FuzzySet set = null;

		try {
			set = new FuzzySetTriangle();
			set.setId(this.getId());
			set.setLabel(this.getLabel());
			set.setMax(this.getMax());
			set.setMin(this.getMin());
			for (FuzzyValue v : this.getValues())
				set.getValues().add(v.cloneObject());
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}		
		return set;
	}

}