package diagnose.fuzzyrules;

import edu.umd.msswe.ece591.diagnose.configuration.FuzzySetFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.*;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.FuzzyEngineImpl;
import edu.umd.msswe.ece591.diagnose.metrics.*;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestFuzzyEngine extends TestCase {
	

	public void setUp() throws Exception
	{
		return;
	}

	public void tearDown()
	{
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void testFuzzyEngine() throws MetricsException, FuzzySystemException, ConfigurationException
	{
		List<FuzzyRule> fuzzyRules = new ArrayList<FuzzyRule>();
		FuzzyRule rule = new FuzzyRule("rule1");
		rule.setIfCondition(new IfComposite());
		rule.setThenCondition("id1");
		fuzzyRules.add(rule);

		JavaApplication javaApplication = mock(JavaApplication.class);
		List<JavaClassInfo> l = new ArrayList<JavaClassInfo>();
		JavaClassInfo classInfo = new ProxyClassInfo("a.b", "MyClass", "id001");
		l.add(classInfo);
		when(javaApplication.getFiles()).thenReturn(l);

		Map<String, MetricReport> lm = new Hashtable<String, MetricReport>();
		MetricReport r = new MetricReport();
		r.setValue(10d);
		lm.put("metric1", r);
		MetricsEngine metricsEngine = mock(MetricsEngine.class);
		when(metricsEngine.process(javaApplication, classInfo)).thenReturn(lm);
		when(metricsEngine.process(javaApplication)).thenReturn(lm);

		FuzzyRulesEngine rulesEngine = mock(FuzzyRulesEngine.class);

		SharpValue lsv = SharpValue.valueOf("0.8");
		when(rulesEngine.fuzzyMatching(rule, lm)).thenReturn(lsv);

		List<FuzzySet> lSet = new ArrayList<FuzzySet>();
		FuzzySet set = FuzzySetFactory.instance().createFuzzySetByType("triangle-set");
		lSet.add(set);
		when(rulesEngine.inference(rule, lsv)).thenReturn(set);
		ResultFuzzySet rSet = new ResultFuzzySet();
		when(rulesEngine.combination(lSet)).thenReturn(rSet);

		FuzzyValue output = new FuzzyValue(SharpValue.valueOf("70"), SharpValue.valueOf("0.8"));
		when(rulesEngine.defuzzification(rSet)).thenReturn(output);
		FuzzyEngineImpl engine = new FuzzyEngineImpl(metricsEngine,rulesEngine,javaApplication, null);
		
		List<FuzzyEngineReport> report = engine.execute(fuzzyRules, fuzzyRules);

		assertTrue(report!= null);
		assertTrue(report.size() == 2);
		assertTrue(report.get(0)!= null);
		assertTrue(report.get(0).getClassname().equals("a.b.MyClass"));
		assertTrue(report.get(0).getFuzzySystemOutput().equalTo(output));
		assertTrue(report.get(0).getMetricReport().equals(lm));
		
	}

}
