package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
/**
 * CBO - Coupling between Object Classes
 * The coupling between object classes (CBO) metric represents the number of classes 
 * coupled to a given class (efferent couplings, Ce). This coupling can occur through 
 * method calls, field accesses, inheritance, arguments, return types, and exceptions. 
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 * 
 */
public class CBO extends Metric {

	private double totalNumClients=0;
	
	/**
	 * Calculate the COB for current class
	 * 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport();
		
		if (!classFile.isInterface())
		{
			int numClients =0;
			for (JavaClassInfo supplier: javaClassesList.getFiles())
			{
				if (isClient(classFile, supplier))
				{
					numClients++;
					report.addDetails(supplier.getClassName());
				}
			}
			report.setValue(numClients);
			totalNumClients += numClients;
		}
		
		super.incrementNumClasses();
		
		return report;
	}
	
	/**
	 * 
	 * @param client
	 * @param supplier
	 * @return
	 */
	protected boolean isClient(JavaClassInfo client, JavaClassInfo supplier)
	{
		if(isClient(supplier, client.getAttributes()))
				return true;
		
		if (isClientInMethod(supplier, client.getConstructors()))
			return true;
		
		if (isClientInMethod(supplier, client.getMethods()))
			return true;

		return false;
	}
	
	/**
	 * 
	 * @param supplier
	 * @param methods
	 * @return
	 */
	protected boolean isClientInMethod(JavaClassInfo supplier, List<JavaMethodInfo> methods)
	{
		for(JavaMethodInfo method: methods)
		{
			if (isClient(supplier, method.getParameters()))
				return true;
			
			if (isClient(supplier, method))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param supplier
	 * @param method
	 * @return
	 */
	protected boolean isClient(JavaClassInfo supplier, JavaMethodInfo method)
	{
		StringBuffer sb = method.getMethodContent();
		
		if (sb.indexOf(supplier.getSimpleName()+".") >= 0)
			return true;
		
		if (sb.indexOf(supplier.getSimpleName()+" ") >= 0)
			return true;

		return false;
	}
	
	/**
	 * 
	 * @param supplier
	 * @param listAttributes
	 * @return
	 */
	protected boolean isClient(JavaClassInfo supplier, List<JavaAttributeInfo> listAttributes)
	{
		for(JavaAttributeInfo attrib: listAttributes)
		{
			if (attrib.getType().equals(supplier.getSimpleName()))
				return true;
		}
		return false;
	}

	/**
	 * Calculate the CBO for all the application
	 * @param javaClassesList
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		
		MetricReport report = new MetricReport();
		report.setValue(totalNumClients/super.getNumClasses());
		report.addDetails("Average "+Math.round(report.getValue())+
				" class(es) coupled to a class within the application.");
		return report;
	}

}
