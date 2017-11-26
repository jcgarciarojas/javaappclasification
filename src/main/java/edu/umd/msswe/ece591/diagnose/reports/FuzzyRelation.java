package edu.umd.msswe.ece591.diagnose.reports;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class FuzzyRelation {

	private List<String> rowTitles;
	private List<String> columnTitles;
	private Hashtable<String, SharpValue> matrix = new Hashtable<String, SharpValue>(); 
	
	/**
	 * 
	 * @param rowTitles
	 * @param columnTitles
	 */
	public FuzzyRelation(List<String> rowTitles, List<String> columnTitles){
		this.rowTitles = rowTitles;
		this.columnTitles = columnTitles;
	}
	
	public String getTitle(int col)
	{
		return columnTitles.get(col);
	}

	/**
	 * 
	 * @param col
	 * @param row
	 * @param value
	 */
	public void setCellValue(int row, int col, SharpValue value)
	{
		matrix.put(col+","+row, value);
	}
	
	/**
	 * 
	 * @param col
	 * @param row
	 * @param value
	 */
	public SharpValue getCellValue(int row, int col)
	{
		return matrix.get(col+","+row);
	}
	
	/**
	 * 
	 * @return an array with the relation size given by row[0] col[1] 
	 */
	public int[] getDimension()
	{
		int[] dimension = {rowTitles.size(), columnTitles.size()};
		return dimension;
	}

	/**
	 * @return the columnTitles
	 */
	public List<String> getColumnTitles() {
		return columnTitles;
	}

	/**
	 * @return the rowTitles
	 */
	public List<String> getRowTitles() {
		return rowTitles;
	}
	
	public String toString()
	{
		StringBuffer b= new StringBuffer(columnTitles.toString()+ "\n"+
				rowTitles.toString()+ "\n");
		for(int row=0;row<rowTitles.size();row++)
		{
			String a="";
			for(int col=0;col<columnTitles.size();col++)
			{
				a+= ""+getCellValue(row, col) +"\t";
			}
			b.append(a+"\n");
		}
		
		return b.toString();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public FuzzyRelation cloneObject()
	{
		FuzzyRelation r = new FuzzyRelation(this.rowTitles, this.columnTitles);
		Hashtable<String, SharpValue> matrix = new Hashtable<String, SharpValue>();
		Enumeration e = this.matrix.keys();
		while(e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			SharpValue value = this.matrix.get(key);
			matrix.put(key, value.cloneObject());
		}
		r.matrix = matrix;
		return r;
	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	public List<SharpValue> getRow(int row)
	{
		List<SharpValue> l = new ArrayList<SharpValue>();
		for (int col=0;col<this.columnTitles.size();col++)
		{
			l.add(this.getCellValue(row, col));
		}
		return l;
	}

	public List<SharpValue> getCol(int col)
	{
		List<SharpValue> l = new ArrayList<SharpValue>();
		for (int row=0;row<this.columnTitles.size();row++)
		{
			l.add(this.getCellValue(row, col));
		}
		return l;
	}
	
	/**
	 * returns 0 if this fuzzy relation equal to otherFuzzyRelation; 
	 * less than 0 if this fuzzy relation is numerically less than otherFuzzyRelation; 
	 * and greater than 0 if this fuzzy relation is numerically greater than otherFuzzyRelation.
	 * if the relations are totally different then the methods throws a ReportException 
	 * @param value
	 * @return
	 */
	public int compareTo(FuzzyRelation r2) throws DecompositionTreeException
	{
		int countEqual = 0;
		int countGreater = 0;
		int countLessThan = 0;
		Enumeration e = this.matrix.keys();
		
		int count =0;
		while(e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			SharpValue s1 = this.matrix.get(key);
			SharpValue s2 = r2.matrix.get(key);
			int comparison =  s1.compareTo(s2);
			
			if(comparison == 0) countEqual++;
			else if(comparison < 0) countLessThan++;
			else if(comparison > 0) countGreater++;
			count++;
		}
		
		if (count == countEqual) return 0;
		else if (count == (countEqual+countGreater)) return 1;
		else if (count == (countEqual+countLessThan)) return -1;
		else throw new DecompositionTreeException("the relations are different ");
	}

}
