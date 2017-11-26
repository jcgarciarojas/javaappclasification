package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.reports.DecompositionTreeReport;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyReport.ComparatorByClassName;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyReport.ComparatorByClassification;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class DefaultMutableTreeFactory {

	private DecompositionTreeReport treeReport;
	
	/**
	 * 
	 * @param treeReport
	 */
	public DefaultMutableTreeFactory(DecompositionTreeReport treeReport)
	{
		this.treeReport = treeReport;
	}
	
	/**
	 * This method generates the DefaultMutableTreeNode to be displayed in the GUI 
	 * @return
	 */ 
	public DefaultMutableTreeNode generateTree()
	{
		DefaultMutableTreeNode parent =
            new DefaultMutableTreeNode("Java Application Design Diagnose");
		this.addTreeStructureToRoot(parent);
		return parent;
	}
	
	/**
	 * add the decomposition tree report to the DefaultMutableTreeNode 
	 * @param root
	 */
	protected  void addTreeStructureToRoot(DefaultMutableTreeNode root)
	{
		//loop through each of the levels in the treeReport (fuzzy decomposition tree)
		List<SharpValue> values = treeReport.getLevelValues();
		int index=0;
    	//add the similarity group to the tree
    	TreeNodeBean bean = new TreeNodeBean("Decomposition Tree");
    	DefaultMutableTreeNode similarityGroups = new DefaultMutableTreeNode(bean);
    	root.add(similarityGroups);
    	
        for(SharpValue value : values)
        {
        	//get the lists of classes for the current level  
        	List<List> listOfGroups = treeReport.getClasses(value);
        	
        	TreeNodeBean beanNode = new TreeNodeBean("Similarity Level " + value);
        	beanNode.setListOfSimilarityGroups(listOfGroups);
        	DefaultMutableTreeNode similarityNode = new DefaultMutableTreeNode(beanNode);
        	similarityGroups.add(similarityNode);

        	//add the children to the tree (groups and classes)
        	this.addGroupsListNode(similarityNode, value, listOfGroups);
        	index++;
        }

        TreeNodeBean ClassesByClassBean = new TreeNodeBean("Classes by classification");
    	DefaultMutableTreeNode classes1Node = new DefaultMutableTreeNode(ClassesByClassBean);
    	root.add(classes1Node);
    	List<FuzzyEngineReport> l = treeReport.getEngineReportList();
    	addClassesListNode(classes1Node, l);
    	
    	TreeNodeBean ClassesbyNameBean = new TreeNodeBean("Classes by name");
    	DefaultMutableTreeNode classes2Node = new DefaultMutableTreeNode(ClassesbyNameBean);
    	root.add(classes2Node);
    	addClassesByNameListNode(classes2Node, treeReport.getEngineReportList());
	}

	
	/**
	 * Add groups and classes to node
	 * @param parent
	 * @param value
	 * @param listOfGroups
	 */
	protected void addGroupsListNode(DefaultMutableTreeNode parent, SharpValue value, List<List> listOfGroups)
	{
		int index =0;
		for(List<String> classes: listOfGroups)
		{
			TreeNodeBean beanGroup = new TreeNodeBean(""+index, value);
			beanGroup.setListOfClasses(classes);
			DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(beanGroup);
			parent.add(newChild);

			index++;
		}
		return;
	}

	
	
	/**
	 * 
	 * @param parent
	 * @param value
	 * @param listOfGroups
	 */
	@SuppressWarnings("unchecked")
	protected void addClassesListNode(DefaultMutableTreeNode parent, List<FuzzyEngineReport> reports)
	{
		Collections.sort(reports, new ComparatorByClassification());
		String classification = null;
		TreeNodeBean beanClassification = null;
		DefaultMutableTreeNode newChild = null;
		
		for(FuzzyEngineReport r: reports)
		{
			if(r.getClassname() == null) continue;
			
			if (classification == null || !classification.equals(r.getClassification()))
			{
				classification = r.getClassification();
				beanClassification = new TreeNodeBean(classification);
				newChild = new DefaultMutableTreeNode(beanClassification);
				parent.add(newChild);
			}
			
			TreeNodeBean beanClass = new TreeNodeBean(r.getClassname());
			DefaultMutableTreeNode newGrandChild = new DefaultMutableTreeNode(beanClass);
			newChild.add(newGrandChild);
			
		}
	}
	
	/**
	 * 
	 * @param parent
	 * @param value
	 * @param listOfGroups
	 */
	@SuppressWarnings("unchecked")
	protected void addClassesByNameListNode(DefaultMutableTreeNode parent, List<FuzzyEngineReport> reports)
	{
    	Collections.sort(reports, new ComparatorByClassName());

    	for(FuzzyEngineReport r: reports)
		{
    		if(r.getClassname() == null) continue;
			TreeNodeBean beanClass = new TreeNodeBean(r.getClassname());
			DefaultMutableTreeNode newGrandChild = new DefaultMutableTreeNode(beanClass);
			parent.add(newGrandChild);
		}
	}

}
