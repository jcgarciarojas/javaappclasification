package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

public class RFC extends Metric {
	
	private double total;
	private static final String specialCharacters = "~`!@#$%^&*_-+={[}]|\\:;\"'?/><,\t\n() ";

	/**
	 * This method calculate RFC for the current class
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		
		MetricReport report = new MetricReport();
		if (classFile.isInterface())
			return report;
		
		report.addDetails("Methods that can be invoked in response of a message:");

		//process RFC methods from the constructors
		this.processRFCMethodsInList(javaClassesList, classFile.getAttributes(), 
				report, classFile.getConstructors());
		//process RFC methods from the methods
		this.processRFCMethodsInList(javaClassesList, classFile.getAttributes(), 
				report, classFile.getMethods());

		//add RFC methods from the class itself
		for (JavaMethodInfo method: classFile.getMethods())
		{
			if (method.isAbstract())
				continue;
			
			if (method.getParameters().size() > 0)
				report.addDetails(classFile.getSimpleName() +"."+ method.getName()+"("+method.getParameters()+")");
			else
				report.addDetails(classFile.getSimpleName() +"."+ method.getName()+"()");
		}
		
		double metric = report.getDetail().size();
		//report.getDetail().clear();
		report.setValue(metric);
		//report.addDetails("There are "+metric+" method(s) that can be invoked in response for a message .");

		total += metric;
		super.incrementNumClasses();
		return report;
	}
	
	/**
	 * This method looks for RFC methods within a list of methods and add then 
	 * to the metric's report. 
	 */
	protected void processRFCMethodsInList(JavaApplication javaClassesList, List<JavaAttributeInfo> classAttributes,
			MetricReport report, List<JavaMethodInfo> methods)
	{
		
		for (JavaMethodInfo method: methods)
		{
			ArrayList<JavaAttributeInfo> attributesMethod = new ArrayList<JavaAttributeInfo>(); 
			attributesMethod.addAll(method.getParameters());
			attributesMethod.addAll(method.getAttributes());
			this.getRfcFromMethodContent(javaClassesList, classAttributes, attributesMethod, 
					method.getMethodContent(), report);
			
			attributesMethod = null;
		}
				
	}

	/**
	 * This method looks for valid RFC methods within the method content and adds them to the metric report
	 * @param javaClassesList
	 * @param this list of attributes includes parameters, attributes within the method and class attributes
	 * @param method
	 * @param l
	 */
	protected void getRfcFromMethodContent(JavaApplication javaClassesList, List<JavaAttributeInfo> classAttributes, 
			List<JavaAttributeInfo> attributesMethod, StringBuffer methodContent, MetricReport report)
	{
		StringTokenizer st = new StringTokenizer(methodContent.toString(), "\n");
		while(st.hasMoreTokens())
		{
			String line = st.nextToken();
			int index = line.indexOf(".");
			if ( index > 0 && index < line.lastIndexOf("("))
			{
				this.getRfcFromLineContent(javaClassesList, classAttributes, attributesMethod, line, report);
			}
			
			line = null;
		}
		st = null;
	}
	
	/**
	 * This method looks for valid RFC methods in the line source code and 
	 * adds them to the metric report if they haven't been added  
	 * 
	 * @param javaClassesList
	 * @param attributes
	 * @param line
	 * @param l
	 */
	protected void getRfcFromLineContent(JavaApplication javaClassesList, List<JavaAttributeInfo> classAttributes, 
			List<JavaAttributeInfo> attributesMethod, String line, MetricReport report)
	{
		StringTokenizer st = new StringTokenizer(line, specialCharacters);
		
		while(st.hasMoreTokens())
		{
			String method = this.getRfcFromToken(javaClassesList, classAttributes, attributesMethod, st.nextToken());
			if (method != null && !report.getDetail().contains(method))
				report.addDetails(method);
		}
		st = null;
		
	}
	
	/**
	 * This method checks if the String token contains a dot and if so then it checks 
	 * if the attirbute is a vlaid class within the application and 
	 * if the method is really a valid method for that class.  
	 * @param javaClassesList
	 * @param attributes
	 * @param token
	 * @return
	 */
	protected String getRfcFromToken(JavaApplication javaClassesList, List<JavaAttributeInfo> classAttributes, 
			List<JavaAttributeInfo> attributesMethod, String token)
	{
				
		JavaAttributeInfo attribute = null;
		if (getElementInList(attributesMethod, token, true, false) != null)
			attribute = getElementInList(classAttributes, token, false, true);
		else
			attribute = getElementInList(classAttributes, token, false, false);
		
		String rfcMethod = null;
		if(attribute != null)
			rfcMethod = this.getRfcMethod(javaClassesList, attribute, token.replace("this.", ""));
		
		return rfcMethod;
	}

	/**
	 * 
	 * @param attributeList
	 * @param token
	 * @param isMethodList
	 * @param foundInMethod
	 * @return
	 */
	protected JavaAttributeInfo getElementInList(List<JavaAttributeInfo> attributeList, String token, 
			boolean isAttributeMethodList, boolean attributeFoundInMethod)
	{
		JavaAttributeInfo attReturn = null;
		if (token != null && token.trim().length() > 0)
		{
			boolean found = false;
			for(JavaAttributeInfo attribute: attributeList)
			{
				if(isAttributeMethodList)
					found = token.startsWith(attribute.getName());
				else
				{
					if(attributeFoundInMethod)
						found = token.startsWith("this."+attribute.getName()+".");
					else
						found = token.startsWith("this."+attribute.getName()+".") || token.startsWith(attribute.getName()+".");
				}
				
				if (found)
				{
					attReturn = attribute;
					break;
				}
			}
		}
		return attReturn;
	}
	
	/**
	 * This method checks if the attribute exists as a class in the application and validates 
	 * that there is a valid method for the attribute in the token String
	 *  
	 * @param javaClassesList
	 * @param attribute
	 * @param token
	 * @return
	 */
	protected String getRfcMethod(JavaApplication javaClassesList, JavaAttributeInfo attribute, String token)
	{
		String rfcMethod = null;
		JavaClassInfo classInfo = super.getJavaClassInfo(javaClassesList, attribute.getType());
		
		if(classInfo == null)
			return rfcMethod;

		for(JavaMethodInfo method : classInfo.getMethods())
		{
			if (token.equals(attribute.getName()+"."+method.getName()))
			{
				rfcMethod = classInfo.getSimpleName()+"."+method.getName()+"()";
				break;
			}
		}
		return rfcMethod;
	}


	/**
	 * calculate RFC for the whole application
	 */
	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {

		MetricReport report = new MetricReport();
		report.setValue(total/super.getNumClasses());
		report.addDetails("Average of "+Math.round(report.getValue())+
				" method(s) per class that can be invoked in response for a message.");
		
		return report;
	}
	
}
