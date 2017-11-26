package edu.umd.msswe.ece591.diagnose.reports;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public abstract class Report {

	public static final String NEW_LINE ="\n";
	public static final String DASH =" - ";
	public static final String COMMA =",";
	public static final String SPACE =" ";
	public static enum OutputType
	{
		TO_XML, TO_TEXT, TO_HTML
	}
	
	public String toReport(OutputType type)
	{
		switch (type) {
			case TO_XML:
				return this.toXml();
			case TO_HTML:
				return this.toHtml();
			default:
				return this.toText();
		}

	}
	
	public abstract String toXml();
	public abstract String toHtml();
	public abstract String toText();
	
}
