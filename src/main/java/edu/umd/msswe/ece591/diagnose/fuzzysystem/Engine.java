package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;
import edu.umd.msswe.ece591.diagnose.reports.MetricsReport;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public interface Engine {

	/**
	 * 
	 * @param appFuzzyRules
	 * @param classFuzzyRules
	 * @return
	 * @throws FuzzySystemException
	 * @throws ConfigurationException
	 */
	public abstract List<FuzzyEngineReport> execute(List<FuzzyRule> appFuzzyRules, List<FuzzyRule> classFuzzyRules)
			throws FuzzySystemException, ConfigurationException;

	public abstract MetricsReport getMetricReport();
}