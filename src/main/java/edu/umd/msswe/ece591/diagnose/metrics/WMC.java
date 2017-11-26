package edu.umd.msswe.ece591.diagnose.metrics;
import java.util.List;



/**
 * WMC: Weighted methods per class
 * A class's weighted methods per class WMC metric is simply the sum of the complexities of its methods. 
 * WMC is calculated by counting the number of methods in each class.
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class WMC extends Metric {

	private double sumMetric;

	/**
	 * This method returns the number of methods defined in the class
	 * 
	 * @param structure
	 * @param class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList, JavaClassInfo classFile){
		
		MetricReport report = new MetricReport(); 
		
		if (classFile.isInterface())
			return report;
		
		List<JavaMethodInfo> methods = classFile.getMethods();

		if(methods != null)
		{
			double wmc = 0;
			report.addDetails("Methods implemented in the class");
			for (JavaMethodInfo method: methods)
			{
				
				if (method.isAbstract())
					continue;
				wmc++;
				report.addDetails(method.toString());
			}
			report.setValue(wmc);
			sumMetric += wmc;
		}

		//report.addDetails("The class has "+report.getValue()+" methods implemented.");
		super.incrementNumClasses();
		
		return report;
	}

	/**
	 * This method returns the average number of methods defined in the sxystem
	 * @param structure
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList){

		MetricReport output = new MetricReport();
		output.setValue(sumMetric/super.getNumClasses());
		output.addDetails("Average of "+Math.round(output.getValue())+" methods implemented per class.");

		return output;
	}

}