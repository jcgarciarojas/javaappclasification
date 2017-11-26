package edu.umd.msswe.ece591.diagnose.reports;

import static edu.umd.msswe.ece591.diagnose.reports.Report.OutputType.TO_TEXT;
import static edu.umd.msswe.ece591.diagnose.reports.Report.OutputType.TO_XML;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.ReportException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.FuzzyOutput;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class FuzzyReport extends Report {

	private static final String UNKNOWN = "UNKNOWN";
	private List<FuzzyOutput> fuzzyOutput;
	private List<OOMetric> ooMetrics;
	private List<FuzzyEngineReport> engineReportList;
	private JavaApplication javaApplication;
	private DecompositionTreeReport treeReport;
    private ComparatorByOutput compByOutput;
    private ComparatorByClassName compByName;
	
    /**
     * 
     * @param ooMetrics
     * @param engineReportList
     * @param javaApplication
     */
	public FuzzyReport(List<OOMetric> ooMetrics, List<FuzzyEngineReport> engineReportList, 
			JavaApplication javaApplication, List<FuzzyOutput> fuzzyOutput)
	{
		this.fuzzyOutput = fuzzyOutput;
		this.ooMetrics = ooMetrics;
		this.engineReportList = engineReportList;
		this.javaApplication = javaApplication;
    	this.compByOutput = new ComparatorByOutput();
    	this.compByName = new ComparatorByClassName();

    	this .setFuzzyEngineReportClassification();
	}
	
	/**
	 * 
	 */
	public String toXml()
	{
		OutputType type = TO_XML;
		StringBuffer sb = new StringBuffer();
		sb.append("<fuzzy-report>"+Report.NEW_LINE);

		sb.append("<summary-report>"+Report.NEW_LINE);
		
		sb.append(javaApplication.toXml());
		sb.append("<classes-with-warnings>" + (engineReportList.size()-1) + "</classes-with-warnings>" + Report.NEW_LINE);
		for (FuzzyOutput output: fuzzyOutput)
		{
			int total = this.getTotalNumberOfWarnings(output.getLabel());
			if (total > 0)
				sb.append("<total-"+output.getLabel().toLowerCase()+"-warnings>" + total + "</total-"+output.getLabel().toLowerCase()+"-warnings>" + Report.NEW_LINE);
		}
		sb.append("</summary-report>"+Report.NEW_LINE);
		sb.append(getSummaryFuzzyReport(type));
		
		if (treeReport != null)
		{
			sb.append("<defuzzification-tree similarity-levels='"+treeReport.getLevelValues().size()+"'>");
			sb.append(treeReport.toXml());
			sb.append("</defuzzification-tree>");
		}
		
		sb.append("<classes-report number-of-classes='"+(engineReportList.size()-1)+"' >"+Report.NEW_LINE);
		sb.append(getDetailFuzzyReport(type));
		sb.append("</classes-report>"+Report.NEW_LINE);
		
		sb.append("<metrics-definition>"+Report.NEW_LINE);
		sb.append(this.getMetricsDef(type));
		sb.append("</metrics-definition>"+Report.NEW_LINE);

		sb.append("</fuzzy-report>"+Report.NEW_LINE);
		return sb.toString();
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
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getScreenMetricsSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("METRICS"+Report.NEW_LINE);
		sb.append(getMetricsDef(TO_TEXT));
		return sb.toString();		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getScreenSummaryReport()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SUMMARY REPORT"+Report.NEW_LINE);
		
		if(javaApplication.getTitle() != null)
			sb.append("Name of the application: "+javaApplication.getTitle()+ Report.NEW_LINE);
		
		sb.append(javaApplication.toReport(TO_TEXT));
		for (FuzzyOutput output: fuzzyOutput)
		{
			int total = this.getTotalNumberOfWarnings(output.getLabel());
			if (total > 0)
				sb.append("Number of "+output.getLabel().toLowerCase()+" warnings found: "+this.getTotalNumberOfWarnings(output.getLabel())+Report.NEW_LINE);
		}
		sb.append( getSummaryFuzzyReport(TO_TEXT));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param listOfSimilarityGroups
	 * @return
	 */
	public String getScreenSimilarityFuzzyReport(List<List> listOfSimilarityGroups)
	{
		StringBuffer sb1 = new StringBuffer(); 
		sb1.append("LEVEL SUMMARY:"+NEW_LINE);
		sb1.append("Similarity Groups classification: " + 
				this.getLabelClassification(this.getSimilarityGroupsClassification(listOfSimilarityGroups)) +NEW_LINE);
		sb1.append("Number of Similarity Groups: " + listOfSimilarityGroups.size() +NEW_LINE);
		
		int length=0;
		for (List l :listOfSimilarityGroups)
			length+= l.size();
		
		sb1.append("Number of Classes: " + length +NEW_LINE);
		return sb1.toString();
	}
	
	/**
	 * 
	 * @param listOfClasses
	 * @return
	 */
	public String getScreenClassListFuzzyReport(List<String> listOfClasses)
	{
		StringBuffer sb1 = new StringBuffer(); 
		sb1.append("LEVEL SUMMARY:"+NEW_LINE);
		sb1.append("Group classification: " + 
				this.getLabelClassification(this.getGroupClassification(listOfClasses)) +NEW_LINE);
		sb1.append("Number of Classes: " + listOfClasses.size() +NEW_LINE);
		sb1.append("Classes: " + listOfClasses.toString().replace("[","").replace("]", "") +NEW_LINE);
		return sb1.toString();
	}
	
	/**
	 * set classification for FuzzyEngineReport list
	 *
	 */
	private void setFuzzyEngineReportClassification()
	{
		for (FuzzyEngineReport report: engineReportList)
			report.setClassification(getLabelClassification(report.getFuzzySystemOutput().getSharpValue()));
	}

	/**
	 * 
	 * @param output
	 * @return
	 */
	private String getLabelClassification(SharpValue output)
	{
		String classification = UNKNOWN;
		if (output != null)
		{
			for (FuzzyOutput o: fuzzyOutput)
				if (o.isInBetween(output))
					classification = o.getLabel();
		}
		
		return classification;
	}
	
	/**
	 * 
	 * @param listOfClasses
	 * @return
	 */
	private SharpValue getGroupClassification(List<String> listOfClasses)
	{
		SharpValue output = null;
		for (String classname : listOfClasses)
		{
			for (FuzzyEngineReport report: engineReportList)
			{
				if (!classname.equals(report.getClassname()))
					continue;
				
				if (output == null)
					output = report.getFuzzySystemOutput().getSharpValue();
				else if (report.getFuzzySystemOutput().getSharpValue().compareTo(output) > 0)
					output = report.getFuzzySystemOutput().getSharpValue();
					
					
			}
		}
		return output;
	}

	/**
	 * 
	 * @param listOfSimilarityGroups
	 * @return
	 */
	private SharpValue getSimilarityGroupsClassification(List<List> listOfSimilarityGroups)
	{
		SharpValue output = null;
		for (List<String> listOfClasses: listOfSimilarityGroups)
		{
			SharpValue newOutput = getGroupClassification(listOfClasses);
			if (output == null)
				output = newOutput;
			else if (newOutput.compareTo(output) > 0)
				output = newOutput;
		}
			
		return output;
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public String getScreenDetailFuzzyReport(String className)
	{
		return getDetailFuzzyReport(TO_TEXT, className);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMetricsDef(OutputType type)
	{
		StringBuffer sb = new StringBuffer();

		MetricReport.sort(ooMetrics);
		for(OOMetric m : ooMetrics)
			sb.append(m.toReport(type) + Report.NEW_LINE);

		return sb.toString();
	}
	
	/**
	 * 
	 * @param type
	 * @param className
	 * @return
	 */
	public String getSummaryFuzzyReport(OutputType type)
	{
		
		StringBuffer sb = new StringBuffer();
		for (FuzzyEngineReport engineReport: engineReportList)
		{
			if (engineReport.getClassname() == null)
			{
				sb.append(engineReport.toReport(type));
				break;
			}
		}
		return sb.toString();	
	}
	
	/**
	 * 
	 * @param type
	 * @param className
	 * @return
	 */
	public String getDetailFuzzyReport(OutputType type)
	{
		
		StringBuffer sb = new StringBuffer();
		for (FuzzyEngineReport engineReport: engineReportList)
		{
			if (engineReport.getClassname() != null)
				sb.append(engineReport.toReport(type));
		}
		return sb.toString();	
	}

	/**
	 * 
	 * @param type
	 * @param className
	 * @return
	 */
	public String getDetailFuzzyReport(OutputType type, String className)
	{
		
		StringBuffer sb = new StringBuffer();
		for (FuzzyEngineReport engineReport: engineReportList)
		{
			if (engineReport.getClassname() != null && engineReport.getClassname().equals(className))
			{
				sb.append(engineReport.toReport(type));
				break;
			}
		}
		return sb.toString();	
	}
	
	/**
	 * 
	 * @return
	 */
	protected int getTotalNumberOfWarnings(String label)
	{
		int total = 0;
		for (FuzzyEngineReport report : engineReportList)
		{
			if (report.getClassname() != null && report.getClassification().equalsIgnoreCase(label))
				total++;
		}
		return total;
		
	}
	
	/**
	 * 
	 * @param output
	 * @param body
	 * @throws ReportException
	 */
	public void saveFile(String output, String body) throws ReportException
	{
	    Writer out = null;
	    try {
	    	out = new OutputStreamWriter(new FileOutputStream(output));
	    	out.write(body);
	    } catch(FileNotFoundException fnf){
	    	throw new ReportException(fnf);
	    } catch(IOException ioe){
	    	throw new ReportException(ioe);
	    }
	    finally {
	    	try {if(out != null) out.close();} catch(IOException ioe) {return;}
	    }
		
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FuzzyEngineReport> getSortEngineReportListByClassname()
	{
		List<FuzzyEngineReport> l = new ArrayList<FuzzyEngineReport>(this.engineReportList.size());
		for(FuzzyEngineReport report: this.engineReportList)
			l.add(report);
    	Collections.sort(l, this.compByName);
    	return l;
	}
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FuzzyEngineReport> getSortEngineReportListByOutput()
	{
		List<FuzzyEngineReport> l = new ArrayList<FuzzyEngineReport>(this.engineReportList.size());
		for(FuzzyEngineReport report: this.engineReportList)
			l.add(report);
    	Collections.sort(l, this.compByOutput);
    	Collections.reverse(l);
    	return l;
	}
	
	/**
	 * @param treeReport the treeReport to set
	 */
	public void setTreeReport(DecompositionTreeReport treeReport) {
		this.treeReport = treeReport;
	}
	
	/**
	 * This helper private class sorts the FuzzyEngineReport elements
	 * @author juan.garcia
	 *
	 */
	private static class ComparatorByOutput implements  Comparator
	{
		public int compare(Object o1, Object o2)
		{
			FuzzyEngineReport r1 = (FuzzyEngineReport)o1;
			FuzzyEngineReport r2 = (FuzzyEngineReport)o2;
			
			//if the sharp/crisp value of the fuzzy set are different then return it
			return r1.getFuzzySystemOutput().toCompare(r2.getFuzzySystemOutput());
		}
	}

	public static class ComparatorByClassName implements  Comparator
	{
		public int compare(Object o1, Object o2)
		{
			FuzzyEngineReport r1 = (FuzzyEngineReport)o1;
			FuzzyEngineReport r2 = (FuzzyEngineReport)o2;
			if(r1.getClassname() == null || r2.getClassname() == null)
				return -1;
			else
				return r1.getClassname().compareTo(r2.getClassname());
		}
	}

	public static class ComparatorByClassification implements  Comparator
	{
		public int compare(Object o1, Object o2)
		{
			FuzzyEngineReport r1 = (FuzzyEngineReport)o1;
			FuzzyEngineReport r2 = (FuzzyEngineReport)o2;
			if(r1.getClassification() == null || r2.getClassification() == null)
				return -1;
			else
				return r1.getClassification().compareTo(r2.getClassification());
		}
	}
	

}
