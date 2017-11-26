package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * 
 * DIT - Depth of Inheritance Tree
 * The depth of inheritance tree (DIT) metric provides for each class a measure 
 * of the inheritance levels from the object hierarchy top. 
 *
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */
public class DIT extends Metric {

	private double total;
	
	/**
	 * This method calculates the DIT for the current class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException 
	{
		
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;
		
		List<JavaClassInfo> parents = super.getParents(javaClassesList, classFile);
		double numberAncestors = parents.size();
		
		if(numberAncestors > 0)
			report.addDetails("Parent classes ");
		else
			report.addDetails("Class does not have parent classes.");
		
		for(JavaClassInfo parent :parents)
			report.addDetails(parent.getSimpleName());
		//report.addDetails("The class has "+numberAncestors+" ancestor(s) from the object hierarchy top");

		report.setValue(numberAncestors);
		
		if (numberAncestors > total)
			total = numberAncestors;
		super.incrementNumClasses();
		return report;
	}
	
	/**
	 * This method calculates the DIT for the application
	 */
	 @Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {

		MetricReport report = new MetricReport();
		report.setValue(total);
		report.addDetails("The highest number of ancestors in the applciation is "+report.getValue());
		return report;
	}

}
