package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

/**
 * 
 * NOC - Number of Children
 * A class's number of children (NOC) metric simply measures the number of immediate descendants of the class.  
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class NOC extends Metric {
	
	private double totalNumberOfChildren; 

	/**
	 * This method calculates the NOC for the current class
	 * 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList, JavaClassInfo classFile)
	{
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;
		
		
		List<String> children = super.getChildren(javaClassesList, classFile);
		double numChildren = children.size();
		
		if (numChildren > 0)
		{
			report.addDetails("Classes descendents ");

			for(String child: children)
				report.addDetails(child);
		} else
			report.addDetails("This class does not have any descendent Classes.");
		
		if (numChildren > totalNumberOfChildren)
			totalNumberOfChildren = numChildren;
		report.setValue(numChildren);
		//report.addDetails("There are "+numChildren+ " descendent(s) for the current class.");
		
		super.incrementNumClasses();
		return report;
	}


	/**
	 * This method calculates the NOC for the whole application
	 * 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
	{
		MetricReport report = new MetricReport();
		report.setValue(totalNumberOfChildren);
		report.addDetails("Highest number of immediate descendents found is "+report.getValue());
		return report;
	}

}
