package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * POF Polymorphism Factor: measures the degree of method overriding in the class inheritance tree. 
 * It equals the number of actual method overrides divided by the maximum number of possible method overrides.
 * PF = overrides / sum for each class(new methods * descendants)
 * 
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class POF extends Metric {

	private double numOveridenMethods =0;
	private double poliformicSituations =0;
	
	/**
	 * Calculate the POF for the current class. 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;

		//calculates metric only from application related objects
		List<JavaClassInfo> parents = getParents(javaClassesList, classFile);
		if (parents.size() <= 0)
			return report;
		
		List<JavaMethodInfo> overriden = this.getOverridenMethods(parents, classFile.getMethods());
		List<JavaMethodInfo> newMethods = this.getNewMethods(parents, classFile.getMethods());
		List<String> children = super.getChildren(javaClassesList, classFile);
		
		double numOveridenMethods = overriden.size(); 
		double numNewMethods = newMethods.size();
		double numDescendants = children.size();
		
		report.setValue(0d);
		if(numNewMethods <= 0)
			report.addDetails("The class uses NO polymorphism.");
		
		else if (numDescendants <= 0)
			report.addDetails("The class uses NO inheritance.");
			
		else 
		{
			double answer = numOveridenMethods/(numNewMethods*numDescendants);
			report.setValue(answer);
			
			if(numOveridenMethods > 0)
				report.addDetails("There are "+overriden.size()+" overriden methods in the class: "+ overriden);
			if(numNewMethods > 0)
				report.addDetails("There are "+newMethods.size()+" new methods defined in the class: "+newMethods);
			if(numDescendants > 0)
				report.addDetails("There are "+children.size()+" descendents of this class: "+children);
		}

		this.numOveridenMethods += numOveridenMethods;
		this.poliformicSituations += (numNewMethods*numDescendants);
		super.incrementNumClasses();
		
		return report;
	}
	
	/**
	 * gets overriden methods from list of methods by looking in the class inheritance tree
	 * @param parentClass
	 * @param methods
	 * @return
	 */
	protected List<JavaMethodInfo> getOverridenMethods(List<JavaClassInfo> parents, List<JavaMethodInfo> methods)
	{
		List<JavaMethodInfo> l = new ArrayList<JavaMethodInfo>();
		for(JavaMethodInfo method: methods)
		{
			if (method.isPrivate()) continue;
			boolean isOverriden = false;
			
			for(JavaClassInfo parent : parents)
			{
				if (isOverridenMethod(parent, method))
				{
					isOverriden = true;
					break;
				}
			}
			
			if (isOverriden)
				l.add(method);
		}
		return l;
	}
	
	/**
	 * 
	 * @param parentClass
	 * @param method
	 * @return
	 */
	protected boolean isOverridenMethod(JavaClassInfo parentClass, JavaMethodInfo method)
	{
		for(JavaMethodInfo parentMethod: parentClass.getMethods())
		{
			if (!parentMethod.isPrivate() && parentMethod.isOverriden(method))
				return true;
		}
		return false;
	}
	
	/**
	 * gets new methods from list of methods by looking in the class inheritance tree
	 * @param parentClass
	 * @param methods
	 * @return
	 */
	protected List<JavaMethodInfo> getNewMethods(List<JavaClassInfo> parents, List<JavaMethodInfo> methods)
	{
		List<JavaMethodInfo> l = new ArrayList<JavaMethodInfo>();
		for(JavaMethodInfo method: methods)
		{
			if (method.isPrivate()) continue;
			boolean isOverriden = false;
			
			for(JavaClassInfo parent : parents)
			{
				if (isOverridenMethod(parent, method))
				{
					isOverriden = true;
					break;
				}
			}
			
			if (!isOverriden)
				l.add(method);
		}
		return l;

	}
	
	/**
	 * Calculate the POF for the application
	 * 
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		
		MetricReport report = new MetricReport();
		if (poliformicSituations != 0)
			report.setValue(numOveridenMethods/poliformicSituations*100);
		else 
			report.setValue(0d);
		
		
		report.addDetails("Average of "+Math.round(numOveridenMethods/super.getNumClasses())+
				" overriden methods per class.");
		return report;
	}
	
}

