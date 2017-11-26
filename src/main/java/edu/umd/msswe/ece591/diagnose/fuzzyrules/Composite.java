package edu.umd.msswe.ece591.diagnose.fuzzyrules;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

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

public abstract class Composite {

	private ArrayList<Composite> list;
	
	protected static final SharpValue zero = SharpValue.valueOf("0"); 

	/**
	 * 
	 *
	 */
	public Composite()
	{
		list = new ArrayList<Composite>();
	}
	
	/**
	 * 
	 * @param composite
	 */
	public void addCompositeCondition(Composite composite)
	{
		list.add(composite);
	}
	
	/**
	 * 
	 * @param composite
	 */
	public void removeConditionComposite(Composite composite)
	{
		list.remove(composite);
	}
	
	public List<Composite> getConditionComposite()
	{
		return list;
	}
		
	/**
	 * 
	 * @param sets
	 * @param inputs
	 * @return
	 * @throws ConfigurationException
	 */
	public abstract SharpValue calculateAntecedent(Map<String, FuzzySet> sets, 
			Map<String, MetricReport> inputs) throws ConfigurationException;

	public String toString()
	{
		if (list != null)
		{
			StringBuffer sb = new StringBuffer();
			for (Composite c :list)
			{
				sb.append (c.toString()+" ");
			}
			return sb.toString();
			
		}
		else
			return "No Values";
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Composite> getCompositeList()
	{
		ArrayList<Composite> list = new ArrayList<Composite>();
		
		if (this instanceof ConditionComposite)
			list.add(this);
		else {
			for (Composite c :this.list)
			{
				if(c.isLeaf())
					list.add(c);
				else
					list.addAll(c.getCompositeList());
			}
		}
		
		return list;
	}
	
	public boolean isLeaf()
	{
		return false;
	}
	
}
