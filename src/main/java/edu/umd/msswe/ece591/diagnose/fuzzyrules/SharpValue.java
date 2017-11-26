package edu.umd.msswe.ece591.diagnose.fuzzyrules;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class SharpValue {
	
	private Double value;

	public SharpValue(Double value)
	{
		this.value = value;
	}

	public SharpValue(String value)
	{
		this.value = Double.valueOf(value);
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static SharpValue valueOf(String value)
	{
		return new SharpValue(value);
	}

	/**
	 * returns 0 if this value equal to otherValue; 
	 * less than 0 if this value is numerically less than anothervalue; 
	 * and greater than 0 if this value is numerically greater than anotherValue.
	 * @param value
	 * @return
	 */
	public int compareTo(SharpValue otherValue)
	{
		return this.value.compareTo(otherValue.getValue());
	}
	
	/**
	 * 
	 */
	public String toString()
	{
		return value.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public SharpValue cloneObject()
	{
		return new SharpValue(value);
	}
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static SharpValue getMax(SharpValue s1, SharpValue s2)
	{
		return (s1.compareTo(s2) > 0) ? s1: s2;
	}

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static SharpValue getMin(SharpValue s1, SharpValue s2)
	{
		return (s1.compareTo(s2) < 0) ? s1: s2;
	}
}
