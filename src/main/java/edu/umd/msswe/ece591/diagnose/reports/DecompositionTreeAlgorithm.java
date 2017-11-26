package edu.umd.msswe.ece591.diagnose.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
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

public class DecompositionTreeAlgorithm 
{
	private static final SharpValue zero = SharpValue.valueOf("0");
	private Comparator cmp;

	private List<OOMetric> metrics;
	private TransitiveClosureFactory factory;
	
	public DecompositionTreeAlgorithm(List<OOMetric> metrics)
	{
		this.factory = new TransitiveClosureFactory();
		this.metrics = metrics;
		this.cmp = new SharpComprator();
	}

	/**
	 * 
	 * @param listFuzzyReport
	 * @throws DecompositionTreeException
	 */
	public DecompositionTreeReport execute(List<FuzzyEngineReport> listFuzzyReport, int maxMinTransClosureSize) throws DecompositionTreeException
	{
		List<String> classNames = this.getClassNames(listFuzzyReport);
		List<SharpValue[]> values = this.getSharpValuesMetrics(listFuzzyReport, maxMinTransClosureSize);
		
		FuzzyRelation r1 = factory.getRelationUsingHamming(classNames, classNames, values);
		
		FuzzyRelation result = null;
		if (values.size() < maxMinTransClosureSize)
			result = factory.getMinMaxTransitiveClosure(r1);
		else 
			result = r1;

		List<SharpValue> levels = this.getLevelsMinMaxTransitiveClosure(result);
		DecompositionTreeReport report = new DecompositionTreeReport(levels, result, maxMinTransClosureSize);
		report.setEngineReportList(listFuzzyReport);
		return report;
	}
	
	/**
	 * Get the levels of the min max transitive closure fuzzy relation
	 * @param r1, the Fuzzy Relation 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SharpValue> getLevelsMinMaxTransitiveClosure(FuzzyRelation r1)
	{
		List<SharpValue> l = new ArrayList<SharpValue>();
		
		for(int row=0;row<r1.getDimension()[0];row++)
		{
			for(int col=0;col<r1.getDimension()[1];col++)
			{
				SharpValue value = r1.getCellValue(row, col);
				if (!contains(l, value))
					l.add(value);
			}
		}
		
		Collections.sort(l, cmp);
		return l;
	}
	
	/**
	 * @param l
	 * @param value
	 * @return
	 */
	protected boolean contains(List<SharpValue> l, SharpValue value)
	{
		boolean isContained = false;
		for(SharpValue s: l)
		{
			if (s.compareTo(value) == 0)
			{
				isContained = true;
				break;
			}
		}
		return isContained;
	}
	
	/**
	 * 
	 * @param listStrings
	 * @return
	 */
	public List<SharpValue[]> getSharpValueArrayList(List<String> listStrings)
	{
		List<SharpValue[]> l = new ArrayList<SharpValue[]>();
		
		for (String str:listStrings)
		{
			int index = 0;
			StringTokenizer st = new StringTokenizer(str, ",");
			int max = st.countTokens();
			SharpValue[] ls = new SharpValue[max];
			while(st.hasMoreTokens())
			{
				String strValue = st.nextToken().trim();
				ls[index++] = SharpValue.valueOf(strValue);
			}
			
			l.add(ls);
		}
		
		return l;
	}
	
	/**
	 * 
	 * @param listFuzzyReport
	 * @return
	 */
	public List<String> getClassNames(List<FuzzyEngineReport> listFuzzyReport)
	{
		List<String> l = new ArrayList<String>();

		for(FuzzyEngineReport fuzzyReport: listFuzzyReport)
		{
			if (fuzzyReport.getClassname() != null)
				l.add(fuzzyReport.getClassname());
		}
		
		return l;
	}
	
	/**
	 * get a list of arrays of sharp values for the metrics from the list of fuzzy engine report 
	 * @param listFuzzyReport
	 * @param listFuzzyReport
	 * @return
	 */
	public List<SharpValue[]> getSharpValuesMetrics(List<FuzzyEngineReport> listFuzzyReport, int maxMinTransClosureSize)
	{
		List<SharpValue[]> values = new ArrayList<SharpValue[]>();

		//If the list of reports is too large then the performance will decrease to create the similarity tree
		//so all the values are set to zero and the tree will have only one level
		boolean setNoneZeroValue = true;
		if(listFuzzyReport.size() >= maxMinTransClosureSize)
			setNoneZeroValue = false;
		
		for (FuzzyEngineReport report: listFuzzyReport)
		{
			if (report.getClassname() != null)
				values.add(getMetricsValues(report, setNoneZeroValue));
		}
		
		return values;
	}
	
	/**
	 * get an arrays of values from the engine report, if there is a metrics with no matching value
	 * in the report then a zero value is assigned for that particular metric
	 * @param report
	 * @return
	 */
	protected SharpValue[] getMetricsValues(FuzzyEngineReport report, boolean setNoneZeroValue)
	{ 
		List<SharpValue> l = new ArrayList<SharpValue>();
		for (OOMetric metric: metrics)
		{
			MetricReport detail = report.getMetricReport(metric.getId());
			if (detail != null && setNoneZeroValue)
				l.add(new SharpValue(detail.getValue()));
			else
				l.add(zero.cloneObject());

		}
		return l.toArray(new SharpValue[0]);
	}

	/**
	 * 
	 * @author juan.garcia
	 *
	 */
	private static class SharpComprator implements  Comparator
	{
		public int compare(Object o1, Object o2)
		{
			SharpValue s1 = (SharpValue)o1;
			SharpValue s2 = (SharpValue)o2;
			//return s1.compareTo(s2);
			return s2.compareTo(s1);
		}
	}
}