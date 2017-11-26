package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.exception.ReportException;
import edu.umd.msswe.ece591.diagnose.reports.Report.OutputType;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public interface ControlOODiagnose {

	public void execute()  throws ConfigurationException, FuzzySystemException, DecompositionTreeException;
	public DefaultMutableTreeNode createTreeReport();
	public void setInitialDataFields();
	public void saveReport(OutputType type) throws ReportException;
	public void setGUIDiagnoseBean(GUIDiagnoseBean subjectBean);
}