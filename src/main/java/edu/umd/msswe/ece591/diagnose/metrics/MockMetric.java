package edu.umd.msswe.ece591.diagnose.metrics;

import edu.umd.msswe.ece591.diagnose.exception.MetricsException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class MockMetric extends Metric {

	@Override
	public MetricReport execute(JavaApplication javaClassesList)
			throws MetricsException {
		MetricReport report = new MetricReport();
		report.setValue(57d);
		return report;
	}

	@Override
	public MetricReport execute(JavaApplication javaClassesList,
			JavaClassInfo classFile) throws MetricsException {
		MetricReport report = new MetricReport();
		report.setValue(57d);
		return report;
	}

}
