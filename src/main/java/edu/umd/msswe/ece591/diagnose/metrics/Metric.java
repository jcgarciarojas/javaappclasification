package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
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

public abstract class Metric {
	
	private int numClasses;

	/**
	 * This method should return the value of the metric for the current class
	 * it should return null or an empty object if this value cannot be calculated
	 *
	 * @param structure 
	 * @param class
	 *
	 * @return the value of the metric for the whole system
	 */
	public abstract MetricReport execute(JavaApplication javaClassesList, JavaClassInfo classFile) throws MetricsException;

	/**
	 * This method should return the value of the metric for all the system
	 * it can potentially the average, but that depends on the equation 
	 * it should return null or an empty object if this value cannot be calculated
	 *  
	 * @param structure 
	 * @return the value of the metric for the whole system
	 */
	public abstract MetricReport execute(JavaApplication javaClassesList) throws MetricsException;

	/**
	 * @return the numClasses
	 */
	protected final synchronized void incrementNumClasses() {
		numClasses++;
	}

	/**
	 * @return the numClasses
	 */
	protected final synchronized int getNumClasses() {
		return numClasses;
	}

	/**
	 * @param numClasses the numClasses to set
	 */
	protected final synchronized void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}
	
	/**
	 * Get the JavaClassInfo from a simple class name (class name without package)
	 * if simpleClassName is null then returns a null JavaClassInfo object  
	 * @param javaClassesList
	 * @param simpleClassName
	 * @return
	 */
	protected JavaClassInfo getJavaClassInfo(JavaApplication javaClassesList, String simpleClassName)
	{
		JavaClassInfo classInfo = null;
		
		if(simpleClassName != null)
		{
			for (JavaClassInfo classFile:javaClassesList.getFiles())
			{
				if ((classFile.getPackageName().length() <= 0 && classFile.getClassName().equals(simpleClassName)) ||
					(classFile.getPackageName().length() > 0 && classFile.getClassName().endsWith("."+simpleClassName)))
				{
					classInfo = classFile;
					break;
				}
			}
		}
		
		return classInfo;
	}

	/**
	 * 
	 * @param javaClassesList
	 * @param classFile
	 * @return
	 */
	protected List<JavaClassInfo> getParents(JavaApplication javaClassesList, JavaClassInfo classFile)
	{
		List<JavaClassInfo> l = new ArrayList<JavaClassInfo>();
		
		while(true)
		{
			classFile = this.getJavaClassInfo(javaClassesList, classFile.getParent());
			if(classFile == null)
				break;
			l.add(classFile);
		}
		
		return l;
	}
	
	/**
	 * get children from the current class
	 * @param javaClassesList
	 * @param currentClassFile
	 * @return
	 */
	protected List<String> getChildren(JavaApplication javaClassesList, JavaClassInfo currentClassFile)
	{
		List<String> files= new ArrayList<String>();
		for(JavaClassInfo classFile: javaClassesList.getFiles())
		{
			if(classFile.getParent() != null && classFile.getParent().equals(currentClassFile.getSimpleName()))
				files.add(classFile.getSimpleName());
		}
		return files;
	}
	
}