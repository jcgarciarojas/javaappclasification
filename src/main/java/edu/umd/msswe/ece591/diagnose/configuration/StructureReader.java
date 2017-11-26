package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class StructureReader {
	
	private String validFileExtensions = "java";
	

	/**
	 * 
	 * @param directories
	 * @param filters
	 * @return
	 * @throws ConfigurationException
	 */
	public List<File> process(String directories, String filters) throws ConfigurationException
	{
		return this.process(this.getDirectories(directories), this.getFilters(filters));
	}
	
	/**
	 * 
	 * @param directories
	 * @return
	 */
	protected List<File> getDirectories(String directories)
	{
		List<File> l = new ArrayList<File>();
		StringTokenizer st = new StringTokenizer(directories, ";"); 
		
		while(st.hasMoreTokens())
			l.add(new File(st.nextToken()));
		
		return l;
	}

	/**
	 * 
	 * @param filters
	 * @return
	 */
	protected List<String> getFilters(String filters)
	{
		List<String> l = new ArrayList<String>();
		
		if (filters != null)
		{
			StringTokenizer st = new StringTokenizer(filters, ","); 
			
			while(st.hasMoreTokens())
				l.add(st.nextToken());
		}	
		return l;
	}
	/**
	 * 
	 */
	public List<File> process(List<File> directories, List<String> filters) throws ConfigurationException
	{
		List<File> files = new Vector<File>();
		
		for(File initialDir: directories)
			getFileNames(files, initialDir, filters);
		
		return files;
	}

	/**
	 * 
	 * @param directory
	 * @return
	 * @throws ConfigurationException
	 */
	private void getFileNames(List<File> files, File dir, List<String> filters) 
	{
		for(File file: dir.listFiles())
		{
			if (!filter(file, filters))
			{
				if(file == null)
					return;
				
				else if (file.isDirectory())
					this.getFileNames(files, file, filters); 
				
				else if (file.isFile() && file.canRead() && isValidFile(file))
					files.add(file);
			}
		}
	}
	
	/**
	 * 
	 * @param dir
	 * @param filter
	 * @return
	 */
	private boolean filter(File file, List<String> filters)
	{
		boolean returnValue = false;
		if (filters != null)
		{ 
			for(String filter: filters)
			{
				if (file.getName().contains(filter))
				{
					returnValue = true;
					break;
				}
			}
		}
		
		return returnValue;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	private boolean isValidFile(File file)
	{
		boolean valid = false;
		if (validFileExtensions != null)
		{
			String extension = null;
			int index = file.getName().lastIndexOf(".");
			if (index > -1)
			{
				extension = file.getName().substring(index+1);
				if (extension != null && validFileExtensions.contains(extension))
					valid = true;
			}
		}
		return valid;
	}

	/**
	 * @return the validFileExtensions
	 */
	public String getValidFileExtensions() {
		return validFileExtensions;
	}

	/**
	 * @param validFileExtensions the validFileExtensions to set
	 */
	public void setValidFileExtensions(String validFileExtensions) {
		this.validFileExtensions = validFileExtensions;
	}

}