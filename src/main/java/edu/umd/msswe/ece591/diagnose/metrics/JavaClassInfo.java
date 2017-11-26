package edu.umd.msswe.ece591.diagnose.metrics;

import java.io.Serializable;
import java.util.List;

/**
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0

 *
 */
public abstract class JavaClassInfo extends JavaModifiers implements Serializable {

	private String serializebleClassId;
	private String name ="";
	private String packageName ="";

	/**
	 * 
	 * @param packageName
	 * @param name
	 * @param serializebleClassId
	 */
	public JavaClassInfo(String packageName, String name, String serializebleClassId) 
	{
		this.name = name;
		this.serializebleClassId = serializebleClassId;
		this.packageName = packageName;
	}
	public abstract List<JavaAttributeInfo> getAttributes();
	
	/**
	 * This method returns a list of all the members of the class separated by comma.
	 * This list contains the attributes, constructors and methods
	 * @return
	 */
	public abstract String getMembers();
	
	public abstract List<JavaMethodInfo> getMethods();
	public abstract List<JavaMethodInfo> getConstructors();
	
	/**
	 * this method returns the modifiers of the class/interface
	 * ie. public abstract, public interface 
	 * @return
	 */
	public abstract String getParameters();
	public abstract String getParent();
	public abstract String getPath();
	public abstract StringBuffer getSourceContent();
	public abstract String getInterfaceName();
	
	/**
	 * @return the name
	 */
	public String getSimpleName() {
		return name;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @return the serializebleClassId
	 */
	public String getSerializebleClassId() {
		return serializebleClassId;
	}
	
	public String getClassName()
	{
		if (packageName == null || packageName.trim().length() <1)
			return name;
		else
		return packageName+"."+name;
	}
	
	public String toString()
	{
		return "package "+
		getPackageName()+
		", class name " + getSimpleName()+
		", full class name "+getClassName();
	}

}