package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRulesEngine;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.MetricsEngine;
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

public class FuzzyEngineImpl implements Engine {

    private MetricsEngine metricsEngine;
    private JavaApplication javaApplication;
    private FuzzyRulesEngine rulesEngine;
    private Map<String, FuzzySet> fuzzySets;
    private MetricsReport metricReport = new MetricsReport();
    

    /**
     * 
     * @param javaClassesList
     * @param metricsEngine
     * @param rulesEngine
     */
    public FuzzyEngineImpl(MetricsEngine metricsEngine, FuzzyRulesEngine rulesEngine, 
    		JavaApplication javaApplication, Map<String, FuzzySet> fuzzySets)
    {
    	this.metricsEngine = metricsEngine;
    	this.javaApplication = javaApplication;
    	this.rulesEngine = rulesEngine;
    	this.fuzzySets = fuzzySets;
    }
    
    /**
     *  
     * This method gathers all the metrics for each of the classes and executes the fuzzy rules classFuzzyRules for the classes
     * and the fuzzy rules appFuzzyRules for the application. 
     * 
     * @param appFuzzyRules
     * @param classFuzzyRules
     * @return List<FuzzyEngineReport>
     */
    public List<FuzzyEngineReport> execute(List<FuzzyRule> appFuzzyRules, List<FuzzyRule> classFuzzyRules) 
    	throws FuzzySystemException, ConfigurationException
    {
        List<FuzzyEngineReport> fuzzyReportList = new ArrayList<FuzzyEngineReport>(); 
        
    	try{
			//execution of metrics and rules engine 
			for (JavaClassInfo classInfo : javaApplication.getFiles()) 
			{
				//calculate the metrics for the current class
				Map<String, MetricReport> metricsOutput = metricsEngine
						.process(javaApplication, classInfo);
				this.setMetricReportClassification(metricsOutput);
				
				//execute rules engine for the current class 
				FuzzyValue fuzzySystemOutput = rulesEngine.execute(
						classFuzzyRules, metricsOutput);

				//Do not report zero fuzzy set for each class
				if (!fuzzySystemOutput.equalTo(FuzzyValue.getZeroFuzzyValue()))
					this.addFuzzyEngineReport(fuzzyReportList, classInfo.getClassName(), fuzzySystemOutput, metricsOutput, classFuzzyRules);

				metricReport.addMetricReport(classInfo, fuzzySystemOutput, metricsOutput);
				
			}
			
			//execution of metrics and rules engine
			Map<String, MetricReport> metricsOutput = metricsEngine
					.process(javaApplication);
			this.setMetricReportClassification(metricsOutput);
			
			FuzzyValue fuzzySystemOutput = rulesEngine.execute(appFuzzyRules, metricsOutput);
			this.addFuzzyEngineReport(fuzzyReportList, null, fuzzySystemOutput, metricsOutput, appFuzzyRules);
			
			metricReport.addMetricReport(null, fuzzySystemOutput, metricsOutput);

    	} catch (MetricsException e) 
    	{
			throw new FuzzySystemException(e);
    	}
		
    	return fuzzyReportList;
    }

    /**
     * This method add the fuzzy output report to the list
     * @param fuzzyReportList
     * @param fuzzyEngineReport
     * @param className
     * @param fuzzyOutput
     */
    protected void addFuzzyEngineReport(List<FuzzyEngineReport> fuzzyReportList, 
    		String className, FuzzyValue fuzzyOutput, Map<String, MetricReport> metricsOutput, List<FuzzyRule> fuzzyRules)
    throws ConfigurationException
    {
    	if (fuzzyOutput != null)
    	{
			FuzzyEngineReport fuzzyEngineReport = new FuzzyEngineReport(); 
	    	fuzzyEngineReport.setClassname(className);
	    	fuzzyEngineReport.setFuzzySystemOutput(fuzzyOutput);
	    	
	    	this.cleanupMetricReport(fuzzyRules, metricsOutput);
	    	fuzzyEngineReport.setMetricReport(metricsOutput);
	    	fuzzyReportList.add(fuzzyEngineReport);
    	}
    }
    
    /**
     * 
     * Calculation for each of the metrics is still needed so we use this method to remove the metrics 
     * that are not utilized within the set of rules.    
     * 
     * @param fuzzyRules
     * @param metricsOutput
     * @throws ConfigurationException
     */
    protected void cleanupMetricReport(List<FuzzyRule> fuzzyRules, Map<String, MetricReport> metricsOutput) 
    throws ConfigurationException
    {
    	Iterator it = metricsOutput.keySet().iterator();
    	
    	while(it.hasNext())
    	{
    		String metricId = (String)it.next();
    		boolean isMetricInRule = false;
    		
    		for (FuzzyRule rule: fuzzyRules)
    		{
    			if(rule.isMetricInRule(metricId))
    				isMetricInRule = true;
    		}
    		
    		if (!isMetricInRule)
    			metricsOutput.remove(metricId);
    		
    	}
    	
    }
    
    /**
     * 
     * @param metricsOutput
     */
    protected void setMetricReportClassification(Map<String, MetricReport> metricsOutput)
    {
    	Iterator it = metricsOutput.keySet().iterator();
    	while(it.hasNext())
    	{
    		String key = (String)it.next();
    		setMetricReportClassification(key, (MetricReport)metricsOutput.get(key));
    	}

    }

	/**
	 * 
	 * @param report
	 * @param metricId
	 * @return
	 */
	protected void setMetricReportClassification(String metricId, MetricReport report)
	{
		String label = null;
		
		if (fuzzySets != null)
		{
			SharpValue reportValue = new SharpValue(report.getValue());
			SharpValue finalValue = SharpValue.valueOf("0");
			Iterator it  = fuzzySets.values().iterator();
			try {
				while(it.hasNext())
				{
					FuzzySet set = (FuzzySet)it.next();
					if (set.getId().toLowerCase().endsWith(metricId.toLowerCase()))
					{
						SharpValue value = set.getMatchingDegree(reportValue);
						if (value.compareTo(finalValue) >= 0)
						{
							label = set.getLabel();
							finalValue = value;
						}
					}
				}
			} catch (ConfigurationException cex)
			{
				label = null;
			}
		}
		
		report.setLabel(label);
	}

	/**
	 * @return the metricReport
	 */
	public MetricsReport getMetricReport() {
		return metricReport;
	}
	
}
