package diagnose.reports;

import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.reports.DecompositionTreeAlgorithm;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyRelation;
import edu.umd.msswe.ece591.diagnose.reports.TransitiveClosureFactory;
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

public class JUnitTestTransitiveClosureFactory  extends TestCase {

	public void setUp() throws Exception
	{
		return;
	}
	public void tearDown()
	{
	}
	public void testCreateHammingValue_zeroValue() throws DecompositionTreeException
	{
		TransitiveClosureFactory f = new TransitiveClosureFactory();
		SharpValue[] class1= new SharpValue[]{
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.25"),
								};
		SharpValue[] class2= new SharpValue[]{
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.25"),
								};
		SharpValue v = f.createHammingValue(class1, class2);
		assertTrue(v.compareTo(SharpValue.valueOf("0"))==0);
	}
	public void testCreateHammingValue_otherValues() throws DecompositionTreeException
	{
		TransitiveClosureFactory f = new TransitiveClosureFactory();
		SharpValue[] class1= new SharpValue[]{
				   SharpValue.valueOf("0.5"),
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.75"),
				   SharpValue.valueOf("0")};
		SharpValue[] class2= new SharpValue[]{
				   SharpValue.valueOf("0.25"),
				   SharpValue.valueOf("0.5"),
				   SharpValue.valueOf("0.50"),
				   SharpValue.valueOf("0.25")};
		SharpValue v = f.createHammingValue(class1, class2);
		assertTrue(v.compareTo(SharpValue.valueOf("0.25"))==0);
	}
	public void testFactoryRelation() throws DecompositionTreeException
	{
		List<String> t = new ArrayList<String>();
		for(int i=0;i<4;i++)
			t.add("Title"+i);

		List<SharpValue[]> s = new ArrayList<SharpValue[]>();
		s.add(new SharpValue[]{
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random())});
		s.add(new SharpValue[]{
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random())});
		s.add(new SharpValue[]{
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random())});
		s.add(new SharpValue[]{
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random()),
				   SharpValue.valueOf(""+Math.random()), SharpValue.valueOf(""+Math.random())});


		TransitiveClosureFactory f = new TransitiveClosureFactory();
		FuzzyRelation r = f.getRelationUsingHamming(t, t, s);
		SharpValue zero= SharpValue.valueOf("0");

		for (int row=0;row<r.getDimension()[0];row++)
			assertTrue(r.getCellValue(row, row).compareTo(zero) ==0);

		for (int row=0;row<r.getDimension()[0];row++)
		{
			for (int col=0;col<r.getDimension()[1];col++)
			{
				assertTrue(r.getCellValue(row, col).compareTo(r.getCellValue(col, row)) ==0);
			}
		}

		return;
	}

	public void testMinMaxComposition()
	{
		List<SharpValue> row = new ArrayList<SharpValue>();
		row.add(SharpValue.valueOf("0"));
		row.add(SharpValue.valueOf("0.26"));
		row.add(SharpValue.valueOf("0.34"));
		row.add(SharpValue.valueOf("0.44"));
		row.add(SharpValue.valueOf("0.29"));
		row.add(SharpValue.valueOf("0.34"));

		List<SharpValue> col = new ArrayList<SharpValue>();
		col.add(SharpValue.valueOf("0.26"));
		col.add(SharpValue.valueOf("0"));
		col.add(SharpValue.valueOf("0.31"));
		col.add(SharpValue.valueOf("0.33"));
		col.add(SharpValue.valueOf("0.43"));
		col.add(SharpValue.valueOf("0.4"));
		SharpValue result = new TransitiveClosureFactory().minMaxComposition(row, col);
		assertTrue(result.compareTo(SharpValue.valueOf("0.26")) == 0);

		col = new ArrayList<SharpValue>();
		col.add(SharpValue.valueOf("0"));
		col.add(SharpValue.valueOf("0"));
		col.add(SharpValue.valueOf("0.31"));
		col.add(SharpValue.valueOf("0.33"));
		col.add(SharpValue.valueOf("0.43"));
		col.add(SharpValue.valueOf("0.4"));
		result = new TransitiveClosureFactory().minMaxComposition(row, col);
		assertTrue(result.compareTo(SharpValue.valueOf("0")) == 0);

		col = new ArrayList<SharpValue>();
		col.add(SharpValue.valueOf("0.34"));
		col.add(SharpValue.valueOf("0.4"));
		col.add(SharpValue.valueOf("0.54"));
		col.add(SharpValue.valueOf("0.27"));
		col.add(SharpValue.valueOf("0.54"));
		col.add(SharpValue.valueOf("0"));
		result = new TransitiveClosureFactory().minMaxComposition(row, col);
		assertTrue(result.compareTo(SharpValue.valueOf("0.34")) == 0);
	}

	public void testGetRelationUsingMinMax() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A");
		title.add("B");
		title.add("C");
		title.add("D");
		title.add("E");

		FuzzyRelation r1 = new FuzzyRelation(title, title);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.9"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.9"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		r1.setCellValue(row, col++, SharpValue.valueOf("1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r1.setCellValue(row, col++, SharpValue.valueOf("1"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.4"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));
		r1.setCellValue(row, col++, SharpValue.valueOf("1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.4"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0"));

		assertTrue(r1.compareTo(r1.cloneObject()) == 0);

		FuzzyRelation results = new TransitiveClosureFactory().getRelationUsingMinMax(r1, r1.cloneObject());
		assertTrue(results.getCellValue(0,1).compareTo(SharpValue.valueOf("0.7"))==0);
		assertTrue(results.getCellValue(0,3).compareTo(SharpValue.valueOf("0.3"))==0);

		assertTrue(results.getCellValue(1,0).compareTo(SharpValue.valueOf("0.7"))==0);
		assertTrue(results.getCellValue(1,2).compareTo(SharpValue.valueOf("0.7"))==0);
		assertTrue(results.getCellValue(1,3).compareTo(SharpValue.valueOf("0.4"))==0);

		assertTrue(results.getCellValue(2,1).compareTo(SharpValue.valueOf("0.7"))==0);
		assertTrue(results.getCellValue(2,4).compareTo(SharpValue.valueOf("0.4"))==0);

		assertTrue(results.getCellValue(3,0).compareTo(SharpValue.valueOf("0.3"))==0);
		assertTrue(results.getCellValue(3,1).compareTo(SharpValue.valueOf("0.4"))==0);

		assertTrue(results.getCellValue(4,2).compareTo(SharpValue.valueOf("0.4"))==0);

		assertFalse(r1.compareTo(results) == 0);
		return;
	}

	public void testComparissonFuzzyRelation_lessThan() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A");
		title.add("B");
		FuzzyRelation r1 = new FuzzyRelation(title, title);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.4"));

		FuzzyRelation r2 = new FuzzyRelation(title, title);
		row=0;
		col=0;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		col=0;
		row++;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.5"));

		assertTrue(r1.compareTo(r2) < 0);

	}

	public void testComparissonFuzzy_greaterThan() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A");
		title.add("B");
		FuzzyRelation r1 = new FuzzyRelation(title, title);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.4"));

		FuzzyRelation r2 = new FuzzyRelation(title, title);
		row=0;
		col=0;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.6"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.5"));

		assertTrue(r2.compareTo(r1) > 0);

	}

	public void testComparissonFuzzy_Equal() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A");
		title.add("B");
		FuzzyRelation r1 = new FuzzyRelation(title, title);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.4"));

		FuzzyRelation r2 = new FuzzyRelation(title, title);
		row=0;
		col=0;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.3"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.4"));

		assertTrue(r2.compareTo(r1) == 0);

	}

	public void testComparissonFuzzy_Different() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A");
		title.add("B");
		FuzzyRelation r1 = new FuzzyRelation(title, title);
		int row=0;
		int col=0;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.5"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.6"));
		col=0;
		row++;
		r1.setCellValue(row, col++, SharpValue.valueOf("0.7"));
		r1.setCellValue(row, col++, SharpValue.valueOf("0.8"));

		FuzzyRelation r2 = new FuzzyRelation(title, title);
		row=0;
		col=0;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.1"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.2"));
		col=0;
		row++;
		r2.setCellValue(row, col++, SharpValue.valueOf("0.9"));
		r2.setCellValue(row, col++, SharpValue.valueOf("0.4"));

		try{
			assertTrue(r2.compareTo(r1) == 0);
		}catch(DecompositionTreeException e)
		{
			assertTrue("fuzzyRelations are different", true);
		}

	}

	public void testGetListMinMaxComposition() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A1");
		title.add("A2");
		title.add("A3");
		title.add("A4");
		title.add("A5");
		title.add("A6");
		TransitiveClosureFactory f = new TransitiveClosureFactory();
		List<String> l = new ArrayList<String>();
		l.add("0.1,0.8,0.3,1.0,0.1,0.0,1.0");
		l.add("0.3,0.8,0.1,1.0,0.0,1.0,0.7");
		l.add("0.7,1.0,0.0,1.0,0.8,0.3,0.7");
		l.add("0.1,0.8,0.7,0.0,0.1,1.0,0.3");
		l.add("0.6,1.0,0.0,0.7,0.8,0.0,1.0");
		l.add("0.0,0.3,0.5,0.1,0.1,0.5,0.8");
		List<SharpValue[]> ls = new DecompositionTreeAlgorithm(null).getSharpValueArrayList(l);

		FuzzyRelation r1 = f.getRelationUsingHamming(title, title, ls);
		List<FuzzyRelation> list = f.getMinMaxCompositionList(r1);
		assertTrue(list.size() == 4);

	}

	public void testGetMinMaxTransitiveClosure() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A1");
		title.add("A2");
		title.add("A3");
		title.add("A4");
		title.add("A5");
		title.add("A6");
		TransitiveClosureFactory f = new TransitiveClosureFactory();
		List<String> l = new ArrayList<String>();
		l.add("0.1,0.8,0.3,1.0,0.1,0.0,1.0");
		l.add("0.3,0.8,0.1,1.0,0.0,1.0,0.7");
		l.add("0.7,1.0,0.0,1.0,0.8,0.3,0.7");
		l.add("0.1,0.8,0.7,0.0,0.1,1.0,0.3");
		l.add("0.6,1.0,0.0,0.7,0.8,0.0,1.0");
		l.add("0.0,0.3,0.5,0.1,0.1,0.5,0.8");
		List<SharpValue[]> ls = new DecompositionTreeAlgorithm(null).getSharpValueArrayList(l);

		FuzzyRelation r1 = f.getRelationUsingHamming(title, title, ls);
		FuzzyRelation r2 = f.getMinMaxTransitiveClosure(r1);
		for (int col=0;col<r2.getDimension()[0];col++)
		{
			assertTrue(r2.getCellValue(col, col).compareTo(SharpValue.valueOf("0")) == 0);
		}
		assertTrue(r1.compareTo(r2) > 0);
		return;
	}

	public void testGetLevelTransitiveClosure() throws DecompositionTreeException
	{
		List<String> title = new ArrayList<String>();
		title.add("A1");
		title.add("A2");
		title.add("A3");
		title.add("A4");
		title.add("A5");
		title.add("A6");
		TransitiveClosureFactory f = new TransitiveClosureFactory();
		List<String> l = new ArrayList<String>();
		l.add("0.1,0.8,0.3,1.0,0.1,0.0,1.0");
		l.add("0.3,0.8,0.1,1.0,0.0,1.0,0.7");
		l.add("0.7,1.0,0.0,1.0,0.8,0.3,0.7");
		l.add("0.1,0.8,0.7,0.0,0.1,1.0,0.3");
		l.add("0.6,1.0,0.0,0.7,0.8,0.0,1.0");
		l.add("0.0,0.3,0.5,0.1,0.1,0.5,0.8");
		List<SharpValue[]> ls = new DecompositionTreeAlgorithm(null).getSharpValueArrayList(l);

		FuzzyRelation r1 = f.getRelationUsingHamming(title, title, ls);
		List<SharpValue> levels =new DecompositionTreeAlgorithm(null).getLevelsMinMaxTransitiveClosure(f.getMinMaxTransitiveClosure(r1));
		assertTrue(levels.size() == 6);
		return;
	}
}