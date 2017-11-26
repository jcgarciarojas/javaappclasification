package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class StructureBuilder implements Builder {

	private ApplicationCompiler compiler;
	private StructureReader m_StructureReader;
	private List<File> filesList;
	private JavaApplication javaApplication;
	private Object[] params;
	
	/**
	 * 
	 * @param m_StructureReader
	 */
	public StructureBuilder(ApplicationCompiler compiler, StructureReader m_StructureReader)
	{
		this.compiler = compiler;
		this.m_StructureReader = m_StructureReader;
	}

	/**
	 * This method is expecting String[] params in the following format: 
	 * String directories, String filters, String workingDirectory, String additionalClassPath
	 */
	public void setParameters(String[] params) throws ConfigurationException
	{
		validateDirectory(params[0]);
		validateDirectory(params[2]);
		validateDirectory(params[3]);
		this.params = params;
	}
	
	/**
	 * 
	 */
	public Object getObject(){
		return javaApplication;
	}
	
	/**
	 * 
	 */
	public void build() throws ConfigurationException
	{
		filesList = m_StructureReader.process((String)params[0], (String)params[1]);

		if (filesList.size()<=0)
			throw new ConfigurationException("there are not valid files in the directory " + (String)params[0]);
	}
	
	/**
	 * 
	 */
	public void buildParts() throws ConfigurationException
	{
		compiler.process(filesList, (String)params[2], (String)params[3]);
		javaApplication = compiler.getJavaApplicationObject();
	}
	
	protected void validateDirectory(String dirs) throws ConfigurationException
	{
		if (dirs == null) return;
		
		StringTokenizer st = new StringTokenizer(dirs, ",;");
		File file = null;
		while(st.hasMoreTokens())
		{
			String strFile = st.nextToken();
			if (strFile == null && strFile.trim().length()<=0) 
				continue;
			
			file = new File(strFile);
			if (!file.exists())
				throw new ConfigurationException ("File doesn't exist "+file.toString());
				
		}
	}

}