package edu.umd.msswe.ece591.diagnose.metrics;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * AHF Attribute Hiding Factor
 * Method and attribute hiding factor measure how variables and methods are encapsulated in a class. 
 * Visibility is counted in respect to other classes. MHF and AHF represent the average amoung of 
 * hiding among all classes in the system. A private method/attribute is fully hidden. 
 * 
 * AHF = 1 - AttributesVisible 
 * AttributesVisible = sum(AV) / (C-1) / Number of attributes 
 * AV = number of other classes where attribute is visible 
 * C = number of classes 
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0 *
 */

public class AHF extends Metric {

	private double totalVisibility = 0; 
	private double numDeclareAttributes = 0; 

	/**
	 * Calculate the AHF for the current class
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;
		
		double value = 0;
		if (classFile.getAttributes().size() > 0)
		{
			for (JavaAttributeInfo attribute:classFile.getAttributes())
			{
				double visibility = sumVisibleAttributes(javaClassesList, classFile, attribute); 
				value += (1- visibility);

				if (visibility > 0)
					report.addDetails("Attribute "+attribute +" has a visibility average of "+visibility+" among all the classes.");
				else
					report.addDetails("Attribute "+attribute +" has no visibility among all the classes.");
			}
	
			report.setValue(value/classFile.getAttributes().size());
			//report.addDetails("There are "+value+" visible attributes defined in this class.");
			//report.addDetails("Attributes defined : "+classFile.getAttributes().toString());
		}
		else {
			report.setValue(0d);
			report.addDetails("This class does not define any attribute.");
		}
		totalVisibility += value; 
		numDeclareAttributes += classFile.getAttributes().size();
		super.incrementNumClasses();
		
		return report;
	}
	
	/**
	 * 
	 * @param javaClassesList
	 * @param selfClass
	 * @param attribute
	 * @return
	 */
	protected double sumVisibleAttributes(JavaApplication javaClassesList, JavaClassInfo selfClass, JavaAttributeInfo attribute)
	{
		double numClasses = 0;
		double visible = 0;
		
		for (JavaClassInfo otherClass:javaClassesList.getFiles())
		{
			if (otherClass.isInterface() || otherClass.getClassName().equals(selfClass.getClassName())) continue;
			
			if (!attribute.isPrivate())
			{
				//final variable cannot be changed so no problem sharing it
				if (attribute.isPublic() && !attribute.isFinal())
					visible++;
				//this should not happen, inheritance should happen at method and attribute level
				//else if (attribute.isProtected() && otherClass.getParent() != null && otherClass.getParent().equals(selfClass.getSimpleName()))
				//	visible++;
				else if(attribute.isDefault() && selfClass.getPackageName().equals(otherClass.getPackageName()))
					visible++;
			}
			numClasses++;
		}
		return visible/numClasses;
	}

	/**
	 * Calculate the AHF for the SW application
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {

		MetricReport report = new MetricReport();
		
		if (numDeclareAttributes <= 0)
			report.setValue(0d);
		else
			report.setValue(totalVisibility/numDeclareAttributes*100);
		
		report.addDetails("Average of "+Math.round(totalVisibility/super.getNumClasses())+" visible attributes per class.");

		return report;
	}

}
