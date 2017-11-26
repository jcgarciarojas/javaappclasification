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

public class IfComposite extends Composite {

	@Override
	public SharpValue calculateAntecedent(Map<String, FuzzySet> sets, 
			Map<String, MetricReport> inputs) throws ConfigurationException
	{
		return this.getConditionComposite().get(0).calculateAntecedent(sets, inputs);
	}

}
