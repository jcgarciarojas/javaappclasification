package edu.umd.msswe.ece591.diagnose.fuzzyrules;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public abstract class Line {

	/**
	 * given a line and a X point it finds the Y point  
	 * @param x
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public Double getIntersecionYPoint(Double x, Double x1, Double y1, Double x2, Double y2)
	{
		Double y = ( (y2-y1)/(x2-x1) * (x-x1)) + y1;
		
		if (y.isInfinite() || y.isNaN())
			throw new NumberFormatException ("Invalid double value calculated");

		return y;
	}

	/**
	 * given a line and a X point it finds the Y point  
	 * @param x
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public Double getIntersecionXPoint(Double y, Double x1, Double y1, Double x2, Double y2)
	{
		Double x = ( (y-y1)/(y2-y1) * (x2-x1) ) + x1;

		if (x.isInfinite() || x.isNaN())
			throw new NumberFormatException ("Invalid double value calculated");
		
		return x;
	}

}
