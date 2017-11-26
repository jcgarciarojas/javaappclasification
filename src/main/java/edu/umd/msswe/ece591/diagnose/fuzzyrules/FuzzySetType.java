package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzySetType {

	private String className;
	private String type;
	private FuzzySet fuzzySet;
	
	public FuzzySetType(String type)
	{
		this.type = type;
	}
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) throws ConfigurationException {
		this.className = className;

		try{
			Class c = Class.forName(this.className);
			this.fuzzySet = (FuzzySet)c.newInstance();
		} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (InstantiationException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the fuzzySet
	 */
	public FuzzySet getFuzzySet() {
		return fuzzySet;
	}
	
}
