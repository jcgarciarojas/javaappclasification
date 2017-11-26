package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class ResultFuzzySet {

	private List<FuzzySet> fuzzySets;
	
	/**
	 * 
	 *
	 */
	public ResultFuzzySet()
	{
		fuzzySets = new ArrayList<FuzzySet>();
	}
	
	/**
	 * 
	 * @param fuzzySet
	 */
	public void add(FuzzySet fuzzySet)
	{
		fuzzySets.add(fuzzySet);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<FuzzySet> getFuzzySets()
	{
		return fuzzySets;
	}
}
