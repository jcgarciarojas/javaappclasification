package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * AIF Attribute Inheritance Factor
 * A class that inherits lots of attributes from its ancestor classes contributes 
 * to a high AIF. A child class that redefines its ancestors' attributes and adds new ones contributes 
 * to a lower AIF. An independent class that does not inherit and has no children contributes 
 * to a lower AIF.
 * 
 * MIF = inherited attributes/total attributes available in classes
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 * 
 */
public class AIF extends Metric {

	private double attributesInherited;
	private double attributesDeclared;

	/**
	 * Calculate the AIF for the classFile class
	 * @param javaClassesList
	 */	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {

		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;

		double numAttributesDeclared = 0;
		int numAif = 0;
		List<JavaAttributeInfo> attributeObjectList = new ArrayList<JavaAttributeInfo>();
		for(JavaClassInfo parent : this.getParents(javaClassesList,classFile))
		{
			
			StringBuffer sb = new StringBuffer(); 
			for(JavaAttributeInfo parentAttribute: parent.getAttributes())
			{
				numAttributesDeclared++;
				if (parentAttribute.isPrivate()) continue;
				
				if(!isOverridenAttribute(parentAttribute, classFile) && !isAttributeAdded(attributeObjectList, parentAttribute))
				{
					attributeObjectList.add(parentAttribute);
					numAif++;
					sb.append(parentAttribute.toString()+", ");
					//report.addDetails("Class "+parent.getClassName()+", Attribute "+parentAttribute.toString());
				}
			}
			if (numAif > 0)
				sb.insert(0, "There are "+numAif+" attributes overriden for parent class "+ parent.getSimpleName() + ": ");

			if (sb.length() > 0)
				report.addDetails(sb.substring(0, sb.length()-1));
			
		}
		attributeObjectList.clear();
		attributeObjectList = null;
		
		attributesInherited += numAif;
		attributesDeclared += numAttributesDeclared;
		
		if (numAttributesDeclared > 0)
			report.setValue(numAif/numAttributesDeclared);
		else 
			report.setValue(0d);
		report.addDetails("There are "+numAif+" attributes inherited in this class");
		
		super.incrementNumClasses();
		
		return report;

	}

	/**
	 * 
	 * @param parentMethod
	 * @param childClassFile
	 * @return
	 */
	protected boolean isOverridenAttribute(JavaAttributeInfo parentAttribute, JavaClassInfo childClassFile)
	{
		for(JavaAttributeInfo attribute: childClassFile.getAttributes())
		{
			if (parentAttribute.isOverriden(attribute))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param methodObjectList
	 * @param method
	 * @return
	 */
	protected boolean isAttributeAdded(List<JavaAttributeInfo> attributeObjectList, JavaAttributeInfo attribute)
	{
		for(JavaAttributeInfo attributeObject: attributeObjectList)
		{
			if (attributeObject.getName().equals(attribute.getName()) &&
					attributeObject.getType().equals(attribute.getType()))
				return true;
			
		}
		return false;
	}
	
	
	/**
	 * Calculate the AIF for the SW application
	 * @param javaClassesList
	 */	
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		MetricReport report = new MetricReport();

		if (attributesDeclared > 0)
			report.setValue(attributesInherited/attributesDeclared*100);
		else
			report.setValue(0d);

		report.addDetails("Average of "+attributesInherited/super.getNumClasses()+" attributes inherited per class in the application.");
		
		return report;
	}

}
