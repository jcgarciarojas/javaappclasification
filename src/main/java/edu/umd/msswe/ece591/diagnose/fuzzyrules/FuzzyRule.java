package edu.umd.msswe.ece591.diagnose.fuzzyrules;

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

public class FuzzyRule {

	private Composite ifCondition;
	private String thenCondition;
	private String id;

	public FuzzyRule(String id){
		this.id = id;
	}
	
	public SharpValue calculate(Map<String, FuzzySet> sets,
			Map<String, MetricReport> inputs) throws ConfigurationException
	{
		try{
			return ifCondition.calculateAntecedent(sets, inputs);
		} catch(ConfigurationException ce)
		{
			System.out.println(this.toString());
			throw ce;
		} catch(Exception ce)
		{
			System.out.println(this.toString());
			throw new ConfigurationException(ce);
		}
	}
	
	public String toString()
	{
		
		return " Fuzzy Rule "+id + " : " +ifCondition.toString() +" then "+thenCondition;
	}
	
	/**
	 * Check if the metricId exists in the fuzzy rule 
	 * @param metricId
	 * @return
	 * @throws ConfigurationException
	 */
	
	public boolean isMetricInRule(String metricId) throws ConfigurationException
	{
		for(Composite cond: ifCondition.getCompositeList())
		{
			ConditionComposite c = (ConditionComposite)cond;
			if (c.getMetricId() != null && c.getMetricId().equals(metricId))
				return true;
		}
		
		return false;
	}
	
	public List<Composite> getConditions()
	{
		return ifCondition.getCompositeList();
	}

	/**
	 * @return the ifCondition
	 */
	public Composite getIfCondition() {
		return ifCondition;
	}

	/**
	 * @param ifCondition the ifCondition to set
	 */
	public void setIfCondition(Composite ifCondition) {
		this.ifCondition = ifCondition;
	}

	/**
	 * @return the thenComposite
	 */
	public String getThenCondition() {
		return thenCondition;
	}

	/**
	 * @param thenComposite the thenComposite to set
	 */
	public void setThenCondition(String thenCondition) {
		this.thenCondition = thenCondition;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}