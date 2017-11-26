package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzyOutput {

	private String minValue;
	private String maxValue;
	private SharpValue sharpMaxValue;
	private SharpValue sharpMinValue;
	private String label;
	
	public FuzzyOutput(String label)
	{
		this.label = label;
	}
	/**
	 * @return the maxValue
	 */
	public String getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(String maxValue) {
		if (maxValue != null)
		{
			this.maxValue = maxValue;
			this.sharpMaxValue = SharpValue.valueOf(maxValue);
		}
	}
	/**
	 * @return the minValue
	 */
	public String getMinValue() {
		return minValue;
	}
	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(String minValue) {
		if (minValue != null)
		{
			this.minValue = minValue;
			this.sharpMinValue = SharpValue.valueOf(minValue);
		}
		
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * This method returns true if the value falls between sharpMinValue and sharpMaxValue
	 * @param value
	 * @return boolean
	 */
	public boolean isInBetween(SharpValue value)
	{
		if(value == null || sharpMaxValue == null || sharpMinValue == null)
			return false;
		
		if (value.compareTo(sharpMinValue) >= 0 && value.compareTo(sharpMaxValue) <= 0)
			return true;
		
		return false;
	}

	public String toString()
	{
		return  label + " : " + minValue + " to " + maxValue;
	}
	
}
