package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.*;

import edu.umd.msswe.ece591.diagnose.reports.Report;


/**
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0

 */
public class JavaApplication extends Report {

	private Hashtable<String, JavaClassInfo> classes;
	private List<String> packages;
	private String title;

	public JavaApplication()
	{
		classes = new Hashtable<String, JavaClassInfo>();
	}
	
	/**
	 * 
	 * @param classObject
	 */
	public void addClassObjects(List<JavaClassInfo> classObjects)
	{
		for (JavaClassInfo classObject: classObjects)
			this.addClassObject(classObject);
		
	}

	/**
	 * 
	 * @param classObject
	 */
	public void addClassObject(JavaClassInfo classObject)
	{
		classes.put(classObject.getClassName(), classObject);
		
	}

	/**
	 * 
	 * @param className
	 */
	public JavaClassInfo getClassObject(String className)
	{
		return classes.get(className);
	}

	/**
	 * 
	 * @return
	 */
	public List<JavaClassInfo> getFiles()
	{
		return new ArrayList<JavaClassInfo>(classes.values());
	}
	
	public List<String> getJavaNames()
	{
		return new ArrayList<String>(classes.keySet());
	}
	
	public String toString()
	{
		return classes.toString();
	}
	
	public int getNumberOfClasses()
	{
		return classes.size();
	}
	
	public int getNumberOfPackages()
	{
		if (packages == null)
		{
			packages = new ArrayList<String>();
			for(JavaClassInfo javaClass: classes.values())
			{
				if (!packages.contains(javaClass.getPackageName()))
					packages.add(javaClass.getPackageName());
			}
		}
		return packages.size();
	}

	public String toHtml()
	{
		return this.toXml();	
	}

	public String toXml()
	{
		StringBuffer sb = new StringBuffer(); 
		
		if (getTitle() != null)
			sb.append("<application-name>"+getTitle()+" </application-name>" + NEW_LINE);
		
		int numPackages = this.getNumberOfPackages();
		if(numPackages > 0)
			sb.append("<total-packages-analyzed>"+numPackages+" </total-packages-analyzed>" + NEW_LINE);
		
		int numClasses = this.getNumberOfClasses();
		if(numClasses > 0)
			sb.append("<total-classes-analyzed> "+numClasses+"</total-classes-analyzed>" + NEW_LINE);
		return sb.toString();	
	}

	public String toText()
	{
		StringBuffer sb = new StringBuffer();
		//sb.append("Application Name " + getTitle() + NEW_LINE);
		sb.append("Number of packages analyzed: " + getNumberOfPackages() + NEW_LINE);
		sb.append("Number of classes analyzed: " + getNumberOfClasses() + NEW_LINE);
		return sb.toString();	
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}	
}