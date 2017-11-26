package edu.umd.msswe.ece591.diagnose.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.reports.Report;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class MetricReport extends Report {
	
	private double value; 
	private String label; 
	private List<String> details;
	
	
	public MetricReport(){
		details = new ArrayList<String>();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getDetail()
	{
		return details;
	}

	/**
	 * 
	 * @param detail
	 */
	public void addDetails(String detail)
	{
		details.add(detail);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	public String toString()
	{
		return "Value ["+ value +"] Details ["+details+"]";
	}

	/**
	 * @param details the details to set
	 */
	public void setDetail(List<String> details) {
		this.details = details;
	}

	/**
	 * 
	 */
	public String toXml()
	{
		StringBuffer sb = new StringBuffer();
		if (label != null)
			sb.append("<metric-class>" + label + "</metric-class>" + NEW_LINE);
		sb.append("<total>" + value + "</total>" + NEW_LINE);
		
		if (details.size() > 0)
		{
			sb.append("<details><![CDATA[" + NEW_LINE);
			
			for(int i=0;i< details.size();i++)
			{
				if (i == (details.size()-1))
					sb.append(details.get(i));
				else
					sb.append(details.get(i) + COMMA + SPACE);
			}
			sb.append("]]></details>" + NEW_LINE);
		}
		
		return sb.toString()+NEW_LINE;	
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
		
		if (label != null)
			sb.append("Metric classification : " + label +". ");
		sb.append("Metric value : " + value);
		if (details.size() > 0)
			sb.append(". Details : ");
		
		for(int i=0;i< details.size();i++)
		{
			if (i == (details.size()-1))
				sb.append(details.get(i));
			else
				sb.append(details.get(i) + COMMA + SPACE);
		}
		
		return sb.toString()+NEW_LINE;	
	}
	
	@SuppressWarnings("unchecked")
	public static void sort(List<OOMetric> ooMetrics)
	{
		Collections.sort(ooMetrics, new ComparatorOOMetricByMetric());		
	}
	
	public static class ComparatorOOMetricByMetric implements  Comparator
	{
		public int compare(Object o1, Object o2)
		{
			OOMetric m1 = (OOMetric)o1;
			OOMetric m2 = (OOMetric)o2;
			if(m1.getId() == null || m2.getId() == null)
				return -1;
			else
				return m1.getId().compareTo(m2.getId());
		}
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}