package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.Map;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class FuzzyRuleBasedEngine extends FuzzyRulesEngine {
	
	private DefuzzificationMethod defuzzification; 
	private InferenceMethod inferenceMethod;

	public FuzzyRuleBasedEngine()
	{
		super();
	}
	
	/**
	 * 
	 * @param defuzzification
	 */
	public void setDefuzzificationMethod(DefuzzificationMethod defuzzification)
	{
		this.defuzzification = defuzzification;
	}

	
	/**
	 * Calculate the degree to which the input data match the condition of the fuzzy rules 
	 * @param rule
	 * @param inputs
	 * @return
	 */
	public SharpValue fuzzyMatching(FuzzyRule rule, Map<String, MetricReport> inputs) 
	throws FuzzySystemException
	{
		SharpValue degree = SharpValue.valueOf("0");
		
		try {

			degree= rule.calculate(this.getFuzzySets(), inputs);
			
		} catch (ConfigurationException e) {
			throw new FuzzySystemException(e);
		}
		
		return degree;

	}

	/**
	 * Calculate's the rule's conclusion based on it's mathing degree 
	 * @param rule
	 * @param values
	 * @return
	 */
	public FuzzySet inference(FuzzyRule rule, SharpValue degree)
	throws FuzzySystemException
	{
		FuzzySet inferedSet = null;
		
		try {
			FuzzySet set = this.getFuzzySets().get(rule.getThenCondition());
			if (inferenceMethod != null)
				inferedSet = inferenceMethod.execute(set, degree);
			/*
			inferedSet = FuzzySetFactory.instance().createFuzzySetByValue(set.getMax(), 
					set.getMatchingInput(degree));
*/
		} catch (ConfigurationException e) {
			throw new FuzzySystemException(e);
		}		
		return inferedSet;
	}
		
	/**
	 * Conbine de conclusion inferred by all fuzzy rules into a final conclusion
	 * @param sets
	 * @return
	 */
	public ResultFuzzySet combination(List<FuzzySet> fuzzySets)
	throws FuzzySystemException
	{
		ResultFuzzySet inferredSet = new ResultFuzzySet(); 
		
		if (fuzzySets != null)
		{
			for (FuzzySet fuzzySet : fuzzySets) 
			{
				int size = inferredSet.getFuzzySets().size();
				
				if (size == 0)
					inferredSet.add(fuzzySet);
				
				else
					combination(inferredSet, fuzzySet); 
			}
		}			
		return inferredSet;
	}
	
	/**
	 * 
	 * This method checks if two fuzzy sets are continuos, merge them into one and add them to ResultFuzzySet 
	 * otherwise adds the fuzzy set to ResultFuzzySet
	 * @param inferredSet
	 * @param fuzzySet
	 * @throws FuzzySystemException
	 */
	private void combination(ResultFuzzySet inferredSet, FuzzySet newFuzzySet) 
	throws FuzzySystemException
	{
		try {
			
			boolean isAdded = false;
			for (FuzzySet currentFuzzyset : inferredSet.getFuzzySets()) {
				
				if (currentFuzzyset.getHighestDegree().compareTo(newFuzzySet.getHighestDegree()) != 0)
					continue;

				else if (areContinuosFuzzySets(currentFuzzyset, newFuzzySet)) {
					currentFuzzyset = FuzzySetFactory.instance()
							.mergeFuzzySets(currentFuzzyset, newFuzzySet);
					isAdded = true;
				}
			}
			if (!isAdded)
				inferredSet.add(newFuzzySet);
		
		} catch (ConfigurationException e) 
		{
			throw new FuzzySystemException(e);
		}		
	}
	
	/**
	 * This method checks if the peak points of two fuzzy sets are continous (have the same degree) 
	 * @param fuzzySet1
	 * @param fuzzySet2
	 * @return
	 */
	private boolean areContinuosFuzzySets(FuzzySet fuzzySet1, FuzzySet fuzzySet2)
	{
		boolean areContinuos = false;
		List<FuzzyValue> sqValues1 = fuzzySet1.getPeakValues();
		List<FuzzyValue> sqValues2 = fuzzySet2.getPeakValues();
		
		if (sqValues1.get(0).getDegree().compareTo(sqValues2.get(0).getDegree()) == 0
				&&
				sqValues1.get(1).getDegree().compareTo(sqValues2.get(1).getDegree()) == 0)
		{
			double a1= sqValues1.get(0).getSharpValue().getValue();
			double a2= sqValues2.get(0).getSharpValue().getValue();
			double b1= sqValues1.get(1).getSharpValue().getValue();
			double b2= sqValues2.get(1).getSharpValue().getValue();
			
			if ( a1 <= b2 && b1 >= a2)
				areContinuos = true;

			else if ( a2 <= b1 && b2 >= a1)
				areContinuos = true;
		}
		
		return areContinuos;
	}

	/**
	 * Convert the fuzzy conclusion into a crisp output 
	 * @param set
	 * @return
	 */
	public FuzzyValue defuzzification(ResultFuzzySet inferredFuzzySet)
	throws FuzzySystemException
	{
		return defuzzification.calculate(inferredFuzzySet);
	}


	
	/**
	 * hookup methods
	 */
	public void preProcess() throws FuzzySystemException
	{
		return;
	}
	
	/**
	 * This method chooses the lowest rules report in case of duplicate reports for the same metric
	 */
	protected void postProcess() throws FuzzySystemException
	{
		return;
	}

	/**
	 * @param inferenceMethod the inferenceMethod to set
	 */
	public void setInferenceMethod(InferenceMethod inferenceMethod) {
		this.inferenceMethod = inferenceMethod;
	}

}
