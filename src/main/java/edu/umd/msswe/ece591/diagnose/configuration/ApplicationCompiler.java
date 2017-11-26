package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

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
public class ApplicationCompiler {

	private JavaApplicationProcessor processor;
	
	/**
	 * 
	 * @param processor
	 * @param workingDirectory
	 */
	public ApplicationCompiler(JavaApplicationProcessor processor)
	{
		this.processor = processor;
	}
	
	/* (non-Javadoc)
	 * @see edu.umd.msswe.ece591.diagnose.fuzzy.System#process()
	 */
	public void process(List<File> javaClassesList, String workingDirectory, String classpath) throws ConfigurationException
	{
		try {
			this.processor.setWorkingDirectory(workingDirectory);
			//Get an instance of java compiler
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			
			if (compiler == null)
				throw new ConfigurationException("This program runs with jdk1.6 or later");

			//Get a new instance of the standard file manager implementation
			StandardJavaFileManager fileManager = compiler.
			        getStandardFileManager(null, null, null);
			        
			Iterable<? extends JavaFileObject> compilationUnits1 = 
			        fileManager.getJavaFileObjectsFromFiles(javaClassesList);
			
			List<String> optionList = new ArrayList<String>();
			optionList.addAll(Arrays.asList("-d",workingDirectory));
			
			if(classpath != null && classpath.length() > 0)
				optionList.addAll(Arrays.asList("-classpath",classpath));

			CompilationTask task = compiler.getTask(new OODiagnoseWriter(workingDirectory), fileManager, null,
					optionList, null, compilationUnits1);

			task.setProcessors(this.getProcessors());
			task.call();	
			fileManager.close();
		} 
		catch (IOException e) 
		{
			throw new ConfigurationException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private LinkedList<AbstractProcessor> getProcessors() throws ConfigurationException 
	{
		LinkedList<AbstractProcessor> list = new LinkedList<AbstractProcessor>();
		list.add(processor);
		return list;
	}
	
	public JavaApplication getJavaApplicationObject()
	{
		return processor.getJavaApplicationObject();
	}
	

}
