package edu.umd.msswe.ece591.diagnose.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class FuzzyEngineReport extends Report {

	private String classname;
	private String classification;
	private FuzzyValue fuzzySystemOutput;
	private Map<String, MetricReport> metricReport;
	
	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}
	/**
	 * @param classname the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
	/**
	 * @return the fuzzySystemOutput
	 */
	public FuzzyValue getFuzzySystemOutput() {
		return fuzzySystemOutput;
	}
	/**
	 * @param fuzzySystemOutput the fuzzySystemOutput to set
	 */
	public void setFuzzySystemOutput(FuzzyValue fuzzySystemOutput) {
		this.fuzzySystemOutput = fuzzySystemOutput;
	}
	/**
	 * @return the metricReport
	 */
	public Map<String, MetricReport> getMetricReport() {
		return metricReport;
	}
	/**
	 * @param metricReport the metricReport to set
	 */
	public void setMetricReport(Map<String, MetricReport> metricReport) {
		this.metricReport = metricReport;
	}
	/**
	 * @return the metricReport
	 */
	public MetricReport getMetricReport(String key) {
		return metricReport.get(key);
	}
	
	/**
	 * 
	 */
	public String toXml() 
	{
		StringBuffer sb = new StringBuffer(); 
		
		if (classification != null)
			sb.append("<classification>" + classification + "</classification>" + NEW_LINE);

		//if (fuzzySystemOutput.getSharpValue() != null)
		//	sb.append("<total-value>" + fuzzySystemOutput.getSharpValue() + "</total-value>" + NEW_LINE);
		
		if (fuzzySystemOutput.getDegree() != null)
			sb.append("<degree-value>" + fuzzySystemOutput.getDegree() + "</degree-value>" + NEW_LINE);
		if (metricReport.size() > 0)
		{
			sb.append("<warnings>" + metricReport.size() + "</warnings>" + NEW_LINE);
	
			sb.append("<metrics-report>" + NEW_LINE);
			
			Iterator it = getSortedKeyIterator(metricReport);
			while(it.hasNext())
			{
				String key = (String)it.next();
				sb.append("<metric id='" + key + "'>" + NEW_LINE);
				sb.append(metricReport.get(key).toXml());
				sb.append("</metric>" + NEW_LINE);
			}
			sb.append("</metrics-report>" + NEW_LINE);
		}
		
		if (classname == null)
			return "<application-report>" + NEW_LINE+ sb.toString() + "</application-report>" + NEW_LINE;
		else
			return "<class-report class-name='" + classname + "'>" + NEW_LINE+ sb.toString() + "</class-report>" + NEW_LINE;

	}

	/**
	 * 
	 * @param metricReport
	 * @return
	 */
	protected Iterator getSortedKeyIterator(Map<String, MetricReport> metricReport)
	{
		ArrayList<String> s = new ArrayList<String>();
		s.addAll(metricReport.keySet());
		Collections.sort(s);

		return s.iterator();		
	}
	
	/**
	 * 
	 */
	public String toHtml()
	{
		return this.toXml();
	}
	/**
	 * 
	 */
	public String toText()
	{
		StringBuffer sb = new StringBuffer();
		if (classname != null)
			sb.append("Class Name: "+classname+NEW_LINE+NEW_LINE);

		if (classification != null)
			sb.append("Classification: "+classification+COMMA);
		
		sb.append(" Total: "+fuzzySystemOutput.getSharpValue()+COMMA+
				" degree of belonging "+fuzzySystemOutput.getDegree()+NEW_LINE);
		sb.append(NEW_LINE);
		
		Iterator it = getSortedKeyIterator(metricReport);
		
		while(it.hasNext())
		{
			String key = (String)it.next();
			sb.append(key+DASH);
			sb.append(metricReport.get(key).toText()+NEW_LINE);
		}
		return sb.toString()+NEW_LINE;	
	}
	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}
	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String toString()
	{
		return classname + " : " + classification + " : " + fuzzySystemOutput;
		
	}
}
