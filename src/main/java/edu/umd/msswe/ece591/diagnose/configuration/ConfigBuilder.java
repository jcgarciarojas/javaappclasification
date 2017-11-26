package edu.umd.msswe.ece591.diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class ConfigBuilder implements Builder {

	private XmlReader xmlReader;
	private SystemConfiguration configuration;

	/**
	 * 
	 * @param xmlReader
	 */
	public ConfigBuilder(XmlReader xmlReader)
	{
		this.xmlReader = xmlReader;
	}
	
	public void setParameters(String[] params)
	{
		return;
	}

	/**
	 * 
	 */
	public Object getObject(){
		return configuration;
	}

	/**
	 * 
	 */
	public void build() throws ConfigurationException
	{
		configuration = new SystemConfiguration();
	}
	
	/**
	 * 
	 */
	//TODO: code should validate if the fuzzy set defined in the rule is a vlaid fuzzy set
	public void buildParts() throws ConfigurationException
	{
		this.xmlReader.readFuzzySetTypes();
		
		configuration.setFuzzyRulesEngine(this.xmlReader.createFuzzyEngine());
		configuration.setDefMethod(this.xmlReader.createDefuzzificationMethod());
		configuration.setInferenceMethod(this.xmlReader.createInferenceMethod());

		configuration.setFuzzyRules(this.xmlReader.readFuzzyRules());
		configuration.setFuzzyRulesClass(this.xmlReader.readFuzzyRulesClass());
		
		configuration.setFuzzySets(this.xmlReader.readFuzzySets());
		configuration.setOoMetrics(this.xmlReader.readOOMetrics());
		configuration.setFuzzyOutput(this.xmlReader.readFuzzyOutput());

	}
	
}