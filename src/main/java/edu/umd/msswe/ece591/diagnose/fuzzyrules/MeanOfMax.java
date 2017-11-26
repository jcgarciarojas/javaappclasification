package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class MeanOfMax extends DefuzzificationMethod {

	/**
	 * this method returns the Mean of Maximum of the Inferred Fuzzy Sets
	 * @param set
	 */
	public FuzzyValue calculate(ResultFuzzySet set) throws FuzzySystemException {
		
		FuzzySet finalFuzzySet =  null; 
		
		try {
			finalFuzzySet = set.getFuzzySets().get(0).cloneObject();
			for (FuzzySet fuzzySet : set.getFuzzySets()) {
				
				SharpValue valueSet =fuzzySet.getHighestDegree();
				SharpValue finalValueSet = finalFuzzySet.getHighestDegree();  
				if (valueSet.compareTo(finalValueSet) > 0)
					finalFuzzySet = fuzzySet.cloneObject();
			}
			
		} catch (ConfigurationException e) {
			throw new FuzzySystemException(e);
		}		
		return this.getMaxOfMean(finalFuzzySet);	
	}
	
	/**
	 * 
	 * @param fuzzySet
	 * @return
	 */
	public FuzzyValue getMaxOfMean(FuzzySet fuzzySet)
	{
		List<FuzzyValue> values = fuzzySet.getPeakValues();
		
		double i=0;
		double sum = 0d; 
		for(FuzzyValue value: values)
		{
			sum += value.getSharpValue().getValue();
			i++;
		}
		
		return new FuzzyValue(""+(sum/i), ""+fuzzySet.getHighestDegree());
	}
	

}