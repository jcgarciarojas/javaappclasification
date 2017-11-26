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

public class AndComposite extends Composite {

	/**
	 * 
	 */
	@Override
	public SharpValue calculateAntecedent(Map<String, FuzzySet> sets,
			Map<String, MetricReport> inputs)
			throws ConfigurationException {
		
		SharpValue s1 = null;
		for (Composite composite : this.getConditionComposite())
		{
			SharpValue s2 = composite.calculateAntecedent(sets, inputs);
			if (s1 == null)
				s1 = s2;
			else
				s1 = SharpValue.getMin(s1, s2);
		}
		
		return s1;
	}

	public String toString()
	{
		if (this.getConditionComposite() != null)
		{
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<this.getConditionComposite().size();i++)
			{
				Composite c =this.getConditionComposite().get(i);
				sb.append (c.toString());
				if (i<this.getConditionComposite().size()-1)
				{
					sb.append (" AND ");
				}
			}
			return "["+sb.toString()+"]";
		}
		else
			return "No Values";
	}
}
