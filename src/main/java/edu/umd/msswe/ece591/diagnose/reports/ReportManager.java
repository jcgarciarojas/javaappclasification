package edu.umd.msswe.ece591.diagnose.reports;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.exception.ReportException;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.GUIDiagnoseBean;
import edu.umd.msswe.ece591.diagnose.reports.Report.OutputType;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class ReportManager {

	private FuzzyReport report;
	private DecompositionTreeReport treeReport;
	private MetricsReport metricsReport ;
	
	/**
	 * 
	 * @param report
	 * @param decompTree
	 */
	public ReportManager(FuzzyReport report, DecompositionTreeReport treeReport, MetricsReport metricsReport) 
		throws DecompositionTreeException, ConfigurationException
	{
		this.report = report;
		this.treeReport = treeReport;
		this.metricsReport = metricsReport;
		report.setTreeReport(treeReport);
				
	}
	
	/**
	 * 
	 * @param engineReportList
	 * @return
	 * @throws DecompositionTreeException
	 */
	public DecompositionTreeReport getDecompositionTreeReport()
	throws DecompositionTreeException
	{
		return treeReport;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMetricsSummary()
	{
		return report.getScreenMetricsSummary();
	}	

	/**
	 * 
	 * @return
	 */
	public String getSummaryReport()
	{
		return report.getScreenSummaryReport();
	}

	/**
	 * 
	 * @param bean
	 * @throws ReportException
	 */
	public void saveReport(GUIDiagnoseBean bean, OutputType type) throws ReportException
	{
		report.saveFile(bean.outputLocation, report.toReport(type));
		metricsReport.saveReport(bean.metricOutputLocation, type);
	}

	/**
	 * 
	 * @param isLeaf
	 * @param classesNames
	 * @return
	 */
	public String getDetailFuzzyReport(String className, List<String> listOfClasses, List<List> listOfSimilarityGroups)
	{
		if (listOfSimilarityGroups != null)
			return report.getScreenSimilarityFuzzyReport(listOfSimilarityGroups);
		
		else if (listOfClasses != null)
			return report.getScreenClassListFuzzyReport(listOfClasses);
		
		else
			return report.getScreenDetailFuzzyReport(className);
	}	

}
