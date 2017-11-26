package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public abstract class FuzzySet extends Line{
	
	private String id;
	private String label;
	private SharpValue max;
	private SharpValue min;
	private List<FuzzyValue> values; 
	
	public FuzzySet() throws ConfigurationException
	{
		min = SharpValue.valueOf("0");
		this.initValues();
	}

	public FuzzySet(SharpValue max) throws ConfigurationException
	{
		this.max = max;
		min = SharpValue.valueOf("0");
		this.initValues();
	}
	
	protected void initValues() throws ConfigurationException
	{
		values = new ArrayList<FuzzyValue>();
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 */
	public String toString()
	{
		return " [Fuzzy Set = "+ id + ", label = "+
			label+", values = ("+
			values + ") min = "+min+", max = "+max+"]";
	}


	/**
	 * @return the max
	 */
	public SharpValue getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(SharpValue max)  {
		this.max = max;
	}
	
	/**
	 * @param value the value to set
	 */
	public int getValueLength(){
		
		return values.size();
	}

	/**
	 * @return the doubleValues
	 */
	public List<FuzzyValue> getValues() {
		return values;
	}
	
	/**
	 * @return the min
	 */
	public SharpValue getMin() {
		return min;
	}
	
	/**
	 * return the highest degree within the fuzzy set
	 * @param fuzzySet
	 * @return
	 */
	public SharpValue getHighestDegree()
	{
		SharpValue maxDegree = null;
		for(FuzzyValue fuzzyValue :this.getValues())
		{
			SharpValue degree = fuzzyValue.getDegree();
			if(maxDegree == null)
				maxDegree = degree;
			
			else if(maxDegree.compareTo(degree) < 0)
					maxDegree = degree;
		}
		
		return maxDegree;
	}

	public abstract FuzzySet cloneObject() throws ConfigurationException;


	public abstract void setStringValues(String strValues) throws ConfigurationException; 
	public abstract void setArrayValues(List<FuzzyValue> values) throws ConfigurationException;
	public abstract SharpValue getMatchingDegree(SharpValue input) throws ConfigurationException;
	public abstract List<FuzzyValue> getMatchingInput(SharpValue inputDegree) throws ConfigurationException;
	public abstract List<FuzzyValue> getSquareValues();
	public abstract List<FuzzyValue> getPeakValues();
	
	/**
	 * return a list of values from a comma separated string
	 * @param strValues
	 * @return
	 * @throws ConfigurationException
	 */
	protected List<SharpValue> getSharpValues(String strValues) throws ConfigurationException
	{
		StringTokenizer st = new StringTokenizer(strValues, ",");
		if (st.countTokens() < 1)
			throw new ConfigurationException("values should be comma separated");
		
		List<SharpValue> l = new ArrayList<SharpValue>(); 
		while(st.hasMoreTokens())
			l.add(SharpValue.valueOf(st.nextToken()));
		
		return l;
	}
	
	/**
	 * set the fuzzy values from a comma/slash separated string
	 * @param strValues they should be in the form value/degree,value/degree
	 * @return
	 * @throws ConfigurationException
	 */
	public void setFuzzyValues(String strFuzzyValues) throws ConfigurationException
	{
		List<FuzzyValue> fuzzyValues = getFuzzyValues(strFuzzyValues);
		this.setArrayValues(fuzzyValues);
	}
	/**
	 * return a list of fuzzy values from a comma/slash separated string
	 * @param strValues they should be in the form value/degree,value/degree
	 * @return
	 * @throws ConfigurationException
	 */
	protected List<FuzzyValue> getFuzzyValues(String strValues) throws ConfigurationException
	{
		StringTokenizer st = new StringTokenizer(strValues, ",");
		if (st.countTokens() < 1)
			throw new ConfigurationException("values should be comma separated");
		
		List<FuzzyValue> l = new ArrayList<FuzzyValue>(); 
		while(st.hasMoreTokens())
		{
			StringTokenizer st1 = new StringTokenizer(st.nextToken(), "/");
			if (st1.countTokens() != 2) throw new ConfigurationException("values should be of the form value/degree");
			l.add(new FuzzyValue(st1.nextToken(),st1.nextToken())); 
		}
		
		return l;
	}

	/**
	 * this method
	 *
	 */
	protected SharpValue validateValue(SharpValue value) throws ConfigurationException
	{
		if (value.compareTo(max)> 0)
			throw new ConfigurationException("fuzzy set "+id +", value "+value +"should be less than the maximum value in this fuzzy set "+max);
		
		if (value.compareTo(min)< 0)
			throw new ConfigurationException("fuzzy set "+id +", value "+value +"should be less greater than or equal to the minimum value in this fuzzy set "+min);

		for(FuzzyValue fuzzyValue: values)
		{
			if (fuzzyValue.getSharpValue().compareTo(value) > 0)
				throw new ConfigurationException("fuzzy set "+id +", value "+value +"should not be greater than a value already assigned");
		}

		return value;
 	}

	/**
	 * @param min the min to set
	 */
	public void setMin(SharpValue min) {
		this.min = min;
	}

	/**
	 * @param values the values to set
	 */
	protected void setValues(List<FuzzyValue> values) {
		this.values = values;
	}

}
