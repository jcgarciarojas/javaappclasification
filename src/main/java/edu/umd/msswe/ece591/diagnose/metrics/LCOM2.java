package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * Lack of Cohesion
 * LCOM2 equals the percentage of methods that do not access a 
 * specific attribute averaged over all attributes in the class. 
 * If the number of methods or attributes is zero, 
 * LCOM2 is undefined and displayed as zero. 
 * 
 * LCOM2 = 1 - (sum(mA)/(m*a)) 
 * m = number of procedures (methods) in class 
 * a = number of variables (attributes) in class 
 * mA = number of methods that access a variable (attribute)
 * sum(mA)sum of mA over attributes of a class
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class LCOM2 extends Metric {

	private double total;
	//private static final String specialCharacters = "~`!@#$%^&*_-+={[}]|\\:;\"'?/><,\t\n() ";

	/**
	 * calculate LCOM2 for the current java class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport(); 
		if (classFile.isInterface())
			return report;
		
		double numAttributes = classFile.getAttributes().size();
		double numMethods = classFile.getMethods().size();
		
		
		double metric = 0;
		
		if (numAttributes > 0 && numMethods > 0)
		{
			report.addDetails("Attributes accessed by methods: ");
			//gets the variables used in the methods
			List<JavaAttributeInfo> attInfoList = classFile.getAttributes();
			HashMap<JavaAttributeInfo, Integer> map = this.processMethods(attInfoList, 
					classFile.getMethods());
			
			double sumMethodsThatAccessGV = 0;
			
			Iterator<JavaAttributeInfo> it = map.keySet().iterator();
			while(it.hasNext())
			{
				JavaAttributeInfo attInfo = it.next();
				int count = map.get(attInfo);
				//report.addDetails(attInfo.getModifiers() + " " + attInfo.getType() + " " + 
				//		attInfo.getName() +" accessed by " + count + " methods");
				report.addDetails(attInfo.getType() +" "+attInfo.getName()+" accessed by " + count + " method(s)");
				sumMethodsThatAccessGV += (double)count;
			}
			metric = (1d - (sumMethodsThatAccessGV /(numMethods*numAttributes)))*100;
			
		} else
			report.addDetails("There are not attributes or methods defined in this class.");
			
		report.setValue(metric);
		//report.addDetails("The class has "+metric+" cohesion measurement.");

		total += metric;
		super.incrementNumClasses();
		
		return report;
	}
	
	/**
	 * 
	 * This method counts the class variables that are present in the java methods and return a HashMap 
	 * with the count of variables in the methods
	 * a hashMap is return with the attributes present in the method 
	 * @param classAttributes
	 * @param methods
	 * @return
	 */
	protected HashMap<JavaAttributeInfo, Integer> processMethods(List <JavaAttributeInfo> classAttributes, 
			List<JavaMethodInfo> methods)
	{
		//hashmap to store the variable's count 
		HashMap<JavaAttributeInfo, Integer> mapCount = new HashMap<JavaAttributeInfo, Integer>();
		
		ArrayList<JavaAttributeInfo> listAttsMethod = new ArrayList<JavaAttributeInfo>(); 
		//loop through all the methods in the class
		for(JavaMethodInfo method: methods)
		{
			//add attributes and parameters mthod to list
			listAttsMethod.addAll(method.getParameters());
			listAttsMethod.addAll(method.getAttributes());
			String str = getMethodNoSpaceNewLineCharacters(method.getMethodContent().toString());
			for (JavaAttributeInfo classAttribute: classAttributes)
			{
				//build search string, with/our this. substring
				String searchFor = classAttribute.getName();
				if(isClassAttributeDefinedAlsoAsAttributeInMethod(classAttribute, listAttsMethod))
					searchFor = "this."+searchFor;
				//remove new line and spaces to make sure that name. subsctring is found 
				if (str.indexOf(searchFor) >= 0)
				{
					int count = 1;
					if (mapCount.get(classAttribute) != null)
						count = mapCount.get(classAttribute) + 1;
					mapCount.put(classAttribute, count);
				}
			}
			listAttsMethod.clear();
		}
		
		return mapCount;
	}
	
	protected String getMethodNoSpaceNewLineCharacters(String method)
	{
		
		return method.replace("\n", "").replace(" ", "").replace("\r", "");
	}
	
	/**
	 * 
	 * @param classAttribute
	 * @param listAttsMethod
	 * @return
	 */
	protected boolean isClassAttributeDefinedAlsoAsAttributeInMethod(JavaAttributeInfo classAttribute, ArrayList<JavaAttributeInfo> listAttsMethod)
	{
		for(JavaAttributeInfo att: listAttsMethod)
		{
			if(classAttribute.getName().equals(att.getName()))
				return true;
		}
		return false;
	}
	
	/**
	 * calculate RFC for the whole application
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		
		MetricReport report = new MetricReport();
		report.setValue(total/super.getNumClasses());
		report.addDetails("Average of "+Math.round(report.getValue())+" cohesion measurement per class.");
		
		return report;
	}

}
