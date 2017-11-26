package edu.umd.msswe.ece591.diagnose.metrics;

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

public interface MetricsEngine {
	
	/**
	 * 
	 * @param className
	 */
	public Map<String, MetricReport> process(JavaApplication javaApplication, JavaClassInfo classInfo)
	 throws MetricsException;

	/**
	 * 
	 * @param className
	 */
	public Map<String, MetricReport> process(JavaApplication javaApplication)
	 throws MetricsException;
	
}