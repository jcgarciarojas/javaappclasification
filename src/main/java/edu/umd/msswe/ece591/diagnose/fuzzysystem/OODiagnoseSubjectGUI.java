package edu.umd.msswe.ece591.diagnose.fuzzysystem;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public interface OODiagnoseSubjectGUI {
	
	public void setControlObject(ControlOODiagnose control);
	public boolean validateEntries();
	public void showSucessFullExecution(String message);
	public void showError(String message);
	public void initComponents();
	public void setFuzzyReport(String text);
	public void setMetricsInfo(String text);
	public Object getLastNodeSelected();
	public GUIDiagnoseBean getGuiInformation();
	public void setGUIDiagnoseBean(GUIDiagnoseBean bean);
	
	public void goStartPanel();
	public void goTreePanel();

}
