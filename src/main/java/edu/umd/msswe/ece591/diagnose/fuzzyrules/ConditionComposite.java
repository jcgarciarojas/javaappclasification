package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.Map;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class ConditionComposite extends Composite {
	private String metricId;
	private String fuzzySetId;
	private String operation;
	
	
	/**
	 * 
	 * @param metricId
	 * @param operation
	 * @param fuzzySetId
	 */
	public ConditionComposite(String metricId, String operation, String fuzzySetId)
	{
		super();
		this.metricId = metricId;
		this.operation = operation;
		this.fuzzySetId = fuzzySetId;
	}

	/**
	 * 
	 */
	@Override
	public SharpValue calculateAntecedent(Map<String, FuzzySet> sets,
			Map<String, MetricReport> inputs) throws ConfigurationException {
		FuzzySet fuzzySet = sets.get(fuzzySetId);
		
		if (fuzzySet == null)
			throw new ConfigurationException("Fuzzy Set is not defined in configuration file "+ fuzzySetId);
		
		MetricReport metricReport = inputs.get(metricId);

		if (metricReport == null)
			return zero;
		
		SharpValue input = new SharpValue(metricReport.getValue());
		SharpValue degree = fuzzySet.getMatchingDegree(input);
		
		return degree;
	}

	/**
	 * 
	 */
	public String toString()
	{
		return metricId+" "+operation+" "+fuzzySetId;
	}

	/**
	 * @return the fuzzySetId
	 */
	public String getFuzzySetId() {
		return fuzzySetId;
	}

	/**
	 * @return the metricId
	 */
	public String getMetricId() {
		return metricId;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
		
	public boolean isLeaf()
	{
		return false;
	}
}
