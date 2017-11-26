package diagnose.reports;


import edu.umd.msswe.ece591.diagnose.configuration.SystemConfiguration;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyValue;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.MetricReport;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;
import edu.umd.msswe.ece591.diagnose.metrics.ProxyClassInfo;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;

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

public class JUnitTestFuzzyReport {
	
	public JUnitTestFuzzyReport()
	{
	}
	
	/**
	 * these methods below are for testing purposes
	 *
	 */
	public JavaApplication getJavaApplication()
	{
		JavaApplication javaApplication = new JavaApplication();
		javaApplication.addClassObject(new ProxyClassInfo("con.edu.fuzzy", "Class1", "id"));
		javaApplication.addClassObject(new ProxyClassInfo("con.edu.fuzzy", "Class2", "id"));
		javaApplication.addClassObject(new ProxyClassInfo("con.edu.fuzzy", "Class3", "id"));
		javaApplication.addClassObject(new ProxyClassInfo("con.edu.fuzzy.subsystem", "Class1", "id"));
		javaApplication.addClassObject(new ProxyClassInfo("con.edu.fuzzy.subsystem", "Class2", "id"));
		
		return javaApplication;
	}
	
	public List<OOMetric> getMetrics()throws ConfigurationException
	{
		SystemConfiguration sysConfig = new SystemConfiguration();
		
		Hashtable<String, OOMetric> ooMetrics = new Hashtable<String, OOMetric>();
		OOMetric m = null;
		m = new OOMetric();
		m.setId("COB");
		m.setName("Coupling between object classes");
		m.setDefinition("The coupling between object classes (CBO) metric represents the number of classes coupled to a given class (efferent couplings, Ce). This coupling can occur through method calls, field accesses, inheritance, arguments, return types, and exceptions. ");
		m.setSolution("Here is the solution of COB");
		ooMetrics.put("COB", m);
		
		m = new OOMetric();
		m.setId("WMC");
		m.setName("Weighted methods per class");
		m.setDefinition("A class's weighted methods per class WMC metric is simply the sum of the complexities of its methods. As a measure of complexity we can use the cyclomatic complexity, or we can abritrarily assign a complexity value of 1 to each method. The ckjm program assigns a complexity value of 1 to each method, and therefore the value of the WMC is equal to the number of methods in the class. ");
		m.setSolution("Here is the solution of WMC");
		ooMetrics.put("WMC", m);

		m = new OOMetric();
		m.setId("DIT");
		m.setName("Depth of Inheritance Tree");
		m.setDefinition("The depth of inheritance tree (DIT) metric provides for each class a measure of the inheritance levels from the object hierarchy top. In Java where all classes inherit Object the minimum value of DIT is 1. ");
		m.setSolution("Here is the solution of DIT");
		ooMetrics.put("DIT", m);

		m = new OOMetric();
		m.setId("NOC");
		m.setName("Number of Children");
		m.setDefinition("A class's number of children (NOC) metric simply measures the number of immediate descendants of the class.");
		m.setSolution("Here is the solution of NOC");
		ooMetrics.put("NOC", m);
		
		sysConfig.setOoMetrics(ooMetrics);
		return sysConfig.getOoMetricsForClasses();
	}
	
	public List<FuzzyEngineReport> getFuzzyEngineReport()throws ConfigurationException
	{
		List<FuzzyEngineReport> engineReportList = new ArrayList<FuzzyEngineReport>();
		 
		engineReportList.add(this.getFuzzyEngineReport(null));
		for(String className: getJavaApplication().getJavaNames())
		{
			engineReportList.add(this.getFuzzyEngineReport(className));
		}
		
		return engineReportList;
	}
	
	private FuzzyEngineReport getFuzzyEngineReport(String className)throws ConfigurationException
	{
		FuzzyEngineReport engineReport = new FuzzyEngineReport();
		engineReport.setClassname(className);
		engineReport.setFuzzySystemOutput(new FuzzyValue(SharpValue.valueOf("8"), SharpValue.valueOf(""+Math.random())));
		
		Map<String, MetricReport> d = new Hashtable<String, MetricReport>();
		for (OOMetric ooMetric: getMetrics())
		{
			
			MetricReport metricReport = new MetricReport();
			List<String> details = new ArrayList<String>();
			details.add("1. here some details should be added for each of the metrics report");
			details.add("2. here some details should be added for each of the metrics report");
			metricReport.setDetail(details);
			metricReport.setValue(Math.random());
			d.put(ooMetric.getId(), metricReport);
		}
		engineReport.setMetricReport(d);
		return engineReport;
		
	}
}
