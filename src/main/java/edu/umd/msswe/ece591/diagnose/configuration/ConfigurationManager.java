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

public class ConfigurationManager {

	private Builder structureBuilder;
	private Builder configBuilder;

	/**
	 * 
	 * @param structureBuilder
	 * @param configBuilder
	 */
	public ConfigurationManager(Builder structureBuilder, Builder configBuilder)
	{
		this.structureBuilder = structureBuilder;
		this.configBuilder = configBuilder;
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public Object getConfig() throws ConfigurationException
	{
		return this.getObject(configBuilder, null);
	}

	/**
	 * This method is expecting the following a String[] params: 
	 * String directories, String filters, String workingDirectory 
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public Object getStructure(String[] params) throws ConfigurationException
	{
		
		return this.getObject(structureBuilder, params);
	}
	
	/**
	 * 
	 * @param builder
	 * @return
	 * @throws ConfigurationException
	 */
	private Object getObject(Builder builder, String[] params) throws ConfigurationException
	{
		builder.setParameters(params);
		builder.build();
		builder.buildParts();
		return builder.getObject();
	}

}