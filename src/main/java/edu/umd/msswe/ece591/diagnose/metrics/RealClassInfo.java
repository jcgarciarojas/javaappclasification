package edu.umd.msswe.ece591.diagnose.metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class RealClassInfo extends JavaClassInfo implements Serializable {
	
	private static final long serialVersionUID = 10275539472837495L;
	private String path;
	private String parent;
	private String interfaceName;
	private String members;
	private String parameters;
	private StringBuffer sourceContent;
	private ArrayList<JavaAttributeInfo> attributes;
	private ArrayList<JavaMethodInfo> methods;
	private ArrayList<JavaMethodInfo> constructors;
	
	/**
	 * 
	 * @param packageName
	 * @param name
	 * @param serializebleClassId
	 */
	public RealClassInfo(String packageName, String name, String serializebleClassId) 
	{
		super(packageName, name, serializebleClassId);
		attributes = new ArrayList<JavaAttributeInfo>();
		methods = new ArrayList<JavaMethodInfo>();
		constructors = new ArrayList<JavaMethodInfo>();
	}

	/**
	 * 
	 * @param method
	 */
	public void addMethods(JavaMethodInfo method)
	{
		methods.add(method);
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
	public List<JavaAttributeInfo> getAttributes() {
		return Arrays.asList(attributes.toArray(new JavaAttributeInfo[0]));
	}

	/**
	 * 
	 */
	public String getMembers() {
		return members;
	}
	
	/**
	 * 
	 * @param members
	 */
	public void setMembers(String members) {
		this.members = members;
	}
	
	/**
	 * 
	 */
	public List<JavaMethodInfo> getMethods() {
		return Arrays.asList(methods.toArray(new JavaMethodInfo[0]));
	}

	/**
	 * 
	 */
	public String getModifiers() {
		return super.getModifiers();
	}
	
	/**
	 * 
	 * @param modifiers
	 */
	public void setModifiers(String modifiers) {
		super.setModifiers(modifiers);
	}

	
	/**
	 * 
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * 
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * 
	 */
	public String getParent() {
		return parent;
	}
	
	/**
	 * 
	 * @param parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	/**
	 * 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 
	 */
	public StringBuffer getSourceContent() {
		return sourceContent;
	}
	
	/**
	 * 
	 * @param sourceContent
	 */
	public void setSourceContent(StringBuffer sourceContent) {
		this.sourceContent = sourceContent;
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the constructors
	 */
	public List<JavaMethodInfo> getConstructors() {
		return Arrays.asList(constructors.toArray(new JavaMethodInfo[0]));
	}

	/**
	 * @param constructors the constructors to set
	 */
	public void addConstructors(JavaMethodInfo method)
	{
		this.constructors.add(method);
	}

}