package diagnose.fuzzysystem;

import diagnose.configuration.*;
import edu.umd.msswe.ece591.diagnose.configuration.*;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.exception.DecompositionTreeException;
import edu.umd.msswe.ece591.diagnose.exception.FuzzySystemException;
import edu.umd.msswe.ece591.diagnose.fuzzyrules.FuzzyRule;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.DiagnoseFacade;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.Engine;
import edu.umd.msswe.ece591.diagnose.fuzzysystem.GUIDiagnoseBean;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.reports.FuzzyEngineReport;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestDiagnoseFacade extends TestCase {
	
	private DiagnoseFacade facade;
	private ConfigurationManager confManager;
	private GUIDiagnoseBean bean;

	public void setUp() throws Exception
	{
		confManager = mock(ConfigurationManager.class);
		facade = new DiagnoseFacade(confManager);
		facade.setGUIDiagnoseBean(bean);
		return;
	}

	public void tearDown()
	{
		facade = null;
		confManager = null;
		return;
	}

	public void testLoadConfigError()throws ConfigurationException, DecompositionTreeException
	{

		try {
			facade.diagnose();
			assertTrue("SystemConfiguration not validated",false);
		} catch (FuzzySystemException e) {
			assertTrue("SystemConfiguration validated",true);
		}
	}

	public void testDiagnoseConfigLoadedDiagnoseHappyPath()throws ConfigurationException, DecompositionTreeException
	{

		try {
			GUIDiagnoseBean subjectBean = new GUIDiagnoseBean();
			subjectBean.sourceDirectories = "";
			subjectBean.workingDirectory = "";
			subjectBean.additionalClassPath = "";
			subjectBean.filter = "";
			facade.setGUIDiagnoseBean(subjectBean);

			SystemConfiguration config = mock(SystemConfiguration.class);
			when(confManager.getConfig()).thenReturn(config);
			JavaApplication javaApplication = mock(JavaApplication.class);
			when(confManager.getStructure(facade.getStructureParameters())).thenReturn(javaApplication);
			Engine fuzzyEngine = mock(Engine.class);
			when(config.getEngine(javaApplication, null)).thenReturn(fuzzyEngine);

			List<FuzzyRule> rules = new ArrayList<FuzzyRule>();
			when(config.getFuzzyRules()).thenReturn(rules);
			List<FuzzyEngineReport> report = new ArrayList<FuzzyEngineReport>();
			when(fuzzyEngine.execute(rules, rules)).thenReturn(report);

			facade.loadConfiguration();
			facade.loadCodeStructure();
			facade.diagnose();
			assertTrue("report manager should be initialized after diagnose is performed", facade.getReportManager() != null);
			assertTrue(facade.createTreeReport() != null);

		} catch (FuzzySystemException e) {
			assertTrue("validation something went wrong" +e ,false);
		}
	}

	public void testFacadeIntegrationTest() throws Exception
	{
		String workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
		JUnitTestApplicationCompiler.createTestFile(workingDirectory);
		List<File> dirs = new ArrayList<File>();
		dirs.add(new File(workingDirectory));

		File xmlFile = new File("test_config_system.xml");
		JavaApplicationProcessor processor = new JavaApplicationProcessor();
		ApplicationCompiler compiler = new ApplicationCompiler(processor);
		StructureReader m_StructureReader = new StructureReader();
		XmlReader xmlReader = new XmlReader(xmlFile);
		Builder structureBuilder = new StructureBuilder(compiler, m_StructureReader);
		Builder configBuilder = new ConfigBuilder(xmlReader);
		ConfigurationManager confManager = new ConfigurationManager(structureBuilder, configBuilder);
		DiagnoseFacade facade = new DiagnoseFacade(confManager);
		facade.loadConfiguration();

		GUIDiagnoseBean subjectBean = new GUIDiagnoseBean();
		subjectBean.sourceDirectories = JUnitTestApplicationCompiler.getWorkingDirectory();
		subjectBean.workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
		subjectBean.outputLocation = JUnitTestApplicationCompiler.getWorkingDirectory()+"/test.xml";
		subjectBean.additionalClassPath = JUnitTestApplicationCompiler.getWorkingDirectory();
		subjectBean.projectName = "";
		subjectBean.filter = "";
		facade.setGUIDiagnoseBean(subjectBean);
		facade.loadCodeStructure();
		facade.diagnose();
		assertTrue("report manager should be initialized after diagnose is performed", facade.getReportManager() != null);		
		JUnitTestApplicationCompiler.deleteTestFile(workingDirectory);
	}
}
