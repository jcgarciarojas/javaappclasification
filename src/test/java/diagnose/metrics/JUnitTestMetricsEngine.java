package diagnose.metrics;

import diagnose.configuration.JUnitTestJavaClassInfoFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.MetricsException;
import edu.umd.msswe.ece591.diagnose.metrics.*;
import junit.framework.TestCase;

import java.util.ArrayList;
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

public class JUnitTestMetricsEngine extends TestCase {
	
	private MetricsEngine engine;

	public void setUp() throws Exception
	{
		List<OOMetric> metrics = new ArrayList<OOMetric>();
		metrics.add(getMetric());
		engine = new MetricsEngineImpl(metrics, metrics);
	}

	public static OOMetric getMetric() throws Exception
	{
		OOMetric metric = new OOMetric();
		metric.setClassName("edu.umd.msswe.ece591.diagnose.metrics.LOC");
		metric.setDefinition("definition");
		metric.setId("LOC");
		metric.setName("Lines of Code");
		metric.setSolution("Solution");
		
		return metric;
	}

	public void tearDown()
	{
		engine.equals(null);
	}
	
	public void testProcess() throws MetricsException, ConfigurationException
	{
		
		Map<String, MetricReport> l = engine.process(new JavaApplication(), JUnitTestJavaClassInfoFactory.getJavaClassesList());
		assertTrue(l.size()>0);
		
		assertTrue(l.get("LOC") != null);
		MetricReport report = l.get("LOC");
		assertTrue(report.getValue() > 0);
		
	}
	
}
