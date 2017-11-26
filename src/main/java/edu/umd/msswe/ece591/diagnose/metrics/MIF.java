package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * MIF Method Inheritance Factor
 * A class that inherits lots of methods (attributes) from its ancestor classes contributes 
 * to a high MIF. A child class that redefines its ancestors' methods and adds new ones contributes 
 * to a lower MIF. An independent class that does not inherit and has no children contributes 
 * to a lower MIF.
 * 
 * MIF = inherited methods/total methods available in classes
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class MIF extends Metric {

	private double methodsInherited;
	private double methodsDeclared;

	/**
	 * Calculate the MIF for the classFile class
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {

		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;

		double numMethodsDeclared = 0;
		List<JavaMethodInfo> methodObjectList = new ArrayList<JavaMethodInfo>(); 
		int numMif = 0;
		
		for(JavaClassInfo parent : this.getParents(javaClassesList,classFile))
		{
			StringBuffer sb = new StringBuffer(); 
			for(JavaMethodInfo parentMethod: parent.getMethods())
			{
				numMethodsDeclared++;
				if (parentMethod.isPrivate()) continue;
				
				if(!isOverridenMethod(parentMethod, classFile) && !isMethodAdded(methodObjectList, parentMethod))
				{
					methodObjectList.add(parentMethod);
					numMif++;
					sb.append(parentMethod.toString()+", ");
				}
			}
			if (numMif > 0)
				sb.insert(0, "There are "+numMif+" methods overriden for parent class "+ parent.getSimpleName() + ": ");

			if (sb.length() > 0)
				report.addDetails(sb.substring(0, sb.length()-1));
		}
		
		methodObjectList.clear();
		methodObjectList = null;
		
		this.methodsInherited += numMif;
		this.methodsDeclared += numMethodsDeclared;
		
		if (numMethodsDeclared > 0)
			report.setValue(numMif/methodsDeclared);
		else 
			report.setValue(0d);
		super.incrementNumClasses();
		
		return report;

	}

	/**
	 * 
	 * @param parentMethod
	 * @param childClassFile
	 * @return
	 */
	protected boolean isOverridenMethod(JavaMethodInfo parentMethod, JavaClassInfo childClassFile)
	{
		for(JavaMethodInfo method: childClassFile.getMethods())
		{
			if (parentMethod.isOverriden(method))
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
	protected boolean isMethodAdded(List<JavaMethodInfo> methodObjectList, JavaMethodInfo method)
	{
		for(JavaMethodInfo methodObject: methodObjectList)
		{
			if (methodObject.getName().equals(method.getName()) &&
					methodObject.getReturnType().equals(method.getReturnType()) &&
					methodObject.equalParameters(method.getParameters()))
				return true;
			
		}
		return false;
	}
	
	
	
	/**
	 * Calculate the MIF for the SW application
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		MetricReport report = new MetricReport();
		if (methodsDeclared > 0)
			report.setValue(methodsInherited/methodsDeclared*100);
		report.addDetails("Average of "+methodsInherited/super.getNumClasses()+" methods inherited per class.");

		return report;
	}

}
