package edu.umd.msswe.ece591.diagnose.fuzzysystem;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

/**
	<property name="oodiagnose.home" location="c:/"/>
	<taskdef name="oodiagnose" 
		classname="edu.umd.msswe.ece591.diagnose.fuzzysystem.OODiagnoseAntTask"
		classpath="C:/oodiagnose.jar;C:/health/bie/OODiagnose/lib/mockito/mockito-all-1.8.5.jar;C:/Program Files/Java/jdk1.6.0_18/lib/tools.jar;C:/Program Files/Java/jdk1.6.0_18/jre/lib/rt.jar"/>

	<target name="oodiagnose" depends="init, compile">
	    <oodiagnose 
			mode="command"
			configurationFile="C:/health/bie/OODiagnose/config_system.xml"
			projectName="PortalWebApp"
			workingDirectory="C:/temp/test"
			sourceDirectory="C:/health/bie/OODiagnose/source/edu"
			outputFile="C:/temp/fuzzy_report.xml"
			metricOutputLocation="C:/temp/metric_report.csv"
	    	additionalClasspath="C:/health/bie/OODiagnose/lib/mockito/mockito-all-1.8.5.jar;C:/Program Files/IBM/SDP70Shared/plugins/org.junit_3.8.1/junit.jar;C:/Program Files/Java/jdk1.6.0_18/lib/tools.jar">
	    </oodiagnose>
	</target>

 */
public class OODiagnoseAntTask extends Task {
	
	private String mode;
	private String projectName;
	private String workingDirectory;
	private String sourceDirectory;
	private String outputFile;
	private String additionalClasspath;
	private String configurationFile;
	private String filter;
	private String metricOutputLocation;
	
	public void execute()
	{
		System.out.println("entered here ");

		System.out.println(mode+"\n"+
							configurationFile+"\n"+
							projectName+"\n"+
							workingDirectory+"\n"+
							sourceDirectory+"\n"+
							outputFile+"\n"+
							additionalClasspath+"\n"+
							metricOutputLocation+"\n"+
							filter
							);
		
		if (configurationFile == null) throw new BuildException("configurationFile is a required");
		if (workingDirectory == null) throw new BuildException("workingDirectory is required");
		if (sourceDirectory == null) throw new BuildException("sourceDirectory is required");
		if (outputFile == null) throw new BuildException("outputFile is required");
		
		GUIDiagnoseBean bean = new GUIDiagnoseBean(); 
		bean.configurationFile = configurationFile;
		bean.additionalClassPath = additionalClasspath;
		bean.outputLocation = outputFile;
		bean.projectName = projectName;
		bean.sourceDirectories = sourceDirectory;
		bean.workingDirectory = workingDirectory;
		bean.filter = filter;
		bean.mode = mode;
		bean.metricOutputLocation = metricOutputLocation;
		
		try{
			new OODiagnose(configurationFile).executeCommandVersion(bean);
		} catch(Exception ex)
		{
			throw new BuildException(ex);
		}
	}

	/**
	 * @param classpath the classpath to set
	 */
	public void setAdditionalClasspath(String additionalClasspath) {
		this.additionalClasspath = additionalClasspath;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param configurationFile the configurationFile to set
	 */
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	/**
	 * @param sourceDirectory the sourceDirectory to set
	 */
	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param metricOutputLocation the metricOutputLocation to set
	 */
	public void setMetricOutputLocation(String metricOutputLocation) {
		this.metricOutputLocation = metricOutputLocation;
	}
	
}
