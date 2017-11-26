package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class OODiagnoseSubjectGUIImpl implements ItemListener, OODiagnoseSubjectGUI {

	private static final long serialVersionUID = 1L;
	
	public static final String CONTENT_TYPE = "plain/text"; 
	private ControlOODiagnose controlObject;

	private JFrame jFrame=null;
	private JPanel cards = null;
	private JPanel jContentPane = null;
	private JPanel jCardPanel1 = null;

	private JTextField jTextField7 = null;
	private JLabel jLabel7 = null;
	private JPanel jPanel7 = null;

	private JTextField jTextField0 = null;
	private JLabel jLabel0 = null;
	private JPanel jPanel0 = null;

	private JTextField jTextField1 = null;
	private JLabel jLabel1 = null;
	private JPanel jPanel1 = null;
	private JTextField jTextField2 = null;
	private JLabel jLabel2 = null;
	private JPanel jPanel2 = null;
	private JTextField jTextField3 = null;
	private JCheckBox jCheckBox3 = null;
	private JPanel jPanel3 = null;
	private JTextField jTextField4 = null;
	private JCheckBox jCheckBox4 = null;
	private JPanel jPanel4 = null;

	private JTextField jTextField9 = null;
	private JCheckBox jCheckBox9 = null;
	private JPanel jPanel9 = null;

	private JPanel jPanel5 = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;

	private JPanel jContentPane2 = null;
	private JPanel jCardPanel2 = null;
	private JScrollPane scrollPane0 = null;
	private JTextPane  jEditorPane0 = null;

	private JPanel jSplitPane1 = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane scrollPane1 = null;
	private JScrollPane scrollPane2=null;
	private JTree jTree = null;
	private JTextPane jEditorPane1 = null;
	private JPanel jPanel6=null;
	private JButton jButton3=null;

	private JPanel jPanel10=null;
	private JButton jButton10=null;
	
	public static final String START_PANEL = "START_PANEL";
	public static final String TREE_PANEL = "TREE_PANEL";
	public static final String SAVE_C0MMAND ="save";  //  @jve:decl-index=0:
	public static final String RESET_C0MMAND ="reset";  //  @jve:decl-index=0:
	public static final String SUBMIT_C0MMAND ="submit";  //  @jve:decl-index=0:
	public static final String RESTART_C0MMAND = "restart";
    
	public OODiagnoseSubjectGUIImpl()
	{
		super();
		//this.createWindow();
	}
	
	public void createWindow()
	{
		jFrame = new JFrame(); 
		jFrame.setTitle("Fuzzy Diagnose - Java Applications"); 
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentToPanels(jFrame.getContentPane());
        //Display the window.
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setSize(500,280);
	}
	
	public void setGUIDiagnoseBean(GUIDiagnoseBean bean)
	{
		
		//TODO: this information should be pass from the arguments passed to the program 
		jTextField0.setText(bean.projectName);
		jTextField1.setText(bean.workingDirectory);
		jTextField2.setText(bean.sourceDirectories);
		jTextField7.setText(bean.outputLocation);
		jTextField4.setText(bean.additionalClassPath);
		jTextField9.setText(bean.metricOutputLocation);
		
		/*jTextField1.setText("C:/temp/test");
		jTextField2.setText("C:/health/bie/OODiagnose/source/edu");
		jTextField7.setText("C:/temp/fuzzy report.xml");
		jTextField4.setText("C:/health/bie/OODiagnose/lib/mockito/mockito-all-1.8.5.jar;C:/Program Files/IBM/SDP70Shared/plugins/org.junit_3.8.1/junit.jar;C:/Program Files/Java/jdk1.6.0_18/lib/tools.jar");
*/
	}


	private void addComponentToPanels(Container pane) {
		cards = new JPanel(new CardLayout());
		cards.add(addComponentToPane1(), START_PANEL);
	    pane.add(cards, BorderLayout.CENTER);
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel addComponentToPane1() {
		if (jContentPane == null) 
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			this.initComponents();
		}
		return jContentPane;
	}

	/**
	 * This method initializes jCardPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jCardPanel1 == null) {
			jCardPanel1 = new JPanel();
			jCardPanel1.setLayout(new BoxLayout(jCardPanel1, BoxLayout.PAGE_AXIS));
			jCardPanel1.add(getJPanel0());
			jCardPanel1.add(getJPanel1());
			jCardPanel1.add(getJPanel2());
			jCardPanel1.add(getJPanel7());
			jCardPanel1.add(getJPanel3());
			jCardPanel1.add(getJPanel4());
			jCardPanel1.add(getJPanel9());
			jCardPanel1.add(getJPanel5());
		}
		return jCardPanel1;
	}
	
	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("*Project Name:");
			jLabel0.setPreferredSize(new Dimension(150, 16));
			jPanel0 = new JPanel();
			//jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.LINE_AXIS));
			jPanel0.add(jLabel0);
			jPanel0.add(getJTextField0());
		}
		return jPanel0;
	}
	private JTextField getJTextField0() {
		if (jTextField0 == null) {
			jTextField0 = new JTextField();
			jTextField0.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField0;
	}

	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("*Output Location:");
			jLabel7.setPreferredSize(new Dimension(150, 16));
			jPanel7 = new JPanel();
			//jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.LINE_AXIS));
			jPanel7.add(jLabel7);
			jPanel7.add(getJTextField7());
		}
		return jPanel7;
	}

	private JTextField getJTextField7() {
		if (jTextField7 == null) {
			jTextField7 = new JTextField();
			jTextField7.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField7;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("*Working Directory:");
			jLabel1.setPreferredSize(new Dimension(150, 16));
			jPanel1 = new JPanel();
			//jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.LINE_AXIS));
			jPanel1.add(jLabel1);
			jPanel1.add(getJTextField1());
		}
		return jPanel1;
	}
	
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
			jTextField1.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField1;
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("*Source directories|files:");
			jLabel2.setPreferredSize(new Dimension(150, 16));
			jPanel2 = new JPanel();
			//jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.LINE_AXIS));
			jPanel2.add(jLabel2);
			jPanel2.add(getJTextField2());
		}
		return jPanel2;
	}
	
	private JTextField getJTextField2() {
		if (jTextField2 == null) {
			jTextField2 = new JTextField();
			jTextField2.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField2;
	}

	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jCheckBox3 = new JCheckBox();
			jCheckBox3.setText("Exclude Files/Directories :");
			jCheckBox3.setPreferredSize(new Dimension(150, 16));
			jCheckBox3.setSelected(true);
			jCheckBox3.addItemListener(this);
			jPanel3 = new JPanel();
			jPanel3.add(jCheckBox3);
			jPanel3.add(getJTextField3());
		}
		return jPanel3;
	}
	
	private JTextField getJTextField3() {
		if (jTextField3 == null) {
			jTextField3 = new JTextField();
			jTextField3.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField3;
	}

	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jCheckBox4 = new JCheckBox();
			jCheckBox4.setText("Additional Classpath :");
			jCheckBox4.setPreferredSize(new Dimension(150, 16));
			jCheckBox4.setSelected(true);
			jCheckBox4.addItemListener(this);
			jPanel4 = new JPanel();
			jPanel4.add(jCheckBox4);
			jPanel4.add(getJTextField4());
		}
		return jPanel4;
	}

	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jCheckBox9 = new JCheckBox();
			jCheckBox9.setText("Metric Report Output :");
			jCheckBox9.setPreferredSize(new Dimension(150, 16));
			jCheckBox9.setSelected(true);
			jCheckBox9.addItemListener(this);
			jPanel9 = new JPanel();
			jPanel9.add(jCheckBox9);
			jPanel9.add(getJTextField9());
		}
		return jPanel9;
	}

	private JTextField getJTextField9() {
		if (jTextField9 == null) {
			jTextField9 = new JTextField();
			jTextField9.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField9;
	}

	private JTextField getJTextField4() {
		if (jTextField4 == null) {
			jTextField4 = new JTextField();
			jTextField4.setPreferredSize(new Dimension(200, 20));
		}
		return jTextField4;
	}

	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jButton1 = new JButton();
			jButton1.setText("Reset");
			jButton1.setActionCommand(RESET_C0MMAND);
			jButton1.addActionListener((ActionListener)controlObject);
			jButton2 = new JButton();
			jButton2.setText("Submit");
			jButton2.setActionCommand(SUBMIT_C0MMAND);
			jButton2.addActionListener((ActionListener)controlObject);
			jPanel5 = new JPanel();
			jPanel5.add(jButton1);
			jPanel5.add(jButton2);
		}
		return jPanel5;
	}
	
	public void itemStateChanged(ItemEvent event)
	{
		Object source = event.getItemSelectable();
		if(source == jCheckBox3)
		{
			if (event.getStateChange() == ItemEvent.SELECTED)
				jTextField3.setEnabled(true);
			else
			{
				jTextField3.setEnabled(false);
				jTextField3.setText("");
			}
		}
		if(source == jCheckBox4)
		{
			if (event.getStateChange() == ItemEvent.SELECTED)
				jTextField4.setEnabled(true);
			else
			{
				jTextField4.setEnabled(false);
				jTextField4.setText("");
			}
		}
		if(source == jCheckBox9)
		{
			if (event.getStateChange() == ItemEvent.SELECTED)
				jTextField9.setEnabled(true);
			else
			{
				jTextField9.setEnabled(false);
				jTextField9.setText("");
			}
		}

	}
	
	public void goStartPanel()
	{
		 CardLayout cl = (CardLayout)(cards.getLayout());
		 cl.show(cards, START_PANEL);
		 initComponents();
		 jFrame.setSize(500,280);
	}
	
	public void goTreePanel()
	{
		jContentPane2 = null;
		jSplitPane = null;
		scrollPane1 = null;
		jTree = null;
		 cards.add(getJContentPane2(),  TREE_PANEL);
		 CardLayout cl = (CardLayout)(cards.getLayout());
		 cl.show(cards, TREE_PANEL);
		 controlObject.setInitialDataFields();
		 jFrame.setSize(750,600);
	}

	/**
	 * 
	 */
	public void initComponents()
	{
		jTextField0.setText("");
		jTextField1.setText("");
		jTextField2.setText("");
		jTextField3.setText("");
		jTextField4.setText("");
		jTextField7.setText("");
		jTextField9.setText("");
		jCheckBox3.setSelected(false);
		jCheckBox4.setSelected(false);
		jCheckBox9.setSelected(false);
		jTextField3.setEnabled(false);
		jTextField4.setEnabled(false);
		jTextField9.setEnabled(false);
	}
	    
	private JPanel getJContentPane2() 
	{
		if (jContentPane2 == null)
		{
			jContentPane2 = new JPanel();
			jContentPane2.setLayout(new BorderLayout());
			jContentPane2.add(getMetricJPanel(), BorderLayout.NORTH);
			jContentPane2.add(getJSplitPane(), BorderLayout.CENTER);
					
			jContentPane2.add(getJSplitPaneButtons(), BorderLayout.SOUTH);
		}
		return jContentPane2;
	}
	
	private JPanel getJSplitPaneButtons() {
		if (jSplitPane1 == null) {
			jSplitPane1 = new JPanel();
			jSplitPane1.add(getJPanelSave());
			jSplitPane1.add(getJPanelInit());
		}
		return jSplitPane1;
	}


	private JPanel getJPanelInit() {
		if (jPanel10 == null) {
			jPanel10 = new JPanel();
			jPanel10.add(getJButton10());
			//jCardPanel2.setLayout(new BoxLayout(jCardPanel2, BoxLayout.PAGE_AXIS));
		}
		return jPanel10;
	}
	private JButton getJButton10()
	{
		if (jButton10 == null) 
		{
			jButton10 = new JButton();
			jButton10.setText("Diagnose Again");
			jButton10.setActionCommand(RESTART_C0MMAND);
			jButton10.addActionListener((ActionListener)controlObject);
		}
		return jButton10;
	}
	
	private JPanel getJPanelSave() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.add(getJButton());
			//jCardPanel2.setLayout(new BoxLayout(jCardPanel2, BoxLayout.PAGE_AXIS));
		}
		return jPanel6;
	}
	private JButton getJButton()
	{
		if (jButton3 == null) 
		{
			jButton3 = new JButton();
			jButton3.setText("Save Report");
			jButton3.setActionCommand(SAVE_C0MMAND);
			jButton3.addActionListener((ActionListener)controlObject);
		}
		return jButton3;
	}

	/**
	 * This method initializes jCardPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMetricJPanel() {
		if (jCardPanel2 == null) {
			jCardPanel2 = new JPanel();
			jCardPanel2.setBorder(new TitledBorder("Metrics"));
			jCardPanel2.add(getScrollPane0());
		}
		return jCardPanel2;
	}
	
	private JScrollPane getScrollPane0()
	{
		if (scrollPane0 == null)
		{
			scrollPane0 = new JScrollPane(getJEditorPane0());
			scrollPane0.setAutoscrolls(true);
			scrollPane0.setBackground(java.awt.Color.green);
			scrollPane0.setPreferredSize(new Dimension(600, 150));

		}
		
		return scrollPane0;
	}
	private JTextPane  getJEditorPane0() {
		if (jEditorPane0 == null) {
			jEditorPane0 = new JTextPane ();
			jEditorPane0.setEditable(false);
		}
		return jEditorPane0;
	}
	
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setBorder(new TitledBorder("Fuzzy Report"));
			jSplitPane.setLeftComponent(getJScrollPane1());
			jSplitPane.setRightComponent(getJScrollPane2());
		}
		return jSplitPane;
	}

	private JScrollPane getJScrollPane1()
	{
		if (scrollPane1 == null)
		{
			scrollPane1 = new JScrollPane(getJTree());
			scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			//scrollPane1.setPreferredSize(new Dimension(250, 150));
			scrollPane1.setAutoscrolls(true);
		}
		return scrollPane1;
	}


	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree(controlObject.createTreeReport());
			jTree.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTree.addTreeSelectionListener((TreeSelectionListener)controlObject);
			//jTree.setPreferredSize(new Dimension(250, 150));
		}
		return jTree;
	}

	private JScrollPane getJScrollPane2()
	{
		if (scrollPane2 == null)
		{
			scrollPane2 = new JScrollPane(getJEditorPane());
			scrollPane2.setPreferredSize(new Dimension(400, 150));
			scrollPane2.setAutoscrolls(true);
		}
		return scrollPane2;
	}

	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JTextPane getJEditorPane() {
		if (jEditorPane1 == null) {
			jEditorPane1 = new JTextPane();
			jEditorPane1.setEditable(false);
			//jEditorPane1.setContentType(CONTENT_TYPE);
		}
		return jEditorPane1;
	}
	
	/**
	 * 
	 */
	public void setControlObject(ControlOODiagnose controlObject)
	{
		this.controlObject = controlObject;
	}

	/**
	 * 
	 */
	public boolean validateEntries()
	{
		boolean isValid = true;
		if (jTextField0.getText().trim().equals("") ||
				jTextField1.getText().trim().equals("") ||
				jTextField2.getText().trim().equals("") ||
				jTextField7.getText().trim().equals(""))
			isValid = false;
			//throw new Exception("Fields (Working Directory/Source directories|files) cannot be empty");

		if(jCheckBox3.isSelected() && jTextField3.getText().trim().equals(""))
			isValid = false;
			//throw new Exception("Fields (Exclude Files/Directories) cannot be empty");
		
		if(jCheckBox4.isSelected() && jTextField4.getText().trim().equals("")) 
			//throw new Exception("Fields (Additional Classpath) cannot be empty");
			isValid = false;
		
		return isValid;
	}

	/**
	 * 
	 */
	public void showError(String message)
	{
		JOptionPane.showMessageDialog(jFrame, message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 
	 */
	public void showSucessFullExecution(String message)
	{
		JOptionPane.showMessageDialog(jFrame, message,
				"Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void setFuzzyReport(String text){
		jEditorPane1.setText(text);
		jEditorPane1.setCaretPosition(0);
	}
	public void setMetricsInfo(String text){
		jEditorPane0.setText(text);
		jEditorPane0.setCaretPosition(0);
	}
	
	public Object getLastNodeSelected()
	{
		return jTree.getLastSelectedPathComponent();
	}
	
	public GUIDiagnoseBean getGuiInformation()
	{
		GUIDiagnoseBean bean = new GUIDiagnoseBean();

		bean.projectName = jTextField0.getText();
		bean.workingDirectory = jTextField1.getText();
		bean.sourceDirectories = jTextField2.getText();
		bean.outputLocation = jTextField7.getText();
		bean.filter = jTextField3.getText();
		bean.additionalClassPath = jTextField4.getText();
		bean.metricOutputLocation = jTextField9.getText();
		
		return bean;
	}
	
}

