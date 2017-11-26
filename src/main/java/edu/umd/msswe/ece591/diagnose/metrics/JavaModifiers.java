package edu.umd.msswe.ece591.diagnose.metrics;

import java.io.Serializable;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */
public abstract class JavaModifiers implements Serializable {
	
	private String modifiers;
	
	public boolean isInterface()
	{
		if(modifiers != null && modifiers.contains("interface"))
			return true;
		else
			return false;
	}
	
	public boolean isAbstract()
	{
		if(modifiers != null && modifiers.contains("abstract"))
			return true;
		else
			return false;
	}
	
	public boolean isClass()
	{
		if(modifiers != null && modifiers.contains("class"))
			return true;
		else
			return false;
	}

	public boolean isPublic()
	{
		if(modifiers != null && modifiers.contains("public"))
			return true;
		else
			return false;
	}
	
	public boolean isPrivate()
	{
		if(modifiers != null && modifiers.contains("private"))
			return true;
		else
			return false;
	}
	
	public boolean isProtected()
	{
		if(modifiers != null && modifiers.contains("protected"))
			return true;
		else
			return false;
	}
	
	public boolean isDefault()
	{
		if(modifiers == null ||
			(!modifiers.contains("public") && 
			!modifiers.contains("private") &&
			!modifiers.contains("protected")))
			return true;
		else
			return false;
	}

	public boolean isStatic()
	{
		if(modifiers != null && modifiers.contains("static"))
			return true;
		else
			return false;
	}

	public boolean isFinal()
	{
		if(modifiers != null && modifiers.contains("final"))
			return true;
		else
			return false;
	}

	public boolean isTransient()
	{
		if(modifiers != null && modifiers.contains("transient"))
			return true;
		else
			return false;
	}

	public boolean isNative()
	{
		if(modifiers != null && modifiers.contains("native"))
			return true;
		else
			return false;
	}
	
	public boolean isVolatile()
	{
		if(modifiers != null && modifiers.contains("volatile"))
			return true;
		else
			return false;
	}

	public boolean isSynchronized()
	{
		if(modifiers != null && modifiers.contains("synchronized"))
			return true;
		else
			return false;
	}

	/**
	 * @return the modifiers
	 */
	public String getModifiers() {
		return modifiers;
	}

	/**
	 * @param modifiers the modifiers to set
	 */
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
}
