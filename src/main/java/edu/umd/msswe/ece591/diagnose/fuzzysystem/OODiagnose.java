package edu.umd.msswe.ece591.diagnose.fuzzysystem;


import java.io.File;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.ConfigBuilder;
import edu.umd.msswe.ece591.diagnose.configuration.ConfigurationManager;
import edu.umd.msswe.ece591.diagnose.configuration.JavaApplicationProcessor;
import edu.umd.msswe.ece591.diagnose.configuration.StructureBuilder;
import edu.umd.msswe.ece591.diagnose.configuration.StructureReader;
import edu.umd.msswe.ece591.diagnose.configuration.XmlReader;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.reports.Report.OutputType;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class OODiagnose {
	
	private DiagnoseFacade facade = null;
	
	public OODiagnose(String configurationFile)
	{
		initDiagnoseFacade(configurationFile);
	}
	
	/**
	 * 
	 *
	 */
	public void executeWindowVersion(GUIDiagnoseBean  bean)
	{
		final GUIDiagnoseBean  finalBean = bean;
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	OODiagnoseSubjectGUIImpl gui = new OODiagnoseSubjectGUIImpl();
            	gui.setControlObject(new ControlOODiagnoseImpl(gui, facade));
            	gui.createWindow();
            	gui.setGUIDiagnoseBean(finalBean);
            }
        });
		
	}

	/**
	 * 
	 * @param subjectBean
	 * @throws Exception
	 */
	public void executeCommandVersion(GUIDiagnoseBean subjectBean) throws Exception
	{
		ControlOODiagnoseImpl cmd = new ControlOODiagnoseImpl(null, facade);
		cmd.setGUIDiagnoseBean(subjectBean);
		cmd.execute();
		cmd.saveReport(OutputType.TO_XML);
	}

	/**
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	public void initDiagnoseFacade(String configurationFile) throws RuntimeException
	{
    	try{
    		File xmlFile = new File(configurationFile);
    		if(!xmlFile.exists())
    			throw new ConfigurationException("Configuration File does not exists config_system.xml"); 
    		
    		ConfigurationManager confManager = new ConfigurationManager(new StructureBuilder(new ApplicationCompiler(new JavaApplicationProcessor()), 
	    			new StructureReader()), new ConfigBuilder(new XmlReader(xmlFile)));
    		facade = new DiagnoseFacade(confManager);
    	} catch(ConfigurationException cef)
    	{
    		cef.printStackTrace();
    		throw new RuntimeException(cef);
    	}
		
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception
	{
		if (args == null || args.length <1)
		{
			printUsage();
			return;
		}
		
		GUIDiagnoseBean  bean = parseParameters(args);
		//validating options 
		if(bean.mode == null || bean.outputLocation == null || 
				bean.projectName == null || bean.sourceDirectories == null || 
				bean.workingDirectory == null || bean.configurationFile == null)
		{
			printUsage();
			return;
		}

		if(bean.mode.equalsIgnoreCase("gui"))
			new OODiagnose(bean.configurationFile).executeWindowVersion(bean);
		
		else
			new OODiagnose(bean.configurationFile).executeCommandVersion(bean);
		
		System.out.println("finished sucessfully!!!");
	}

	/**
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private static GUIDiagnoseBean parseParameters(String args[]) throws Exception
	{
		GUIDiagnoseBean subjectBean = new GUIDiagnoseBean(); 
		for(int i=0; i< args.length;i++)
		{
			if (args[i].startsWith("-mode="))
				subjectBean.mode = getArgsValue(args[i]);
			else if (args[i].startsWith("-configurationFile="))
				subjectBean.configurationFile = getArgsValue(args[i]);
			else if (args[i].startsWith("-projectName="))
				subjectBean.projectName = getArgsValue(args[i]);
			else if (args[i].startsWith("-workingDirectory="))
				subjectBean.workingDirectory = getArgsValue(args[i]);
			else if (args[i].startsWith("-sourceDirectory="))
				subjectBean.sourceDirectories = getArgsValue(args[i]);
			else if (args[i].startsWith("-outputFile="))
				subjectBean.outputLocation = getArgsValue(args[i]);
			else if (args[i].startsWith("-additionalClasspath="))
				subjectBean.additionalClassPath = getArgsValue(args[i]);
			else if (args[i].startsWith("-filter="))
				subjectBean.filter = getArgsValue(args[i]);
			else if (args[i].startsWith("-metricOutputLocation="))
				subjectBean.metricOutputLocation = getArgsValue(args[i]);
			else
				throw new Exception("Incorrect parameter "+args[i]); 
		}

	    return subjectBean;
	}
	private static String getArgsValue(String args) throws Exception
	{
		StringTokenizer st = new StringTokenizer(args, "=");
		if (st.countTokens() != 2)
			throw new Exception("Incorrect parameter "+args);
		
		//first token is the key
		st.nextToken();
		//next token is the value
		String value = st.nextToken(); 
		return value;
	}
		
	/**
	 * 
	 *
	 */
	private static void printUsage()
	{
		System.err.println(
			"Usage with_arguments for commandline: OODiagnose " +
			"[{-mode}=mode(gui|command)] "+
			"[{-sourceDirectory}=comma_separated_source_directories] "+
			"[{-workingDirectory}=working_directory] "+
			"[{-outputFile}=output_file_full_path] "+
			"[{-projectName}=project_name] "+
			"[{-filter}=comma_separated_filters] "+
			"[{-additionalClasspath}=additional_class_path] "+
			"[{-configurationFile}=configuration_file] "
			);
		
	}

	
}
