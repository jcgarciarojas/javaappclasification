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

public class NotComposite extends Composite {
	
	/**
	 * 
	 */
	@Override
	public SharpValue calculateAntecedent(Map<String, FuzzySet> sets,
			Map<String, MetricReport> inputs)
			throws ConfigurationException {
		
		if (this.getConditionComposite().size() != 1)
			throw new ConfigurationException("NOT condition should have only one element");
		
		Composite composite = this.getConditionComposite().get(0);
		SharpValue s1 = composite.calculateAntecedent(sets, inputs); 
		if ( s1.getValue() >= 0.5)
			return SharpValue.valueOf("0");
		else
			return SharpValue.valueOf("1");
		
	}

	/**
	 * 
	 */
	public String toString()
	{
		if (this.getConditionComposite() != null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(" NOT [ ");
			for (int i=0;i<this.getConditionComposite().size();i++)
			{
				Composite c =this.getConditionComposite().get(i);
				sb.append (c.toString());
			}
			return sb.append (" ] ").toString();
		}
		else
			return "No Values";
	}

}
