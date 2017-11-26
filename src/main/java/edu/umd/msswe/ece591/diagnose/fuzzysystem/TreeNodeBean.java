package edu.umd.msswe.ece591.diagnose.fuzzysystem;

import java.util.List;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class TreeNodeBean {
	private String id;
	private SharpValue level;
	private List<List> listOfSimilarityGroups;
	private List<String> listOfClasses;
	
	public TreeNodeBean(String id)
	{
		this.id = id;
	}

	public TreeNodeBean(String id, SharpValue level)
	{
		this.id = id;
		this.level = level;
	}

	public List<List> getListOfSimilarityGroups()
	{
		return listOfSimilarityGroups;
	}
	
	public SharpValue getLevel()
	{
		return level;
	}
	
	public String toString()
	{
		if (level != null)
		{
			if (listOfSimilarityGroups != null)
				return "Similarity Level "+level;
			else
				return "Level "+level+" - Group "+id;
		}
		else 
			return id;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the listOfClasses
	 */
	public List<String> getListOfClasses() {
		return listOfClasses;
	}

	/**
	 * @param listOfClasses the listOfClasses to set
	 */
	public void setListOfClasses(List<String> listOfClasses) {
		this.listOfClasses = listOfClasses;
	}

	/**
	 * @param listOfSimilarityGroups the listOfSimilarityGroups to set
	 */
	public void setListOfSimilarityGroups(List<List> listOfSimilarityGroups) {
		this.listOfSimilarityGroups = listOfSimilarityGroups;
	}
}
