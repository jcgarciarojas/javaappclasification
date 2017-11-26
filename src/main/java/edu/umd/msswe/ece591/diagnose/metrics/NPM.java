package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

/**
 * NPM - Number of Public Methods
 * The NPM metric simply counts all the methods in a class that are declared as public. 
 * It can be used to measure the size of an API provided by a package.  
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class NPM extends Metric {

	private double sumMetric;

	@Override
	public MetricReport execute(JavaApplication javaClassesList, JavaClassInfo classFile) {
		MetricReport report = new MetricReport();
		List<JavaMethodInfo> methods = classFile.getMethods();
		double numMethods = 0; 
		
		if (methods != null)
		{
			for(JavaMethodInfo method: methods)
			{
				if (method.isPublic())
					numMethods++;
			}
		}
		
		report.setValue(numMethods);
		sumMetric += numMethods;
		report.addDetails("There are "+numMethods+" public methods defined in this class.");
		super.incrementNumClasses();
		return report;
	}

	/**
	 * Return the average NPM per class in the system
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList) {

		MetricReport report = new MetricReport();
		report.setValue(sumMetric/super.getNumClasses());
		report.addDetails("There is an average of "+Math.round(report.getValue())+" public methods per class defined in the application.");
		
		return report;
	}

}
