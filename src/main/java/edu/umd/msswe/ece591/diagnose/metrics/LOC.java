package edu.umd.msswe.ece591.diagnose.metrics;
import java.util.StringTokenizer;



/**
 * LOC - Lines of Code
 * This class calculates the number of lines of codes including blanks and comments

 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class LOC extends Metric {
	
	private double sumMetric;

	public LOC(){

	}

	/**
	 * This method calculates the LOC for the current class 
	 * @param structure
	 * @param class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList, JavaClassInfo classFile){
		
		MetricReport output = new MetricReport();
		if (classFile != null){
			StringBuffer content = classFile.getSourceContent();
			if (content != null)
			{
				StringTokenizer tk = new StringTokenizer(content.toString(), "\n");
				double loc = tk.countTokens();
				output.setValue(loc);
				//output.addDetails("The class contains "+loc+ " lines of code.");
				sumMetric += loc;
			}
		}
		super.incrementNumClasses();
		return output;
	}

	/**
	 * This method calculates the average of LOC for the application
	 *  
	 * @param structure
	 * @param class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
	{
		MetricReport output = new MetricReport();
		output.setValue(sumMetric/super.getNumClasses());
		output.addDetails("Average of "+Math.round(sumMetric/super.getNumClasses())+ 
				" lines of code per class.");
		
		return output;
	}

} 