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

public class FuzzySetDescendent extends FuzzySet {

	/**
	 * 
	 * @param max
	 * @throws ConfigurationException
	 */
	public FuzzySetDescendent() throws ConfigurationException
	{
		super();
	}

	/**
	 * 
	 * @param max
	 * @throws ConfigurationException
	 */
	public FuzzySetDescendent(SharpValue max) throws ConfigurationException
	{
		super(max);
	}
	
	/**
	 * 
	 */
	public void setStringValues(String strValues) throws ConfigurationException 
	{
		List<SharpValue> l =this.getSharpValues(strValues);
		if (l.size() != 2)
			throw new ConfigurationException("descedent-set must have only two values "+l);
		
		this.getValues().add(new FuzzyValue(this.validateValue(l.get(0)), SharpValue.valueOf("1")));
		this.getValues().add(new FuzzyValue(this.validateValue(l.get(1)), SharpValue.valueOf("0")));
	}

	/**
	 * 
	 */
	public void setArrayValues(List<FuzzyValue> values) throws ConfigurationException 
	{
		if (values.size() != 2)
			throw new ConfigurationException("size of values for this fuzzys set must be two ");
		
		if(values.get(0).getDegree().compareTo(SharpValue.valueOf("0")) == 0)
			throw new ConfigurationException("degree of first element in this fuzzy set must be zero ");

		if(values.get(1).getDegree().compareTo(SharpValue.valueOf("0")) != 0)
			throw new ConfigurationException("degree of second element in this fuzzy set must be different to zero ");
		
		for(FuzzyValue fuzzyValue: values)
			this.validateValue(fuzzyValue.getSharpValue());
		
		this.setValues(values);
	}

	public List<FuzzyValue> getSquareValues()
	{
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(new FuzzyValue(this.getMin(), this.getValues().get(1).getDegree()));//min,zero
		l.add(new FuzzyValue(this.getMin(), this.getValues().get(0).getDegree()));//min,one
		l.add(this.getValues().get(0));//a
		l.add(this.getValues().get(1));//b
		return l;
	}

	/**
	 * 
	 */
	public List<FuzzyValue> getPeakValues()
	{
		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(new FuzzyValue(this.getMin(), this.getValues().get(0).getDegree()));//max
		l.add(this.getValues().get(0));//b
		return l;
	}

	/**
	 * 
	 */
	public SharpValue getMatchingDegree(SharpValue input)
	{
		Double y = null;

		double x = input.getValue();
		double x1 = this.getValues().get(0).getSharpValue().getValue();
		double y1 = this.getValues().get(0).getDegree().getValue();
		double x2 = this.getValues().get(1).getSharpValue().getValue();
		double y2 = this.getValues().get(1).getDegree().getValue();

		if (x <= x1) y = 1D;
		else if (x >= x2) y = 0D;
		else y = this.getIntersecionYPoint(x, x1, y1, x2, y2);

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
		SharpValue x = new SharpValue(this.getIntersecionXPoint(y, x1, y1, x2, y2));

		List<FuzzyValue> l = new ArrayList<FuzzyValue>();
		l.add(new FuzzyValue(x, inputDegree));
		l.add(this.getValues().get(1));
		return l;
	}
	
	/**
	 * 
	 */
	public FuzzySet cloneObject() throws ConfigurationException
	{
		FuzzySet set = null;

		try {
			set = new FuzzySetDescendent();
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
