package edu.umd.msswe.ece591.diagnose.metrics;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * MHF Method Hiding Factor
 * Method and attribute hiding factor measure how variables and methods are encapsulated in a class. 
 * Visibility is counted in respect to other classes. MHF and AHF represent the average amoung of 
 * hiding among all classes in the system. A private method/attribute is fully hidden. 
 * 
 * MHF = 1 - MethodsVisible 
 * MethodsVisible = sum(MV) / (C-1) / Number of methods 
 * MV = number of other classes where method is visible
 * C = number of classes 
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class MHF extends Metric {
	
	private double totalVisible = 0; 
	private double numDeclareMethods = 0; 

	/**
	 * Calculate the MHF for the current class
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;
		
		double visible =0;
		for (JavaMethodInfo method:classFile.getMethods())
		{
			double visibility = sumVisibleMethods(javaClassesList, classFile, method);
			
			if (visibility > 0)
				report.addDetails("Method "+method +" has a visibility average of "+visibility+" among all the classes.");
			else
				report.addDetails("Method "+method +" has no visibility among all the classes.");

			visible += (1- visibility);
		}

		if (classFile.getMethods().size() >0)
		{
			report.setValue(visible/classFile.getMethods().size());
			//report.addDetails("Methods defined in the class : "+classFile.getMethods().toString());
			//report.addDetails("There are "+value+" visible methods defined in this class.");
		}
		else {
			report.setValue(0d);
			report.addDetails("this class does not define any method ");
		}
		totalVisible += visible; 
		numDeclareMethods += classFile.getMethods().size();
		
		super.incrementNumClasses();
		return report;
	}
	
	/**
	 * 
	 * @param javaClassesList
	 * @param selfClass
	 * @param method
	 * @return
	 */
	protected double sumVisibleMethods(JavaApplication javaClassesList, JavaClassInfo selfClass, JavaMethodInfo method)
	{
		double numClasses = 0;
		double visible = 0;
		
		for (JavaClassInfo otherClass:javaClassesList.getFiles())
		{
			if (otherClass.isInterface() || otherClass.getClassName().equals(selfClass.getClassName())) continue;
			
			if (!method.isPrivate())
			{
				if (method.isPublic())
					visible++;
				else if (method.isProtected() && otherClass.getParent() != null && 
						otherClass.getParent().equals(selfClass.getSimpleName()))
					visible++;
				else if(method.isDefault() && selfClass.getPackageName().equals(otherClass.getPackageName()))
					visible++;
			}
			numClasses++;
		}
		return visible/numClasses;
	}

	
	/**
	 * Calculate the MHF for the SW application
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {

		MetricReport report = new MetricReport();
		if (numDeclareMethods <= 0)
			report.setValue(0d);
		else
			report.setValue(totalVisible/numDeclareMethods*100);
		
		report.addDetails("Average of "+Math.round(totalVisible/super.getNumClasses())+" visible methods per class.");

		return report;
	}

}
