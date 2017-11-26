package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.AndComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.Composite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.ConditionComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.DefuzzificationMethod;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRulesEngine;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySet;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzySetType;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.IfComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.InferenceMethod;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.NotComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.OrComposite;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.SharpValue;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.FuzzyOutput;
import edu.umd.msswe.ece591.diagnose.metrics.OOMetric;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class XmlReader  {

	
	private static String OO_DIAGNOSE = "oodiagnose";
	private static String DIAGNOSE_HOME = "home";
	private static String REPORT_OUTPUT = "output";
	private static String OUTPUT_FILE = "outputFile";
	private static String WORKING_DIRECTORY = "workingDirectory";
	private static String SOURCE_PATH = "sourcePath";
	private static String METRIC_VALUES = "values";

	 
	 private static String METRICS = "metrics";
	private static String METRIC_ID = "id";
	private static String CLASS_NAME = "class-name";
	private static String METRIC_NAME = "name";
	private static String METRIC_DEFINITION = "definition";
	private static String METRIC_SOLUTION = "solution";
	
	private static String FUZZY_OUTPUT = "fuzzy-output";
	private static String FUZZY_OUTPUT_LABEL = "id";
	private static String FUZZY_OUTPUT_MIN = "min";
	private static String FUZZY_OUTPUT_MAX = "max";
	private static String FUZZY_SET_TYPE = "fuzzy-set-type";
	private static String FUZZY_SETS = "fuzzy-sets";
	private static String FUZZY_SET_ID = "id";
	private static String FUZZY_SET_LABEL = "label";
	private static String FUZZY_SET_VALUE = "value";
	private static String FUZZY_SET_TYPE_ID = "type";
	private static String FUZZY_SET_MAX = "max";

	private static String FUZZY_RULES = "fuzzy-rules-app";
	private static String FUZZY_RULES_CLASS = "fuzzy-rules-class";

	private Document doc;
	
	/**
	 * 
	 * @param xmlFile
	 * @throws ConfigurationException
	 */
	public XmlReader(File xmlFile) throws ConfigurationException
	{
		this.init(xmlFile);
	}
	
	/**
	 * 
	 * @param xmlFile
	 * @throws ConfigurationException
	 */
	private void init(File xmlFile) throws ConfigurationException
	{
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(xmlFile);
			doc.getDocumentElement().normalize();

		} catch (SAXException e) {
			throw new ConfigurationException(e);
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}	
	}
	
	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public Properties readDiagnoseConfig()throws ConfigurationException
	{
		return this.processOOSystem(doc.getElementsByTagName(OO_DIAGNOSE));
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public Hashtable<String, OOMetric> readOOMetrics()throws ConfigurationException
	{
		return this.processMetrics(doc.getElementsByTagName(METRICS));
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public List<FuzzyOutput> readFuzzyOutput()throws ConfigurationException
	{
		return this.processFuzzyOutputs(doc.getElementsByTagName(FUZZY_OUTPUT));
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public Hashtable<String, FuzzySet> readFuzzySets()throws ConfigurationException
	{
		return this.processFuzzySets(doc.getElementsByTagName(FUZZY_SETS));
	}
	
	/**
	 * New logic for fuzzy rules
	 * @return
	 * @throws ConfigurationException
	 */
	public List<FuzzyRule> readFuzzyRules() throws ConfigurationException
	{
		return this.processFuzzyRules(doc.getElementsByTagName(FUZZY_RULES));
	}

	public List<FuzzyRule> readFuzzyRulesClass()throws ConfigurationException
	{
		return this.processFuzzyRules(doc.getElementsByTagName(FUZZY_RULES_CLASS));
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public FuzzyRulesEngine createFuzzyEngine() throws ConfigurationException
	{
		return this.processEngine(doc.getElementsByTagName("fuzzy-rules-engine"));
	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private FuzzyRulesEngine processEngine(NodeList parentList) throws ConfigurationException
	{
		FuzzyRulesEngine engine = null;
		
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node "+OO_DIAGNOSE);
		
		Node node = parentList.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE)
	    {
			Element elmSet = (Element) node;
			engine = createFuzzyRulesEngine(elmSet);
	    }
			
		return engine;
	}
	
	protected FuzzyRulesEngine createFuzzyRulesEngine(Element elmSet) throws ConfigurationException
	{
		 FuzzyRulesEngine engine = null;
		 
		 String className = elmSet.getAttribute("class-name");
		
		 try{
		 	Class c = Class.forName(className);
		 	engine = (FuzzyRulesEngine)c.newInstance();
		 } catch (Exception e) 
		 {
			e.printStackTrace();
			throw new ConfigurationException(e);
		 }
		 return engine;
	}
	
	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public DefuzzificationMethod createDefuzzificationMethod() throws ConfigurationException
	{
		return this.processDefMethod(doc.getElementsByTagName("defuzzification-method"));
	}

	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public InferenceMethod createInferenceMethod() throws ConfigurationException
	{
		return this.processInfMethod(doc.getElementsByTagName("inference-method"));
	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private DefuzzificationMethod processDefMethod(NodeList parentList) throws ConfigurationException
	{
		DefuzzificationMethod method = null;
		
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node defuzzification ");
		
		Node node = parentList.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE)
	    {
			Element elmSet = (Element) node;
			method = createDefMethod(elmSet);
	    }
			
		return method;
	}
	
	protected DefuzzificationMethod createDefMethod(Element elmSet) throws ConfigurationException
	{
		DefuzzificationMethod method = null;
		 
		 String className = elmSet.getAttribute("class");
		
		 try{
		 	Class c = Class.forName(className);
		 	method = (DefuzzificationMethod)c.newInstance();
		 } catch (Exception e) 
		 {
			e.printStackTrace();
			throw new ConfigurationException(e);
		 }
		 return method;
	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private InferenceMethod processInfMethod(NodeList parentList) throws ConfigurationException
	{
		InferenceMethod method = null;
		
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node defuzzification ");
		
		Node node = parentList.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE)
	    {
			Element elmSet = (Element) node;
			method = createInfMethod(elmSet);
	    }
			
		return method;
	}
	
	protected InferenceMethod createInfMethod(Element elmSet) throws ConfigurationException
	{
		InferenceMethod method = null;
		 
		 String className = elmSet.getAttribute("class");
		
		 try{
		 	Class c = Class.forName(className);
		 	method = (InferenceMethod)c.newInstance();
		 } catch (Exception e) 
		 {
			e.printStackTrace();
			throw new ConfigurationException(e);
		 }
		 return method;
	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private Hashtable<String, OOMetric> processMetrics(NodeList parentList) throws ConfigurationException
	{
		Hashtable<String, OOMetric> metrics = new Hashtable<String, OOMetric>();
		
		//metrics tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node for "+METRICS);
		NodeList nodeList = parentList.item(0).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			 Node fstNode = nodeList.item(i);
			 if (fstNode.getNodeType() == Node.ELEMENT_NODE)
			 {
				 Element elmMetric = (Element) fstNode;
				 
				 OOMetric m = new OOMetric();
				 m.setId(elmMetric.getAttribute(METRIC_ID));
				 m.setClassName(elmMetric.getAttribute(CLASS_NAME));
				 String tagValue = getTagValue(elmMetric.getElementsByTagName(METRIC_DEFINITION));
				 if (tagValue != null)
					 m.setDefinition(tagValue);
				 tagValue = getTagValue(elmMetric.getElementsByTagName(METRIC_NAME));
				 if (tagValue != null)
					 m.setName(tagValue);
				 tagValue = getTagValue(elmMetric.getElementsByTagName(METRIC_SOLUTION));
				 if (tagValue != null)
					 m.setSolution(tagValue);
				 tagValue = getTagValue(elmMetric.getElementsByTagName(METRIC_VALUES));
				 if (tagValue != null)
					 m.setValues(tagValue);
				 
				 metrics.put(m.getId(), m);
			 }
		}

		return metrics;
	}
	
	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private Properties processOOSystem(NodeList parentList) throws ConfigurationException
	{
		Properties prop = new Properties();
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node "+OO_DIAGNOSE);
		
		Node node = parentList.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE)
	    {
			 Element elmSet = (Element) node;
			 prop.put(DIAGNOSE_HOME, elmSet.getAttribute(DIAGNOSE_HOME));
			 prop.put(REPORT_OUTPUT, elmSet.getAttribute(REPORT_OUTPUT));
			 prop.put(OUTPUT_FILE, elmSet.getAttribute(OUTPUT_FILE));
			 prop.put(WORKING_DIRECTORY, elmSet.getAttribute(WORKING_DIRECTORY));
			 prop.put(SOURCE_PATH, elmSet.getAttribute(SOURCE_PATH));
		}
		return prop;

	}
	
	/**
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public void readFuzzySetTypes()throws ConfigurationException
	{
		this.processFuzzySetsTypes(doc.getElementsByTagName(FUZZY_SET_TYPE));
	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private void processFuzzySetsTypes(NodeList parentList) throws ConfigurationException
	{
		List <FuzzySetType> types = new ArrayList <FuzzySetType>();
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node "+FUZZY_SET_TYPE);
		
		NodeList nodeList = parentList.item(0).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			 Node fstNode = nodeList.item(i);
			 if (fstNode.getNodeType() == Node.ELEMENT_NODE)
			 {
				 Element elmSet = (Element) fstNode;
				 FuzzySetType s = new FuzzySetType(elmSet.getAttribute(FUZZY_SET_TYPE_ID));
				 s.setClassName(elmSet.getAttribute(CLASS_NAME));
				 types.add(s);
			 }
		}
		FuzzySetFactory.instance().setTypes(types);

	}
	
	private List<FuzzyOutput> processFuzzyOutputs(NodeList parentList) throws ConfigurationException
	{
		List <FuzzyOutput> outputs = new ArrayList <FuzzyOutput>();
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node "+FUZZY_OUTPUT);
		
		NodeList nodeList = parentList.item(0).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			 Node fstNode = nodeList.item(i);
			 if (fstNode.getNodeType() == Node.ELEMENT_NODE)
			 {
				 Element elmSet = (Element) fstNode;
				 FuzzyOutput s = new FuzzyOutput(elmSet.getAttribute(FUZZY_OUTPUT_LABEL));
				 s.setMinValue(elmSet.getAttribute(FUZZY_OUTPUT_MIN));
				 s.setMaxValue(elmSet.getAttribute(FUZZY_OUTPUT_MAX));
				 outputs.add(s);
			 }
		}
		return outputs;

	}

	/**
	 * 
	 * @param parentList
	 * @return
	 * @throws ConfigurationException
	 */
	private Hashtable<String, FuzzySet> processFuzzySets(NodeList parentList) throws ConfigurationException
	{
		Hashtable<String, FuzzySet> sets = new Hashtable<String, FuzzySet>();
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node "+FUZZY_SETS);
		
		NodeList nodeList = parentList.item(0).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			 Node fstNode = nodeList.item(i);
			 if (fstNode.getNodeType() == Node.ELEMENT_NODE)
			 {
				 Element elmSet = (Element) fstNode;
				 
				 String attr = elmSet.getAttribute(FUZZY_SET_TYPE_ID);
				 FuzzySet m = FuzzySetFactory.instance().createFuzzySetByType(attr);
				 
				 m.setMax(SharpValue.valueOf(elmSet.getAttribute(FUZZY_SET_MAX)));
				 m.setId(elmSet.getAttribute(FUZZY_SET_ID));
				 m.setLabel(elmSet.getAttribute(FUZZY_SET_LABEL));
				 m.setStringValues(elmSet.getAttribute(FUZZY_SET_VALUE));
				 sets.put(m.getId(), m);
			 }
		}
		return sets;
	}

	/**
	 * 
	 * @param parentList1
	 * @param label
	 * @return
	 * @throws ConfigurationException
	 */
	protected List<FuzzyRule> processFuzzyRules(NodeList parentList) throws ConfigurationException
	{
		List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
		//fuzzy-sets tag should be only one
		if (parentList.getLength() > 1)
			throw new ConfigurationException("There should be only one node ");
		Node parentNode = parentList.item(0);
		if (parentNode != null)
		{
			NodeList nodeList = parentNode.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) 
			{	
				Node n = nodeList.item(i);
				if (n.getNodeName() != null && n.getNodeName().equals("fuzzy-rule"))
				{
					Element e = (Element)n;
					FuzzyRule fuzzyRule = new FuzzyRule(e.getAttribute("id"));
					
					//get 'if' and 'then' tags
					NodeList childrenList = n.getChildNodes();
					for(int j = 0; j < childrenList.getLength(); j++)
					{
						Node node = childrenList.item(j);
						if (node.getNodeName() != null && node.getNodeName().equals("if"))
						{
							Composite ifComposite = new IfComposite();
							this.processChildrenCompositeNodes(rules, ifComposite, node);
							fuzzyRule.setIfCondition(ifComposite);
						}
						else if (node.getNodeName() != null && node.getNodeName().equals("then"))
						{
							Element thenNode = (Element)node;
							fuzzyRule.setThenCondition(thenNode.getAttribute("fuzzy-set-id"));
						}
					}
					rules.add(fuzzyRule);
				}
			}
		}
			
		return rules;
	}
	
	/**
	 * 
	 * @param parent
	 * @param parentNode
	 */
	protected void processChildrenCompositeNodes(List<FuzzyRule> rules, Composite parent, Node parentNode)
	throws ConfigurationException
	{
		NodeList list = parentNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			 Node fstNode = list.item(i);
			 String nodeName = fstNode.getNodeName(); 
			 if (nodeName != null )
			 {
				 if (nodeName.equals("condition"))
				 {
					Composite child = processConditionNode((Element)fstNode);
					parent.addCompositeCondition(child);
				 }
				 else if (nodeName.equals("and"))
				 {
					 Composite andComposite = new AndComposite();
					 parent.addCompositeCondition(andComposite);
					 processChildrenCompositeNodes(rules, andComposite, fstNode);
				 }
				 else if (nodeName.equals("or"))
				 {
					 Composite orComposite = new OrComposite();
					 parent.addCompositeCondition(orComposite);
					 processChildrenCompositeNodes(rules, orComposite, fstNode);
				 }
				 else if (nodeName.equals("not"))
				 {
					NotComposite notComposite = new NotComposite();
					parent.addCompositeCondition(notComposite);
					String attValue = getAttributeTag("fuzzy-rule-id", fstNode);
					if (attValue != null)
						notComposite.addCompositeCondition(this.getComposite(attValue, rules));
					else
						processChildrenCompositeNodes(rules, notComposite, fstNode);	
				 }
			 }
		}
	}
	
	/**
	 * 
	 * @param conditionNode
	 * @return
	 */
	protected Composite processConditionNode(Element conditionNode)
	{
		String metricId = conditionNode.getAttribute("metric-id");
		String operation = conditionNode.getAttribute("operation");
		String fuzzySetid = conditionNode.getAttribute("fuzzy-set-id");
		Composite conditionComposite = new ConditionComposite(metricId, operation, fuzzySetid);
		return conditionComposite;
	}

	/**
	 * 
	 * @param fstNmElmntLst
	 * @return
	 */
	private String getTagValue(NodeList fstNmElmntLst)
	{
		if (fstNmElmntLst == null || fstNmElmntLst.item(0) == null)
			return null;
        NodeList fstNm = fstNmElmntLst.item(0).getChildNodes();
        if (fstNm.item(0) != null)
        	return ((Node) fstNm.item(0)).getNodeValue();
        else 
        	return null;
	}

	/**
	 * 
	 * @param fstNmElmntLst
	 * @return
	 */
	private String getAttributeTag(String attribute, Node fstNode)
	{
		Element elmMetric = (Element)fstNode;
		String attValue = elmMetric.getAttribute(attribute);
		if (attValue != null && attValue.length()>0)
			return attValue;
		else
			return null;
	}
	
	/**
	 * 
	 * @param attribute
	 * @param rules
	 * @return
	 * @throws ConfigurationException
	 */
	private Composite getComposite(String attribute, List<FuzzyRule> rules) throws ConfigurationException
	{
		Composite conditionComposite = null;
		for(FuzzyRule rule: rules)
		{
			if (rule.getId().equals(attribute))
			{
				conditionComposite = rule.getIfCondition();
				break;
			}
		}
		if(conditionComposite == null)
			throw new ConfigurationException("rule "+attribute +" should be previously defined before using it");
		
		return conditionComposite;
	}

}