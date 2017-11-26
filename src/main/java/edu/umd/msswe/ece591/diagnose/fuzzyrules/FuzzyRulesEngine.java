package edu.umd.msswe.ece591.diagnose.fuzzyrules;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

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

public abstract class FuzzyRulesEngine {
	
	private Map<String, FuzzySet> fuzzySets;

	/**
	 * Parent class receives the fuzzy sets and a builder object
	 * The builder object is not really used by this class but it is
	 * enforced in here so there is a factory class that can be used by the children class
	 * to initialize objects within it
	 * It is worth to note that there should be enough capacity for the system
	 * to plug any other engine and this class might help build some parts of the subsystem
	 * 
	 * @param fuzzySets
	 * @param builder
	 */
	public FuzzyRulesEngine()
	{
		return;
	}

	/**
	 * 
	 * @param fuzzySets
	 */
	public void setFuzzySets(Map<String, FuzzySet> fuzzySets)
	{
		this.fuzzySets = fuzzySets;
	}

	
	/**
	 *  Execute the fuzzy rules engine for the specified inputs, and calculate the fuzzy inout 
	 * @param rules the fuzzy rules to be executed with the inputs
	 * @param input the metrics report
	 * @return a FuzzyValue or null  
	 */
	public final FuzzyValue execute(List<FuzzyRule> fuzzyRules, 
			Map<String, MetricReport> inputs) throws FuzzySystemException {
		
		FuzzyValue defuzzificationValue = FuzzyValue.getZeroFuzzyValue();
		List<FuzzySet> inferedSets = new ArrayList<FuzzySet>();
		
		this.preProcess();
		
		for (FuzzyRule rule: fuzzyRules)
		{
			SharpValue matchingDegree = fuzzyMatching(rule, inputs);
			//if matching degree is greater than 0 continue  
			if (matchingDegree.compareTo(SharpValue.valueOf("0.5")) >= 0)
			{
				FuzzySet resultedSet = inference(rule, matchingDegree);
				if (resultedSet != null)
					inferedSets.add(resultedSet);
			}
		}

		//if there are inferenced values calculated then continue with the algorithm  
		if (inferedSets.size() > 0)
		{
			ResultFuzzySet fuzzySetCombination = combination(inferedSets);
			defuzzificationValue = defuzzification(fuzzySetCombination);
			this.postProcess();
		} else
			inferedSets = null;
		
		return defuzzificationValue;
	}
	
	/**
	 * hook up methods
	 * @throws FuzzySystemException
	 */
	protected abstract void preProcess() throws FuzzySystemException;
	protected abstract void postProcess() throws FuzzySystemException;

	/**
	 * Calculate the degree to which the input data match the condition of the fuzzy rules 
	 * @param rule
	 * @param inputs
	 * @return
	 */
	public abstract SharpValue fuzzyMatching(FuzzyRule rule,
			Map<String, MetricReport> inputs) throws FuzzySystemException;
	
	/**
	 * Calculate's the rule's conclusion based on it's mathing degree 
	 * @param rule
	 * @param values
	 * @return
	 */
	public abstract FuzzySet inference(FuzzyRule rule, SharpValue value) throws FuzzySystemException;
	
	/**
	 * Combine de conclusion inferred by all fuzzy rules into a final conclusion
	 * @param sets
	 * @return
	 */
	public abstract ResultFuzzySet combination(List<FuzzySet> fuzzySets) throws FuzzySystemException;
	
	/**
	 * Convert the fuzzy conclusion into a crisp output 
	 * @param set
	 * @return
	 */
	public abstract FuzzyValue defuzzification(ResultFuzzySet inferredFuzzySet) throws FuzzySystemException;

	/**
	 * 
	 * @param defuzzification
	 */
	public abstract void setDefuzzificationMethod(DefuzzificationMethod defuzzification);
	public abstract void setInferenceMethod(InferenceMethod inferenceMethod);
	/**
	 * @return the fuzzySets
	 */
	protected Map<String, FuzzySet> getFuzzySets() {
		return fuzzySets;
	}
	
}
