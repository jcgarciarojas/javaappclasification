package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class ClippingMethod extends InferenceMethod {

	/**
	 * 
	 */
	@Override
	public FuzzySet execute(FuzzySet set, SharpValue matchingDegree)
			throws ConfigurationException {
		return FuzzySetFactory.instance().createFuzzySetByValue(set.getMax(), 
				set.getMatchingInput(matchingDegree));
	}

}
