package edu.umd.msswe.ece591.diagnose.exception;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzySystemException extends Exception {
	
	static final long serialVersionUID = -1L;
	public FuzzySystemException(Exception e)
	{
		super(e);
	}

	public FuzzySystemException(String e)
	{
		super(e);
	}
}
