package edu.umd.msswe.ece591.diagnose.reports;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class DecompositionTreeReport extends Report {

	private List<SharpValue> levelValues;
	private FuzzyRelation fuzzyRelation;
	private List<FuzzyEngineReport> engineReportList;
	private int maxMinTransClosureSize;
	
	public DecompositionTreeReport(List<SharpValue> levelValues, FuzzyRelation fuzzyRelation, int maxMinTransClosureSize)
	{
		this.levelValues = levelValues;
		this.fuzzyRelation = fuzzyRelation;
		this.maxMinTransClosureSize = maxMinTransClosureSize;
	}

	/**
	 * @param engineReportList the engineReportList to set
	 */
	public void setEngineReportList(List<FuzzyEngineReport> engineReportList) {
		this.engineReportList = engineReportList;
	}

	/**
	 * @return the levelValues
	 */
	public List<SharpValue> getLevelValues() {
		return levelValues;
	}
	
	/**
	 * Get a list of classes of the form [[list of classes 1], [list of classes 2],..,[list of classes N]] for a particular level/sharp value 
	 * @param value
	 * @return
	 */
	public List<List> getClasses(SharpValue value)
	{
		List<List> results = new ArrayList<List>();
		for(int row=0;row<fuzzyRelation.getDimension()[0];row++)
		{
			List<String> cluster = new ArrayList<String>();
			for(int col=0;col<fuzzyRelation.getDimension()[1];col++)
			{
				SharpValue v = fuzzyRelation.getCellValue(row, col);
				String className = fuzzyRelation.getTitle(col);
				
				if (v.compareTo(value) <= 0)
					cluster.add(className);
			}
			if (!results.contains(cluster))
				results.add(cluster);
		}

		return results;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(levelValues+"\n");
		
		for (SharpValue value : this.getLevelValues())
			sb.append(value +" = "+ this.getClasses(value)+"\n");
		return sb.toString();
	}
	
	public String toXml()
	{
		StringBuffer sb = new StringBuffer();
		for(SharpValue value: levelValues)
		{
			sb.append("<similarity-level value='"+value+"'>"+NEW_LINE);
			List<List> list = this.getClasses(value);
			sb.append("<number-of-groups>"+list.size()+"</number-of-groups>"+NEW_LINE);
			for(List<String> listOfGroups: list)
			{
				sb.append("<group>"+NEW_LINE);
				if (listOfGroups.size() >= this.maxMinTransClosureSize)
					sb.append("<warning>similarity is not being calculated due to the number of classes exceeds the max allowed "+
							maxMinTransClosureSize+"  </warning>"+NEW_LINE);
				
				sb.append("<classification>"+this.getClassification(listOfGroups)+"</classification>"+NEW_LINE);
				sb.append("<number-of-classes>"+listOfGroups.size()+"</number-of-classes>"+NEW_LINE);
				sb.append("<classes>"+listOfGroups.toString().replace("[", "").replace("]", "")+"</classes>"+NEW_LINE);
				sb.append("</group>"+NEW_LINE);
			}
			sb.append("</similarity-level>"+NEW_LINE);
		}
		return sb.toString();
	}
	
	public String getClassification(List<String> listOfGroups)
	{
		String classification="UNKNOWN";
		if (engineReportList != null)
		{
			FuzzyValue fuzzy =FuzzyValue.getZeroFuzzyValue();
			for (FuzzyEngineReport report : engineReportList)
			{
				if (report.getClassname() != null && listOfGroups.contains(report.getClassname()))
				{
					if (report.getFuzzySystemOutput().toCompare(fuzzy) > 0)
					{
						fuzzy = report.getFuzzySystemOutput();
						classification = report.getClassification();
					}
				}
			}
		}
		return classification;
	}
	
	
	public String toHtml()
	{
		return toXml();
	}
	
	public String toText()
	{
		StringBuffer sb = new StringBuffer(); 
		return sb.toString();
	}

	/**
	 * @return the engineReportList
	 */
	public List<FuzzyEngineReport> getEngineReportList() {
		return engineReportList;
	}

}
