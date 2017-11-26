package edu.umd.msswe.ece591.diagnose.metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Attributes for this class are set as follow: 
 *    modifiers typeParameters type name
 *      ( parameters ) 
 *      body
 *    modifiers type name () default defaultValue
 *
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0

 *
 */
public class JavaMethodInfo extends JavaModifiers implements Serializable {

	private String name;
	private ArrayList<JavaAttributeInfo> parameters;
	private String returnType;
	private String exceptions;
	private String parametersType;
	private StringBuffer methodContent;
	private String className;
	private String defaultName;
	private static final long serialVersionUID = 1027553947283749510L;
	private ArrayList<JavaAttributeInfo> attributes;

	public JavaMethodInfo()
	{
		attributes = new ArrayList<JavaAttributeInfo>();
		parameters = new ArrayList<JavaAttributeInfo>();
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}
	/**
	 * @param defaultName the defaultName to set
	 */
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	/**
	 * @return the exceptions
	 */
	public String getExceptions() {
		return exceptions;
	}
	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	/**
	 * @return the methodContent
	 */
	public StringBuffer getMethodContent() {
		return methodContent;
	}
	/**
	 * @param methodContent the methodContent to set
	 */
	public void setMethodContent(StringBuffer methodContent) {
		this.methodContent = methodContent;
	}
	/**
	 * @return the modifiers
	 */
	public String getModifiers() {
		return super.getModifiers();
	}
	/**
	 * @param modifiers the modifiers to set
	 */
	public void setModifiers(String modifiers) {
		super.setModifiers(modifiers);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * This method returns the parameters of the method separated  by commas
	 * ie. Type name, Type1 name1 
	 * @return the parameters
	 */
	public List<JavaAttributeInfo> getParameters() {
		return Arrays.asList(parameters.toArray(new JavaAttributeInfo[0]));
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(JavaAttributeInfo parameter) {
		this.parameters.add(parameter);
	}

	/**
	 * @return the parametersType
	 */
	public String getParametersType() {
		return parametersType;
	}
	/**
	 * @param parametersType the parametersType to set
	 */
	public void setParametersType(String parametersType) {
		this.parametersType = parametersType;
	}
	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * 
	 * @param attribute
	 */
	public void addAttribute(JavaAttributeInfo attribute)
	{
		attributes.add(attribute);	
	}

	/**
	 * 
	 */
	public List<JavaAttributeInfo> getAttributes() 
	{
		return Arrays.asList(attributes.toArray(new JavaAttributeInfo[0]));
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		if(this.getModifiers() != null)
			sb.append(this.getModifiers()+" ");
		if(returnType != null)
			sb.append(returnType+" ");
		else 
			sb.append("void ");
		sb.append(name);
		
		if (parameters.size() > 0)
			sb.append("("+parameters.toString().substring(1, parameters.toString().length()-1)+") ");
		else
			sb.append("() "); 
			
		if(exceptions != null && exceptions.trim().length() >0)
			sb.append("throws "+exceptions);

		return sb.toString().trim();
		
	}
	
	/**
	 * This method checks that the conditions are given for overriden methods 
	 * are return true if the current method and the child method mets the criteria.
	 * overriden methods criteria: same name, same return type and same parameter's type 
	 *   
	 * @param childMethod
	 * @return
	 */
	public boolean isOverriden(JavaMethodInfo childMethod)
	{
		if (!this.isPrivate() && !childMethod.isPrivate() &&
				this.getName().equals(childMethod.getName()) &&
				this.getReturnType().equals(childMethod.getReturnType()) &&
				this.equalParameters(childMethod.getParameters()))
			return true;
		
		return false;
	}
	
	protected boolean equalParameters(List<JavaAttributeInfo> childParameters)
	{
		int size = this.parameters.size();
		if (size != childParameters.size())
			return false;

		for(int i=0; i<size;i++)
		{
			if (!this.parameters.get(i).getType().equals(childParameters.get(i).getType()))
				return false;
		}
		
		return true;
	}

	public boolean isOverloaded(JavaMethodInfo childMethod)
	{
		if (this.getName().equals(childMethod.getName()) &&
				this.getReturnType().equals(childMethod.getReturnType()) &&
				!this.equalParameters(childMethod.getParameters()))
			return true;
		
		return false;
	}
}
