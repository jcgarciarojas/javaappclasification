package edu.umd.msswe.ece591.diagnose.reports;

import java.util.ArrayList;
import java.util.List;

import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
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

public class TransitiveClosureFactory 
{

	/**
	 * create a fuzzy binary relation from messages using hamming distance
	 * @param rowTitles
	 * @param columnTitles
	 * @param classes
	 * @return a dissimitud fuzzy relation (zero value in the diagonals).
	 * @throws FuzzySystemException
	 */
	public FuzzyRelation getRelationUsingHamming(List<String> rowTitles, List<String> columnTitles, 
			List<SharpValue[]> values) throws DecompositionTreeException
	{

		if (rowTitles.size() != columnTitles.size() || rowTitles.size() != values.size())
			throw new DecompositionTreeException("size of titles or size of classes doesn't match");

		FuzzyRelation r = new FuzzyRelation(rowTitles, columnTitles);
		for (int row=0; row< values.size(); row++)
			r.setCellValue(row, row, SharpValue.valueOf("0"));
		
		for (int row=0; row< values.size()-1; row++)
		{
			SharpValue[] class1 = values.get(row);
			for (int col=row+1 ; col< values.size(); col++)
			{
				SharpValue[] class2 = values.get(col);
				SharpValue value = createHammingValue(class1, class2);
				r.setCellValue(row, col, value);
				r.setCellValue(col, row, value);
			}
		}
		
		return r;
	}
	/**
	 * the java classes are classified using different metrics, for purpose of assesing the classes 
	 * they must be classified as to the output of the metrics
	 */
	public SharpValue createHammingValue(SharpValue[] class1, SharpValue[] class2) throws DecompositionTreeException
	{
		if (class1.length != class2.length)
			throw new DecompositionTreeException("messages should have the same length ["+class1.length +","+ class2.length+"]");
		
		double result=0;
		for (int i=0; i<class1.length; i++)
		{
			result += Math.abs(class1[i].getValue() - class2[i].getValue());
		}
		return new SharpValue(result/class2.length);
	}
	
	
	/**
	 * This method applies min max composition to R1's row and R2's column  
	 * the operation first applies the max composition over the rows and columns 
	 * and the the min composition over the results 
	 * @param rowR1
	 * @param colR2
	 * @return
	 */
	public SharpValue minMaxComposition(List<SharpValue> rowR1, List<SharpValue> colR2)
	{
		int index=0; 
		
		SharpValue s1 = null;
		for(SharpValue row: rowR1)
		{
			SharpValue col = colR2.get(index);
			SharpValue s2 = SharpValue.getMax(row, col);
			if(s1 == null) s1 = s2;
			s1 = SharpValue.getMin(s1, s2);	
			index++;
		}
		
		return s1;
	}
	
	/**
	 * 
	 * @param r1
	 * @return
	 * @throws DecompositionTreeException
	 */
	public FuzzyRelation getMinMaxTransitiveClosure(FuzzyRelation r1) throws DecompositionTreeException
	{
		FuzzyRelation min = null;

		for(FuzzyRelation r: getMinMaxCompositionList(r1))
			if (min == null) min = r; 
			else if (r.compareTo(min) < 0) min = r;
		
		return min;
	}
	
	/**
	 * 
	 * @param r1
	 * @return
	 * @throws DecompositionTreeException
	 */
	public List<FuzzyRelation> getMinMaxCompositionList(FuzzyRelation r1) throws DecompositionTreeException
	{
		List<FuzzyRelation> l = new ArrayList<FuzzyRelation>();
		l.add(r1.cloneObject());
		
		FuzzyRelation relationPower = r1.cloneObject();
		
		while(true)
		{
			FuzzyRelation result = getRelationUsingMinMax(r1, relationPower);
			if(relationPower.compareTo(result) == 0) break;
			l.add(result);
			relationPower = result.cloneObject();
		}
		
		return l;
	}
	
	/**
	 * return a relation using max min composition over the two relations passed as parameters 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public FuzzyRelation getRelationUsingMinMax(FuzzyRelation r1, FuzzyRelation r2)
	{
		FuzzyRelation result = new FuzzyRelation(r1.getRowTitles(), r1.getColumnTitles());
		int r1RowSize = r1.getDimension()[0];
		int r2ColSize = r2.getDimension()[1];
		
		for (int row =0; row < r1RowSize; row++)
		{
			for (int col =0; col < r2ColSize; col++)
				result.setCellValue(row, col, this.minMaxComposition(r1.getRow(row), r2.getCol(col)));
		}
		
		return result;
	}	
}
