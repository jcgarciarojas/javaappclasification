package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.util.Calendar;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.umd.msswe.ece591.diagnose.configuration.ConfigurationManager;
import edu.umd.msswe.ece591.diagnose.configuration.SystemConfiguration;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.exception.ReportException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.reports.DecompositionTreeAlgorithm;
import edu.umd.msswe.ece591.diagnose.reports.DecompositionTreeReport;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyReport;
import edu.umd.msswe.ece591.diagnose.reports.ReportManager;
import edu.umd.msswe.ece591.diagnose.reports.Report.OutputType;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class DiagnoseFacade {

	private GUIDiagnoseBean subjectBean;
	private Engine fuzzyEngine;
	private ConfigurationManager confManager;
	private JavaApplication javaApplication;
	private SystemConfiguration sysConfig;
	private ReportManager reportManager;
	private DefaultMutableTreeFactory factory;
	
	/**
	 * 
	 * @param confManager
	 */
	public DiagnoseFacade(ConfigurationManager confManager)
	{
		this.confManager = confManager;
		this.logCommand("initializing system");
	}

	/**
	 * 
	 * @param subjectBean
	 */
	public void setGUIDiagnoseBean(GUIDiagnoseBean subjectBean)
	{
		this.subjectBean = subjectBean;
	}
	
	/**
	 * 
	 * @throws ConfigurationException
	 * @throws FuzzySystemException
	 */
	public void diagnose() throws ConfigurationException, FuzzySystemException, DecompositionTreeException
	{
		if(sysConfig == null)
			throw new FuzzySystemException("System configuration has not been loaded execute DiagnoseFacade.loadConfiguration() first");

		this.createFuzzyEngine();
		
		this.logCommand("diagnosing system - executing oo metrics and fuzzy rules");
		List<FuzzyEngineReport> engineReportList = 
			this.fuzzyEngine.execute(this.sysConfig.getFuzzyRules(), this.sysConfig.getFuzzyRulesClass());
		
		javaApplication.setTitle(subjectBean.projectName);
		FuzzyReport fuzzyReport = new FuzzyReport(sysConfig.getFullSetOoMetrics(), engineReportList, javaApplication, sysConfig.getFuzzyOutput());
		
		this.logCommand("diagnosing system - generating similarity tree report");
		DecompositionTreeReport treeReport = new DecompositionTreeAlgorithm(sysConfig.getOoMetricsForClasses()).execute(engineReportList, 200);
		reportManager = new ReportManager( fuzzyReport,treeReport, this.fuzzyEngine.getMetricReport());
			
	}

	/**
	 * 
	 * @throws ConfigurationException
	 */
	public void loadCodeStructure() throws ConfigurationException
	{
		
		this.logCommand("loading application structure and extracting data from classes");
		this.javaApplication = (JavaApplication)confManager.getStructure(this.getStructureParameters());
	}
	
	/**
	 * 
	 * @throws ConfigurationException
	 */
	public void deleteWorkingDirectory() throws ConfigurationException
	{
		return;
	}

	public String[] getStructureParameters()
	{
		String[] params = new String[4];
		params[0] = subjectBean.sourceDirectories;
		params[1] = subjectBean.filter;
		params[2] = subjectBean.workingDirectory;
		params[3] = subjectBean.additionalClassPath;
		
		return params;
	}

	/**
	 * 
	 * @throws ConfigurationException
	 */
	public void loadConfiguration() throws ConfigurationException
	{
		this.logCommand("loading configuration from XML file");
		this.sysConfig = (SystemConfiguration)confManager.getConfig();
	}
	
	/**
	 * 
	 * @throws FuzzySystemException
	 */
	protected void createFuzzyEngine() throws FuzzySystemException
	{
		this.logCommand("creating fuzzy engine");
		if (sysConfig == null)
			throw new FuzzySystemException("System configuration has not been loaded execute DiagnoseFacade.loadConfiguration() first");

		if (javaApplication == null)
			throw new FuzzySystemException("application has not been loaded execute DiagnoseFacade.loadCodeStructure() first");

		try {
			fuzzyEngine = this.sysConfig.getEngine(javaApplication, this.subjectBean.metricOutputLocation);
		} catch (ConfigurationException e) {
			throw new FuzzySystemException(e);
		}
		
	}
	
	/**
	 * 
	 * @return
	 * @throws FuzzySystemException
	 * @throws DecompositionTreeException
	 */
	public DefaultMutableTreeNode createTreeReport() throws FuzzySystemException, DecompositionTreeException 
	{
		this.logCommand("creating similatiry tree report");
		if (factory == null)
			factory = new DefaultMutableTreeFactory(reportManager.getDecompositionTreeReport());

		return factory.generateTree();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public String getMetricsSummary()
	{
		this.logCommand("creating metric summary report");
		return reportManager.getMetricsSummary();
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public String getSummaryReport()
	{
		this.logCommand("creating fuzzy summary report");
		return reportManager.getSummaryReport();
	}
	
	/**
	 * 
	 * @param subjectBean
	 * @throws ReportException
	 */
	public void saveReport(OutputType type) throws ReportException
	{
		this.logCommand("saving fuzzy report");
		reportManager.saveReport(subjectBean, type);
	}

	/**
	 * gets the similarity groups metrics report to be displayed on the screen 
	 * @param type
	 * @param className
	 * @return
	 */
	public String getFuzzyReport(TreeNodeBean bean)
	{
		return reportManager.getDetailFuzzyReport(bean.getId(), bean.getListOfClasses(), 
				bean.getListOfSimilarityGroups());
	}
	
	protected void logCommand(String message)
	{
		System.out.println(Calendar.getInstance().getTime()+": "+message+".....");
		
	}

	/**
	 * @return the reportManager
	 */
	public ReportManager getReportManager() {
		return reportManager;
	}

}