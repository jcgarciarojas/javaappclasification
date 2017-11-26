package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.configuration.JavaClassInfoFactory;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class ProxyClassInfo extends JavaClassInfo {

	private static final long serialVersionUID = 10275539472837496L;
	private RealClassInfo classInfo;
	
	/**
	 * 
	 * @param packageName
	 * @param name
	 * @param serializebleClassId
	 */
	public ProxyClassInfo(String packageName, String name, String serializebleClassId)
	{
		super(packageName, name, serializebleClassId);
	}
	
	/**
	 * 
	 */
	public List<JavaAttributeInfo> getAttributes()
	{
		this.initRealClass();
		return classInfo.getAttributes();
	}
	
	/**
	 * 
	 */
	public String getMembers()
	{
		this.initRealClass();
		return classInfo.getMembers();
	}

	/**
	 * 
	 */
	public List<JavaMethodInfo> getMethods()
	{
		this.initRealClass();
		return classInfo.getMethods();
	}
	
	public List<JavaMethodInfo> getConstructors()
	{
		this.initRealClass();
		return classInfo.getConstructors();
	}
	
	/**
	 * 
	 */
	public String getModifiers()
	{
		this.initRealClass();
		return classInfo.getModifiers();
	}
	
	/**
	 * 
	 */
	public String getParameters()
	{
		this.initRealClass();
		return classInfo.getParameters();
	}
	
	/**
	 * 
	 */
	public String getParent()
	{
		this.initRealClass();
		return classInfo.getParent();
	}
	
	/**
	 * 
	 */
	public String getPath()
	{
		this.initRealClass();
		return classInfo.getPath();
	}
	
	/**
	 * 
	 */
	public StringBuffer getSourceContent()
	{
		this.initRealClass();
		return classInfo.getSourceContent();
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		this.initRealClass();
		return classInfo.getInterfaceName();
	}
	
	public boolean isInterface()
	{
		this.initRealClass();
		return classInfo.isInterface();
	}
	
	public boolean isAbstract()
	{
		this.initRealClass();
		return classInfo.isAbstract();
	}
	
	public boolean isClass()
	{
		this.initRealClass();
		return classInfo.isClass();	
	}

	public boolean isPublic()
	{
		this.initRealClass();
		return classInfo.isPublic();
	}
	
	public boolean isPrivate()
	{
		this.initRealClass();
		return classInfo.isPrivate();
	}
	
	public boolean isProtected()
	{
		this.initRealClass();
		return classInfo.isProtected();
	}
	
	public boolean isDefault()
	{
		this.initRealClass();
		return classInfo.isDefault();
	}

	public boolean isStatic()
	{
		this.initRealClass();
		return classInfo.isStatic();
	}

	public boolean isFinal()
	{
		this.initRealClass();
		return classInfo.isFinal();
	}

	public boolean isTransient()
	{
		this.initRealClass();
		return classInfo.isTransient();
	}

	public boolean isNative()
	{
		this.initRealClass();
		return classInfo.isNative();
	}
	
	public boolean isVolatile()
	{
		this.initRealClass();
		return classInfo.isVolatile();
	}

	public boolean isSynchronized()
	{
		this.initRealClass();
		return classInfo.isSynchronized();
	}

	/**
	 * 
	 *
	 */
	private void initRealClass()
	{
		if (classInfo == null)
			classInfo = JavaClassInfoFactory.getInstance().deserializeRealClassInfo(this);
	}
		
}
