package diagnose.reports;

import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;
import edu.umd.msswe.ece591.diagnose.reports.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class JUnitTestDecompositionTreeAlgorithm extends TestCase{

	private List<OOMetric> metrics;
	DecompositionTreeAlgorithm t;
	public void setUp() throws Exception
	{
		metrics = new ArrayList<OOMetric>();
		OOMetric m = null;
		m = new OOMetric();
		m.setId("metric1");
		metrics.add(m);
		m = new OOMetric();
		m.setId("metric2");
		metrics.add(m);
		m = new OOMetric();
		m.setId("metric3");
		metrics.add(m);
		m = new OOMetric();
		m.setId("metric4");
		metrics.add(m);

		t = new DecompositionTreeAlgorithm(metrics);
		return;
	}
	public void tearDown()
	{
		t = null;
		metrics = null;
	}

	public static List<FuzzyEngineReport> getListReport()
	{
		List<FuzzyEngineReport> l = new ArrayList<FuzzyEngineReport>();
		FuzzyEngineReport r = null;
		Map<String, MetricReport> t = null;
		t = new Hashtable<String, MetricReport>();
		MetricReport mr=null;
		mr = new MetricReport();
		mr.setValue(120d);
		t.put("metric1", mr);
		mr = new MetricReport();
		mr.setValue(20d);
		t.put("metric2", mr);
		mr = new MetricReport();
		mr.setValue(75d);
		t.put("metric3", mr);

		//engine report
		r = new FuzzyEngineReport();
		r.setClassname("class 1");
		r.setFuzzySystemOutput(new FuzzyValue(SharpValue.valueOf("120"), SharpValue.valueOf("0.7")));
		r.setMetricReport(t);
		l.add(r);

		t = new Hashtable<String, MetricReport>();
		mr = new MetricReport();
		mr.setValue(28d);
		t.put("metric1", mr);
		mr = new MetricReport();
		mr.setValue(78d);
		t.put("metric2", mr);
		mr = new MetricReport();
		mr.setValue(68d);
		t.put("metric4", mr);

		//engine report
		r = new FuzzyEngineReport();
		r.setClassname("class 2");
		r.setFuzzySystemOutput(new FuzzyValue(SharpValue.valueOf("120"), SharpValue.valueOf("0.7")));
		r.setMetricReport(t);
		l.add(r);

		return l;
	}

	public void testGetClassNames()
	{
		List<FuzzyEngineReport> l = getListReport();
		List<String> classes = t.getClassNames(l);
		assertTrue(classes.size() == 2);
		assertTrue(classes.contains("class 1"));
		assertTrue(classes.contains("class 2"));
	}
	
	public void testGetSharpValuesMetrics()
	{
		List<FuzzyEngineReport> l = getListReport();
		List<SharpValue[]> s = t.getSharpValuesMetrics(l, 200);
		assertTrue(s.size()==2);
		SharpValue[] s1= s.get(0);
		assertTrue(s1.length == 4);
		assertTrue(s1[0].compareTo(SharpValue.valueOf("120"))==0);
		assertTrue(s1[1].compareTo(SharpValue.valueOf("20"))==0);
		assertTrue(s1[2].compareTo(SharpValue.valueOf("75"))==0);
		assertTrue(s1[3].compareTo(SharpValue.valueOf("0"))==0);
		SharpValue[] s2= s.get(1);
		assertTrue(s2.length == 4);
		assertTrue(s2[0].compareTo(SharpValue.valueOf("28"))==0);
		assertTrue(s2[1].compareTo(SharpValue.valueOf("78"))==0);
		assertTrue(s2[2].compareTo(SharpValue.valueOf("0"))==0);
		assertTrue(s2[3].compareTo(SharpValue.valueOf("68"))==0);
	}
	
	public void testgetLevelsMinMaxTransitiveClosure()
	{
		List<String> t = new ArrayList<String>();
		t.add("a");
		t.add("b");
		t.add("c");
		t.add("d");
		FuzzyRelation r1 = new FuzzyRelation(t,t);
		int col=0;
		r1.setCellValue(0, col++, SharpValue.valueOf("0"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.2"));
		col=0;
		r1.setCellValue(1, col++, SharpValue.valueOf(".8"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0.2"));
		col=0;
		r1.setCellValue(2, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0.6"));
		col=0;
		r1.setCellValue(3, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0.6"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0"));
		
		List<SharpValue> level  = this.t.getLevelsMinMaxTransitiveClosure(r1);
		assertTrue(level.size()==4);
		assertTrue(level.get(3).compareTo(SharpValue.valueOf("0"))==0);
		assertTrue(level.get(2).compareTo(SharpValue.valueOf("0.2"))==0);
		assertTrue(level.get(1).compareTo(SharpValue.valueOf("0.6"))==0);
		assertTrue(level.get(0).compareTo(SharpValue.valueOf("0.8"))==0);
	}
	
	public void testDecompositionTreeReport() throws DecompositionTreeException, DecompositionTreeException {
		List<String> t = new ArrayList<String>();
		t.add("a");
		t.add("b");
		t.add("c");
		t.add("d");
		FuzzyRelation r1 = new FuzzyRelation(t,t);
		int col=0;
		r1.setCellValue(0, col++, SharpValue.valueOf("0"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(0, col++, SharpValue.valueOf("0.2"));
		col=0;
		r1.setCellValue(1, col++, SharpValue.valueOf(".8"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(1, col++, SharpValue.valueOf("0.2"));
		col=0;
		r1.setCellValue(2, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0"));
		r1.setCellValue(2, col++, SharpValue.valueOf("0.6"));
		col=0;
		r1.setCellValue(3, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0.6"));
		r1.setCellValue(3, col++, SharpValue.valueOf("0"));
		
		FuzzyRelation result = new TransitiveClosureFactory().getMinMaxTransitiveClosure(r1);
		List<SharpValue> level  = this.t.getLevelsMinMaxTransitiveClosure(result);
		
		DecompositionTreeReport r = new DecompositionTreeReport(level, result, 200);

		List l = r.getClasses(SharpValue.valueOf("0"));
		assertTrue(l.size() == 4);
		List l1 = r.getClasses(SharpValue.valueOf("0.2"));
		assertTrue(l1.size() == 1);
		//System.out.println(l.toString());
	}
	
	public void testDecompositionTreeReport_test2() throws DecompositionTreeException
	{
		List<String> t = new ArrayList<String>();
		t.add("a");
		t.add("b");
		t.add("c");
		t.add("d");
		t.add("e");
		FuzzyRelation r1 = new FuzzyRelation(t,t);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));

		List<SharpValue> level  = this.t.getLevelsMinMaxTransitiveClosure(r1);
		assertTrue(level.size()==4);
		DecompositionTreeReport r = new DecompositionTreeReport(level, r1, 200);
		List l = null;
		l = r.getClasses(SharpValue.valueOf("0"));
		System.out.println(l.toString());
		assertTrue(l.size()==4);
		l = r.getClasses(SharpValue.valueOf("0.1"));
		System.out.println(l.toString());
		assertTrue(l.size()==3);
		l = r.getClasses(SharpValue.valueOf("0.2"));
		System.out.println(l.toString());
		assertTrue(l.size()==2);
		l = r.getClasses(SharpValue.valueOf("0.8"));
		assertTrue(l.size()==1);
		System.out.println(l.toString());
	}

}
