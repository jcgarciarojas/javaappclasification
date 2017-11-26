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

public interface Builder {

	public Object getObject();
	
	public void setParameters(String[] params) throws ConfigurationException;
	public void build() throws ConfigurationException;
	public void buildParts() throws ConfigurationException;

}