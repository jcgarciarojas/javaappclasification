package edu.umd.msswe.ece591.diagnose.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.umd.msswe.ece591.diagnose.exception.ReportException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
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

public class MetricsReport {
	private List<FuzzyEngineReport> metricReport;
	
	public MetricsReport()
	{
		metricReport = new ArrayList<FuzzyEngineReport>();
	}
	
	/**
	 * 
	 * @param classInfo
	 * @param metricsReport
	 */
	public void addMetricReport(JavaClassInfo classInfo, FuzzyValue fuzzySystemOutput, Map<String, MetricReport> metricsReport)
	{
		FuzzyEngineReport metricReport = new  FuzzyEngineReport();
		if (classInfo != null)
			metricReport.setClassname(classInfo.getClassName());
		metricReport.setMetricReport(metricsReport);
		metricReport.setFuzzySystemOutput(fuzzySystemOutput);
		this.metricReport.add(metricReport);
	}

	/**
	 * @return the metricReport
	 */
	public List<FuzzyEngineReport> getMetricReport() {
		return metricReport;
	}
	
	/**
	 * 
	 * @param metricOutputLocation
	 * @param type
	 * @throws ReportException
	 */
	public void saveReport(String metricOutputLocation, OutputType type)throws ReportException
	{
		if (metricOutputLocation != null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("<metric-report>");
			for (FuzzyEngineReport singleReport: metricReport)
			{
				sb.append(singleReport.toReport(type));
			}
			
			sb.append("</metric-report>");
			saveFile(metricOutputLocation, sb.toString());
		}
	}

	/**
	 * 
	 * @param output
	 * @param body
	 * @throws ReportException
	 */
	private static void saveFile(String output, String body) throws ReportException
	{
	    Writer out = null;
	    try {
	    	out = new OutputStreamWriter(new FileOutputStream(output));
	    	out.write(body+"\n");
	    } catch(FileNotFoundException fnf){
	    	throw new ReportException(fnf);
	    } catch(IOException ioe){
	    	throw new ReportException(ioe);
	    }
	    finally {
	    	try {if(out != null) out.close();} catch(IOException ioe) {return;}
	    }
		
	}	
}
