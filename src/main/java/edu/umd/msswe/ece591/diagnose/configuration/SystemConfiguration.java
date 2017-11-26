package edu.umd.msswe.ece591.diagnose.configuration;
import java.util.*;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.DefuzzificationMethod;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRulesEngine;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.InferenceMethod;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.Engine;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.FuzzyEngineImpl;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.FuzzyOutput;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MetricsEngine;
import edu.umd.msswe.ece591.diagnose.metrics.MetricsEngineImpl;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;
import edu.umd.msswe.ece591.diagnose.reports.DecompositionTreeAlgorithm;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class SystemConfiguration {

	private List<FuzzyRule> fuzzyRulesClass;
	private List<FuzzyRule> fuzzyRules;
	private Hashtable<String, OOMetric> ooMetrics;
	private List<OOMetric> listOoMetricsClasses;
	private List<OOMetric> listAppMetrics;
	private List<OOMetric> listOoMetrics;
	private Hashtable<String, FuzzySet> fuzzySets;
	private List<FuzzyOutput> fuzzyOutput;
	private FuzzyRulesEngine fuzzyEngine;

	/**
	 * @param fuzzyClassRules the fuzzyClassRules to set
	 */
	public void setFuzzyRules(List<FuzzyRule> fuzzyRules) {
		this.fuzzyRules = fuzzyRules;
	}

	/**
	 * @param fuzzySets the fuzzySets to set
	 */
	public void setFuzzySets(Hashtable<String, FuzzySet> fuzzySets) {
		this.fuzzySets = fuzzySets;
		this.fuzzyEngine.setFuzzySets(this.fuzzySets);
	}

	/**
	 * @param ooMetrics the ooMetrics to set
	 */
	public void setOoMetrics(Hashtable<String, OOMetric> ooMetrics) {
		this.ooMetrics = ooMetrics;
	}

	/**
	 * @return the fuzzyClassRules
	 */
	public List<FuzzyRule> getFuzzyRules() {
		return fuzzyRules;
	}

	/**
	 * @return the fuzzySets
	 */
	public Object getFuzzySet(String key) {
		return fuzzySets.get(key);
	}

	public Hashtable<String, FuzzySet> getFuzzySets()
	{
		return fuzzySets;
	}
	
	/**
	 * @return the ooMetrics
	 */
	public Object getOoMetric(String key) {
		return ooMetrics.get(key);
	}
	
	/**
	 * This method gets only the OO Metrics used in the Fuzzy Rules used for the classification of the Classes.
	 * @return the ooMetrics
	 */
	public List<OOMetric> getOoMetricsForClasses() throws ConfigurationException
	{
		if (listOoMetricsClasses == null)
		{
			listOoMetricsClasses= new ArrayList<OOMetric>();
			Enumeration e = ooMetrics.keys();
			while(e.hasMoreElements())
			{
				String metricId = (String)e.nextElement();
				for (FuzzyRule rule: fuzzyRulesClass)
				{
					if(rule.isMetricInRule(metricId))
					{
						OOMetric m =ooMetrics.get(metricId);
						if (m != null && !listOoMetricsClasses.contains(m))
							listOoMetricsClasses.add(m);
					}
				}
			}
		}
		
		return listOoMetricsClasses;
	}

	public List<OOMetric> getOoMetricsForApp() throws ConfigurationException
	{
		if (listAppMetrics == null)
		{
			listAppMetrics= new ArrayList<OOMetric>();
			Enumeration e = ooMetrics.keys();
			while(e.hasMoreElements())
			{
				String metricId = (String)e.nextElement();
				for (FuzzyRule rule: fuzzyRules)
				{
					if(rule.isMetricInRule(metricId))
					{
						OOMetric m =ooMetrics.get(metricId);
						if (m != null && !listAppMetrics.contains(m))
							listAppMetrics.add(m);
					}
				}
			}
		}
		
		return listAppMetrics;
	}
	
	/**
	 * This method gets only the OO Metrics used in the Fuzzy Rules used for the classification of the system.
	 * @return the ooMetrics
	 */
	public List<OOMetric> getAllOoMetrics() throws ConfigurationException
	{
		if (listOoMetrics == null)
		{
			listOoMetrics= new ArrayList<OOMetric>();
			Enumeration e = ooMetrics.keys();
			while(e.hasMoreElements())
			{
				String metricId = (String)e.nextElement();
				addToList(fuzzyRules, metricId, listOoMetrics); 
				addToList(fuzzyRules, metricId, listOoMetricsClasses); 
			}
		}
		
		return listOoMetrics;
	}
	
	private void addToList(List<FuzzyRule> fuzzyRules, String metricId, List<OOMetric> list) 
	throws ConfigurationException
	{
		for (FuzzyRule rule: fuzzyRules)
		{
			if(rule.isMetricInRule(metricId))
			{
				OOMetric m =ooMetrics.get(metricId);
				if (m != null && !list.contains(m))
					list.add(m);
			}
		}
	}

	/**
	 * @return the ooMetrics
	 */
	public List<OOMetric> getFullSetOoMetrics() 
	{
		return new ArrayList<OOMetric>(ooMetrics.values());
	}
	
	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public void setFuzzyRulesEngine(FuzzyRulesEngine fuzzyEngine)
	{
		this.fuzzyEngine = fuzzyEngine;
	}
	
	/**
	 * 
	 * @param defMethod
	 */
	public void setDefMethod(DefuzzificationMethod defMethod)
	{
		if (defMethod != null)
			this.fuzzyEngine.setDefuzzificationMethod(defMethod);
	}
	
	public void setInferenceMethod(InferenceMethod infMethod)
	{
		if (infMethod != null)
			this.fuzzyEngine.setInferenceMethod(infMethod);
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public FuzzyRulesEngine getFuzzyRulesEngine() throws ConfigurationException
	{
		return fuzzyEngine;
	}

	/**
	 * 
	 * @return
	 */
	public MetricsEngine getMetricsEngine() throws ConfigurationException
	{
		return new MetricsEngineImpl(this.getOoMetricsForApp(), this.getOoMetricsForClasses());
	}
	
	/**
	 * 
	 * @param javaApplication
	 * @return
	 * @throws ConfigurationException
	 */
	public Engine getEngine(JavaApplication javaApplication, String metricOutputLocation) throws ConfigurationException
	{   
		MetricsEngine mEngine = this.getMetricsEngine();
		return new FuzzyEngineImpl(mEngine, this.getFuzzyRulesEngine(), javaApplication, fuzzySets); 
	}
	
	public DecompositionTreeAlgorithm getDecompositionTreeAlgorithm() throws ConfigurationException
	{
		return new DecompositionTreeAlgorithm(this.getOoMetricsForClasses());

	}

	/**
	 * @return the fuzzyOutput
	 */
	public List<FuzzyOutput> getFuzzyOutput() {
		return fuzzyOutput;
	}

	/**
	 * @param fuzzyOutput the fuzzyOutput to set
	 */
	public void setFuzzyOutput(List<FuzzyOutput> fuzzyOutput) {
		this.fuzzyOutput = fuzzyOutput;
	}

	/**
	 * @return the fuzzyRulesClass
	 */
	public List<FuzzyRule> getFuzzyRulesClass() {
		if (fuzzyRulesClass == null || fuzzyRulesClass.size() <=0)
			return fuzzyRules;
		else
			return fuzzyRulesClass;
	}

	/**
	 * @param fuzzyRulesClass the fuzzyRulesClass to set
	 */
	public void setFuzzyRulesClass(List<FuzzyRule> fuzzyRulesClass) {
		
		this.fuzzyRulesClass = fuzzyRulesClass;
	}
	

}