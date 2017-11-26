package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */
public class COF extends Metric {

	private double totalNumClients=0;
	
	/**
	 * Calculate the COF for current class
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
				if (supplier.getClassName() != null && 
						supplier.getClassName().equals(classFile.getClassName()))
					continue;
				if (isClient(classFile, supplier))
					numClients++;
			}
		
			totalNumClients += numClients;
		}
		
		this.incrementNumClasses();
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
		
		//static client
		if (sb.indexOf(supplier.getSimpleName()+".") >= 0)
			return true;

		//non static client
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
	 * Calculate the COF for the application
	 * 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {

		MetricReport report = new MetricReport();
		
		double tc = this.getNumClasses();
		if (tc > 0)
		{
			double t2 = tc*tc;//Math.pow(tc,2d);
			double d = t2 - tc;
			double metric = totalNumClients / d;
			report.setValue(metric*100);
			report.addDetails("Average of "+Math.round(totalNumClients/tc)+
				" couplings per class.");
		}
		
		return report;
	}

}
