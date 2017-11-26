package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public abstract class DefuzzificationMethod  {

	/**
	 * 
	 * @param set
	 */
	public abstract FuzzyValue calculate(ResultFuzzySet set) throws FuzzySystemException;

}