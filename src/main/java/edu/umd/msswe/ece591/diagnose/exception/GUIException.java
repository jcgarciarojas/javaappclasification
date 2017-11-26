package edu.umd.msswe.ece591.diagnose.exception;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class GUIException extends Exception {
	
	static final long serialVersionUID = -1L;
	public GUIException(Exception e)
	{
		super(e);
	}

	public GUIException(String e)
	{
		super(e);
	}
}
