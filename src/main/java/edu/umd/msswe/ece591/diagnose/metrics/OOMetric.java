package edu.umd.msswe.ece591.diagnose.metrics;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.reports.Report;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class OOMetric extends Report {
	
	private String definition;
	private String className;
	private String id;
	private String name;
	private String solution;
	private String values;
	private Metric metric;
	

	/**
	 * 
	 * @return
	 */
	public String getDefinition() 
	{
		return definition;
	}
	/**
	 * 
	 * @return
	 */

	public void setDefinition(String definition) 
	{
		this.definition = definition;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() 
	{
		return className;
	}

	/**
	 * 
	 * @return
	 */
	public void setClassName(String className) throws ConfigurationException
	{
		this.className = className;
		try{
			Class c = Class.forName(this.className);
			this.metric = (Metric)c.newInstance();
		} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (InstantiationException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
			throw new ConfigurationException(e);
		}

	}

	/**
	 * 
	 * @return
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public void setId(String id) 
	{
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getSolution() 
	{
		return solution;
	}

	/**
	 * 
	 * @return
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toString()
	{
		return definition + ", "+
			className + ", "+ id + ", "+ name + ", "+ solution;
		
	}

	/**
	 * @return the metric
	 */
	public Metric getMetricClass() {
		return metric;
	}
	
	public String toHtml()
	{
		return this.toXml();
	}

	public String toXml()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<metric-definition id='" + this.id + "'>" + NEW_LINE);
		
		if(name != null)
			sb.append("<name>" + name + "</name>" + NEW_LINE);
		if(definition != null )
			sb.append("<definition> <![CDATA[" + definition + "]]> </definition>" + NEW_LINE);
		if(solution != null )
			sb.append("<solution> <![CDATA[" +solution + "]]> </solution>" + NEW_LINE);

		//if(values != null )
		//	sb.append("<values> " +values + " </values>" + NEW_LINE);
				
		sb.append("</metric-definition>" + NEW_LINE);
		return sb.toString();	
	}

	public String toText()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Metric: "+id+" ("+name+")"+ NEW_LINE);
		if(definition != null )
			sb.append("Definition: " +definition + NEW_LINE);
		if(solution != null )
			sb.append("Solution: " +solution + NEW_LINE);
		//if(values != null )
		//	sb.append("Values: " +values + NEW_LINE);

		return sb.toString();
	}
	/**
	 * @return the values
	 */
	public String getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(String values) {
		this.values = values;
	}
}