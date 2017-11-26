package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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

public class ControlOODiagnoseImpl implements TreeSelectionListener, ActionListener, ControlOODiagnose 
{
	private DiagnoseFacade facade;
	private OODiagnoseSubjectGUI subject;

	public ControlOODiagnoseImpl(OODiagnoseSubjectGUI subject, DiagnoseFacade facade)
	{
		this.subject = subject;
		this.facade = facade;
	}
	
	/**
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode createTreeReport()
	{
		DefaultMutableTreeNode tree= null;
		try {
			tree = facade.createTreeReport();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return tree;
	}
	
	/**
	 * 
	 */
	public void setInitialDataFields()
	{
		subject.setFuzzyReport(facade.getSummaryReport());
		subject.setMetricsInfo(facade.getMetricsSummary());
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		try{
			this.setGUIDiagnoseBean(subject.getGuiInformation());
			
			if (OODiagnoseSubjectGUIImpl.RESET_C0MMAND.equals(e.getActionCommand()))
				subject.initComponents();
			
			else if (OODiagnoseSubjectGUIImpl.SUBMIT_C0MMAND.equals(e.getActionCommand())) 
			{
				if (!subject.validateEntries())
					subject.showError("Confirm input values and resubmit job");
				else
				{
					this.execute();
					subject.goTreePanel();
					this.setInitialDataFields();
				}
				return;

			} else if (OODiagnoseSubjectGUIImpl.SAVE_C0MMAND.equals(e.getActionCommand())) 
					this.saveReport(OutputType.TO_XML);

			else if (OODiagnoseSubjectGUIImpl.RESTART_C0MMAND.equals(e.getActionCommand())) 
			{
				subject.goStartPanel();
				return;
			}
			subject.showSucessFullExecution("Execution completed!");
			
			
		} catch(ConfigurationException ex)
		{
			ex.printStackTrace();
			subject.showError("Error in the input parameters or the configuration of the system");
		} catch(FuzzySystemException ex)
		{
			ex.printStackTrace();
			subject.showError("Error while diagnosing the system");
		} catch(ReportException ex)
		{
			ex.printStackTrace();
			subject.showError("Error generating report");
		}catch(Exception ex)
		{
			ex.printStackTrace();
			subject.showError("General Exception");
		}
	}
	
	/**
	 * 
	 */
	public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        				subject.getLastNodeSelected();

        if (node == null) return;
        if (node.isRoot())
        {
			subject.setFuzzyReport(facade.getSummaryReport());
			subject.setMetricsInfo(facade.getMetricsSummary());
        }
        else {
	        Object nodeInfo = node.getUserObject();
	    	TreeNodeBean bean = (TreeNodeBean)nodeInfo;
	    	subject.setFuzzyReport(facade.getFuzzyReport(bean));
        }
	}
	
	/**
	 * 
	 */
	public void execute() throws ConfigurationException, FuzzySystemException, DecompositionTreeException
	{
		facade.loadConfiguration();
		facade.loadCodeStructure();
		facade.diagnose();
		facade.deleteWorkingDirectory();
	}
	
	/**
	 * 
	 */
	public void saveReport(OutputType type) throws ReportException
	{
		facade.saveReport(type);
	}
	
	/**
	 * 
	 */
	public void setGUIDiagnoseBean(GUIDiagnoseBean subjectBean)
	{
		facade.setGUIDiagnoseBean(subjectBean);
	}

}