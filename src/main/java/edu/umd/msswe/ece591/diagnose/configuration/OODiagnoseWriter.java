package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class OODiagnoseWriter extends Writer {
	
	private FileWriter fstream;
	
	/**
	 * 
	 * @param workingDirectory
	 * @throws IOException
	 */
	public OODiagnoseWriter(String workingDirectory) throws IOException
	{
		fstream = new FileWriter(workingDirectory+"/compile.log");
	}
	
	/**
	 * 
	 */
	public void write(char cbuf[], int off, int len) throws IOException
	{
		fstream.write(cbuf, off, len);
	}
	
	/**
	 * 
	 */
	public void flush() throws IOException
	{
		fstream.flush();
	}
	/**
	 * 
	 */
	public void close() throws IOException
	{
		fstream.close();
	}
}
