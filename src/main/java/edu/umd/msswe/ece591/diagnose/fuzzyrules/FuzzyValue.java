package edu.umd.msswe.ece591.diagnose.fuzzyrules;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzyValue {

	private SharpValue value;
	private SharpValue degree;
	private static SharpValue zero = SharpValue.valueOf("0");
	private static FuzzyValue ZERO_FUZZY_SET = new FuzzyValue(zero,zero);
	
	public FuzzyValue(SharpValue value, SharpValue degree)
	{
		this.value = value;
		this.degree = degree;
	}

	public FuzzyValue(SharpValue value)
	{
		this(value, SharpValue.valueOf("1"));
	}

	public FuzzyValue(String value)
	{
		this(SharpValue.valueOf(value));
	}

	public FuzzyValue(String value, String degree)
	{
		this(SharpValue.valueOf(value), SharpValue.valueOf(degree));
	}

	/**
	 * @return the degree
	 */
	public SharpValue getDegree() {
		return degree;
	}

	/**
	 * @return the value
	 */
	public SharpValue getSharpValue() {
		return value;
	}
	
	public String toString()
	{
		return value+"/"+degree;
	}
	
	public boolean equalTo(FuzzyValue fuzzyValue)
	{
		return(this.value.compareTo(fuzzyValue.getSharpValue()) == 0 &&
				this.degree.compareTo(fuzzyValue.getDegree()) == 0);
	}
	
	public FuzzyValue cloneObject()
	{
		return new FuzzyValue(value.cloneObject(), degree.cloneObject());
	}
	
	public static FuzzyValue getZeroFuzzyValue()
	{
		return ZERO_FUZZY_SET;
	}
	
	/**
	 * 
	 * @param otherValue
	 * @return
	 */
	public int toCompare(FuzzyValue otherValue)
	{
		int compare = this.getSharpValue().compareTo(otherValue.getSharpValue());
		if (compare != 0)
			return compare;
		else
			return this.getDegree().compareTo(otherValue.getDegree());
		
	}
}
