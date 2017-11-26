package diagnose.reports;

import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyRelation;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class JUnitTestFuzzyRelation extends TestCase {

	public void setUp() throws Exception
	{
		return;
	}
	public void tearDown()
	{
	}
	
	public void testTitlesRelation()
	{
		int i=0;
		List<String> t = new ArrayList<String>();
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		FuzzyRelation r = new FuzzyRelation(t,t);
		int[] d = r.getDimension();
		assertTrue(d[0] == t.size());
		assertTrue(d[1] == t.size());
	}

	public void testContentDiagonalRelation()
	{
		int i=0;
		List<String> t = new ArrayList<String>();
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		FuzzyRelation r = new FuzzyRelation(t,t);
		int[] d = r.getDimension();
		for (int row=0; row< d[0]; row++)
		{
			r.setCellValue(row, row, SharpValue.valueOf("0"));
		}

		for (int row=0; row< d[0]; row++)
		{
			assertTrue(SharpValue.valueOf("0").compareTo(r.getCellValue(row, row)) == 0);
		}

	}

	public void testContentRelation()
	{
		int i=0;
		List<String> t = new ArrayList<String>();
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		t.add(""+(i++));
		FuzzyRelation r = new FuzzyRelation(t,t);
		int[] d = r.getDimension();
		for (int row=0; row< d[0]; row++)
		{
			for (int col=0; col< d[1]; col++)
			{
				r.setCellValue(row, col, SharpValue.valueOf(""+(col+row)));
			}
		}
		
		for (int row=0; row< d[0]; row++)
		{
			for (int col=0; col< d[1]; col++)
			{
				assertTrue(SharpValue.valueOf(""+(col+row)).compareTo(r.getCellValue(row, col)) == 0);
			}
		}

	}

}
