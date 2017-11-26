package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class MetricsEngineImpl implements MetricsEngine {

	private List<OOMetric> allMetrics = new ArrayList<OOMetric>();
	private List<OOMetric> appMetrics;
	private List<OOMetric> classMetrics;
	
	
	public MetricsEngineImpl(List<OOMetric> appMetrics, List<OOMetric> classMetrics){
		this.appMetrics = appMetrics;
		this.classMetrics = classMetrics;
		allMetrics.addAll(appMetrics);	
		allMetrics.addAll(classMetrics);	
	}
	
	/**
	 * 
	 * @param className
	 */
	public Map<String, MetricReport> process(JavaApplication javaClassesList, JavaClassInfo classInfo)
	 throws MetricsException
	{
		Map<String, MetricReport> d = new Hashtable<String, MetricReport>();  
		for (OOMetric metric: allMetrics)
		{
			MetricReport report = metric.getMetricClass().execute(javaClassesList, classInfo);
			if(report != null && classMetrics.contains(metric))
				d.put(metric.getId(), report);
		}
		return d;
	}

	/**
	 * 
	 * @param className
	 */
	public Map<String, MetricReport>  process(JavaApplication javaClassesList)
	 throws MetricsException
	{
		Map<String, MetricReport> d = new Hashtable<String, MetricReport>();  
		for (OOMetric metric: allMetrics)
		{
			MetricReport report = metric.getMetricClass().execute(javaClassesList);
			if (report != null && appMetrics.contains(metric))
				d.put(metric.getId(), report);
		}

		return d;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<OOMetric> getClassMetrics()
	{
		return classMetrics;
	}

	public List<OOMetric> getAppMetrics()
	{
		return appMetrics;
	}

}