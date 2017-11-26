package edu.umd.msswe.ece591.diagnose.tools;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class GenerateXmlRules {
	
	private String compoundValueMetrics;
	private ArrayList<String> listMetricIds;
	private ArrayList<String> listMetricGroups;
	
	private static final String BEGINNING_RULE="\t\t";
	private static final String BEGINNING_CONDITION="\t\t\t";
	
	/**
	 * 
	 * @param compoundValueMetrics
	 */
	public GenerateXmlRules(String compoundValueMetrics)
	{
		this.compoundValueMetrics = compoundValueMetrics;
		listMetricIds = new ArrayList<String>(); 
		listMetricGroups = new ArrayList<String>();
	}
	
	/**
	 * get the list of metrics from title
	 * @param titleValue
	 */
	public void setListMetricIds(String titleValue)
	{
		StringTokenizer st = new StringTokenizer(titleValue, ","); 
		
		int len = st.countTokens();
		int index = -1;
		
		while(st.hasMoreTokens())
		{
			index++;
			String metricValue = st.nextToken();
			if (index == 0) continue;
			
			if (index < (len-2))
				listMetricIds.add(metricValue.toUpperCase());
			
		}
	}
		
	/**
	 * get the list of metrics from title
	 * @param titleValue
	 */
	public void setListMetricGroups(String metricGroupsStr)
	{
		StringTokenizer st = new StringTokenizer(metricGroupsStr, ";"); 
		while(st.hasMoreTokens())
		{
			String metricValue = st.nextToken();
			listMetricGroups.add(metricValue.toUpperCase());
		}
	}
	/**
	 * return complete fuzzy rules
	 * 
	 * @param cellValue
	 * @param length
	 * @param listMetricIds
	 * @return
	 * @throws ConfigurationException
	 */
	public String processFuzzyRules(String lineRecord) throws ConfigurationException
	{
		String label = "fuzzyrule";
		StringTokenizer st = new StringTokenizer(lineRecord, ",");
		if (st.countTokens() != listMetricIds.size()+3)
			throw new ConfigurationException("Expected record with length"+(listMetricIds.size()+3)+" but the count is "+st.countTokens()+" "+lineRecord);
		
		String ruleId=label+st.nextToken();
		
		ArrayList<String> l = new ArrayList<String>();
		for (String metric: listMetricIds)
		{
			l.add(this.processCellValue(metric, st.nextToken()));
		}
		
		Map<String, String> groups = getListMetricGroups(l);
		l.add(this.processGroups(groups));
		l.add(this.processCellValueAdditionalRules(label, st.nextToken()));
		
		return this.getfuzzyRuleCondition(ruleId, this.getIfRule(this.getAndOrCondition(l, "and")), this.getThenCondition(st.nextToken()));
		
	}
	
	/**
	 * 
	 * @param listMetricIds
	 * @return
	 */
	protected Map<String, String> getListMetricGroups(ArrayList<String> listConditions)
	{
		Map<String, String> lg = new HashMap<String, String>();
		ArrayList<String> listRemoveConditions = new ArrayList<String>();
		
		for(String commaSeparatedGroup:listMetricGroups)
		{
			for (String condition: listConditions)
			{
				if (this.isElementInList(commaSeparatedGroup, condition))
				{
					listRemoveConditions.add(condition);
					String value = lg.get(commaSeparatedGroup);
					
					if (value != null)
						value += condition;
					else
						value = condition;
					
					lg.put(commaSeparatedGroup, value);
				}
			}
		}
		listConditions.removeAll(listRemoveConditions);
		
		return lg;
	}
	
	
	/**
	 * 
	 * @param group
	 * @param condition
	 * @return
	 */
	protected boolean isElementInList(String group, String condition)
	{
		StringTokenizer st = new StringTokenizer(group, ",");
		
		while (st.hasMoreTokens())
		{
			if (condition.contains(st.nextToken()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param groups
	 * @return
	 */
	protected String processGroups(Map<String, String> groups)
	{
		Collection values = groups.values();
		Iterator it = values.iterator();
		StringBuffer sb = new StringBuffer(); 
		while(it.hasNext())
		{
			sb.append(BEGINNING_CONDITION+"<or>"+"\n");
			sb.append(it.next());
			sb.append(BEGINNING_CONDITION+"</or>"+"\n");
		}
		return sb.toString();
	}
	
	/**
	 * get additional rules 
	 * 
	 * <not fuzzy-rule-id="ruleclass2"/>
	 * <not fuzzy-rule-id="ruleclass3"/>
	 * 
	 * 
	 * @param metricId
	 * @param cellValue
	 * @return
	 */
	protected String processCellValueAdditionalRules(String label, String cellValue)
	{
		StringTokenizer st = new StringTokenizer(cellValue, ";");

		ArrayList<String> listFuzzyRuleIds = new ArrayList<String>(); 
		while(st.hasMoreTokens())
		{
			String addRule = st.nextToken();
			
			if (addRule.trim().length() > 0)
				listFuzzyRuleIds.add(label+addRule);
		}
		if(listFuzzyRuleIds.size() >0)
			return this.getNotCondition(listFuzzyRuleIds);
		
		return "";
	}

	/**
	 * 
	 * return a cell value
	 * 	<or>
	 * 		<condition metric-id="COF" operation="is" fuzzy-set-id="high-cof"/>
	 *		<condition metric-id="COF" operation="is" fuzzy-set-id="medium-cof"/>
	 *	</or>
	 *
	 * @param metricId
	 * @param cellValue
	 * @return
	 */
	protected String processCellValue(String metricId, String cellValue)
	{
		StringTokenizer st = new StringTokenizer(cellValue, "|");
		
		ArrayList<String> listFuzzyConditions = new ArrayList<String>(); 
		while(st.hasMoreTokens())
		{
			String singleValue = st.nextToken();
			for(String fuzzySetId: this.getFuzzySetId(metricId, singleValue))
				listFuzzyConditions.add(this.getCondition(metricId, "is", fuzzySetId));

		}
		
		int length = listFuzzyConditions.size();
		if ( length == 1)
			return listFuzzyConditions.get(0);
		
		else if (length > 1)
			return getAndOrCondition(listFuzzyConditions, "or");
		
		else
			return null;
		
	}
	
	/**
	 * get not condition
	 * <not fuzzy-rule-id="ruleclass3"/>
	 */
	protected String getNotCondition(ArrayList<String> listFuzzyRuleId)
	{
		StringBuffer sb = new StringBuffer();
		for (String fuzzySet: listFuzzyRuleId)
			sb.append(BEGINNING_CONDITION + "<not fuzzy-rule-id='"+fuzzySet+"'/>\n");
		return sb.toString();
	}
	
	/**
	 * get if rule
	 * 
	 * 
	 * @param condition
	 * @param thenCondition
	 * @return
	 */
	
	protected String getIfRule(String condition)
	{
		return BEGINNING_CONDITION + "<if>\n"
									+condition+
									BEGINNING_CONDITION+"</if>\n";
	}

	/**
	 * get and or condition
		<or><condition metric-id="AHF" operation="is" fuzzy-set-id="high-ahf"/>
			<condition metric-id="AHF" operation="is" fuzzy-set-id="medium-ahf"/>
		</or>
		<and><condition metric-id="AHF" operation="is" fuzzy-set-id="high-ahf"/>
			<condition metric-id="AHF" operation="is" fuzzy-set-id="medium-ahf"/>
		</and>

	 * @param listConditions
	 * @param type
	 * @return
	 */
	protected String getAndOrCondition(ArrayList<String> listConditions, String type)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(BEGINNING_CONDITION + "<"+type+">\n");
		for (String cond: listConditions)
			sb.append(cond);

		sb.append(BEGINNING_CONDITION + "</"+type+">\n");
		return sb.toString();
	}
	
	/**
	 * get a condition
	 * <condition metric-id="AHF" operation="is" fuzzy-set-id="medium-ahf"/>
	 * 
	 * @param metricId
	 * @param operation
	 * @param fuzzySetId
	 * @return
	 */
	protected String getCondition(String metricId, String operation, String fuzzySetIdValue)
	{
		return BEGINNING_CONDITION + "<condition metric-id='"+metricId.toUpperCase()+"' operation='"+operation+"' fuzzy-set-id='"+fuzzySetIdValue.toLowerCase()+"' />\n";
	}


	/**
	 * get the fuzzy rule header
	 * <fuzzy-rule id="ruleclass11">
	 * @return
	 */
	public String getfuzzyRuleCondition(String ruleId, String ifRule, String thenRule)
	{
		return BEGINNING_RULE + "<fuzzy-rule id='"+ruleId+"'>\n"+
			ifRule+thenRule+BEGINNING_RULE+"</fuzzy-rule>\n";
	}
	/**
	 * then then condition
	 * <then fuzzy-set-id="high" />
	 * 
	 * @param fuzzySetIdValue
	 * @return
	 */
	protected String getThenCondition(String fuzzySetIdValue)
	{
		return BEGINNING_CONDITION + "<then fuzzy-set-id='"+fuzzySetIdValue+"' />\n";
	}
	
	/**
	 * get a single of compound fuzzy value
	 * H = high, very-low
	 * M = medium, low
	 * N=normal
	 * @param metricId
	 * @param type
	 * @return
	 */
	protected ArrayList<String> getFuzzySetId(String metricId, String type)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		boolean iscompoundMetric = (compoundValueMetrics.indexOf(metricId) >=0);
		if (type.equalsIgnoreCase("H"))
		{
			list.add("high-"+metricId.toLowerCase());
			if (iscompoundMetric)
				list.add("very-low-"+metricId.toLowerCase());
		}
		else if (type.equalsIgnoreCase("M"))
		{
			list.add("medium-"+metricId.toLowerCase());
			if (iscompoundMetric)
				list.add("low-"+metricId.toLowerCase());
		}
		else if (type.equalsIgnoreCase("N"))
			list.add("normal-"+metricId.toLowerCase());

		return list; 
	}
	
	/*
	 * 
	NA C:\health\bie\OODiagnose\class2.csv LOC,WMC,RFC;DIT,NOC
	MIF,MHF,POF C:\health\bie\OODiagnose\app2.csv MIF,AIF;MHF,AHF;DIT,NOC
	*/
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws ConfigurationException
	{
		String compoundMetrics = args[0];
		String rulesFile = args[1];
		String metricsGroups = args[2]; 
		
		GenerateXmlRules generator = new GenerateXmlRules(compoundMetrics);
		generator.setListMetricGroups(metricsGroups);
		
		StringBuffer sb = new StringBuffer(); 
		DataInputStream dis = null;
		boolean firstLine = true;
		try {

		      // Here BufferedInputStream is added for fast reading.
		      dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(rulesFile))));

		      // dis.available() returns 0 if the file does not have more lines.
		      while (dis.available() != 0) {

		    	  if(firstLine)
		    	  {
		    		  	generator.setListMetricIds(dis.readLine());
		    	  		firstLine = false;
		    	  }else
		    	  {
		    		  sb.append(generator.processFuzzyRules(dis.readLine()));
		    	  }
		      }

		      // dispose all the resources after using them.
		      dis.close();

	    } catch (Exception e) {
	      e.printStackTrace();
	      System.exit(1);
	    }
	    finally
	    {
	    	try{if(dis != null)
	    		dis.close();} catch(IOException ioe){}	    	
	    }
	    
	    System.out.println(sb.toString());
	    System.exit(0);
	}

}


