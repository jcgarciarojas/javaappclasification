package edu.umd.msswe.ece591.diagnose.metrics;

import java.io.Serializable;

/**
 * Attributes for this class are set as follow: 
 * modifiers type name initializer;
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0

 *
 */
public class JavaAttributeInfo extends JavaModifiers implements Serializable {

	private static final long serialVersionUID = 1027553947283749510L;
	private String name;
	private String type;
	private String className;
	private String initializer;
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
	 * @return the initializer
	 */
	public String getInitializer() {
		return initializer;
	}
	/**
	 * @param initializer the initializer to set
	 */
	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}
	/**
	 * @return the modifiers
	 */
	public String getModifiers() {
		return super.getModifiers();
	}
	/**
	 * public static
	 * 
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if (this.getModifiers() != null)
			sb.append(this.getModifiers() + " ");
		if (type != null)
			sb.append(type + " ");
		sb.append(name + " ");

		return sb.toString().trim();
	}
	
	public boolean isOverriden(JavaAttributeInfo childAttribute)
	{
		if (!this.isPrivate() && !childAttribute.isPrivate() &&
				this.getName().equals(childAttribute.getName()) &&
				this.getType().equals(childAttribute.getType()))
			return true;
		
		return false;
	}
}
