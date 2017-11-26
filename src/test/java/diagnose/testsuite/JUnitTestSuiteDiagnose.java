package diagnose.testsuite;


import diagnose.configuration.*;
import diagnose.fuzzyrules.*;
import diagnose.fuzzysystem.JUnitTestDiagnoseFacade;
import diagnose.metrics.*;
import diagnose.reports.JUnitTestDecompositionTreeAlgorithm;
import diagnose.reports.JUnitTestFuzzyRelation;
import diagnose.reports.JUnitTestTransitiveClosureFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 *
 */

public class JUnitTestSuiteDiagnose {

	public static void main(String[] args) {

		TestRunner.run(suite());
	}
	public static Test suite() {

		TestSuite suite = new TestSuite("All Diagnose JUnit Tests");
		
		//package edu.umd.msswe.ece591.diagnose.configuration
		suite.addTestSuite(JUnitTestJavaClassInfoFactory.class);
		suite.addTestSuite(JUnitTestConfigBuilder.class);
		suite.addTestSuite(JUnitTestConfigurationManager.class);
		suite.addTestSuite(JUnitTestStructureBuilder.class);
		suite.addTestSuite(JUnitTestStructureReader.class);
		suite.addTestSuite(JUnitTestXmlReader.class);
		suite.addTestSuite(JUnitTestFuzzySetFactory.class);
		suite.addTestSuite(JUnitTestApplicationCompiler.class);

		//package edu.umd.msswe.ece591.diagnose.fuzzysystem
		suite.addTestSuite(JUnitTestDiagnoseFacade.class);
		suite.addTestSuite(JUnitTestFuzzyEngine.class);
		suite.addTestSuite(JUnitTestDecompositionTreeAlgorithm.class);
		suite.addTestSuite(JUnitTestFuzzyRelation.class);
		suite.addTestSuite(JUnitTestTransitiveClosureFactory.class);
		/*suite.addTestSuite(JUnitTestFuzzyEngine.class);
		suite.addTestSuite(JUnitTestFuzzyRule.class);
		suite.addTestSuite(JUnitTestFuzzyRulesEngine.class);*/
		
		//package edu.umd.msswe.ece591.diagnose.main
		
		//package edu.umd.msswe.ece591.diagnose.fuzzyrules
		suite.addTestSuite(JUnitTestRulesEngine.class);
		suite.addTestSuite(JUnitTestAlgorithmMoM.class);
		suite.addTestSuite(JUnitTestDefuzzificationMethod.class);
		suite.addTestSuite(JUnitTestFuzzySet.class);
		suite.addTestSuite(JUnitTestInferenceAlgorithm.class);
		suite.addTestSuite(JUnitTestLine.class);
		suite.addTestSuite(JUnitTestRuleBasedEngine.class);
		suite.addTestSuite(JUnitTestMatchingDegree.class);
		
		//package edu.umd.msswe.ece591.diagnose.metrics
		suite.addTestSuite(JUnitTestMetricsEngine.class);
		suite.addTestSuite(JUnitTestWMC.class);
		suite.addTestSuite(JUnitTestCBO.class);
		suite.addTestSuite(JUnitTestDIT.class);
		suite.addTestSuite(JUnitTestNOC.class);
		suite.addTestSuite(JUnitTestRFC.class);

		suite.addTestSuite(JUnitTestAHF.class);
		suite.addTestSuite(JUnitTestAIF.class);
		suite.addTestSuite(JUnitTestLCOM2.class);
		suite.addTestSuite(JUnitTestMHF.class);
		suite.addTestSuite(JUnitTestMIF.class);
		suite.addTestSuite(JUnitTestPOF.class);
/*
 * 		//14
		suite.addTestSuite(JUnitTestCOF.class);
		suite.addTestSuite(JUnitTestPOF.class);
		suite.addTestSuite(JUnitTestLOC.class);
		suite.addTestSuite(JUnitTestNPM.class);
		*/
		//package edu.umd.msswe.ece591.diagnose.reports
		suite.addTestSuite(JUnitTestDecompositionTreeAlgorithm.class);
		suite.addTestSuite(JUnitTestFuzzyRelation.class);
		//suite.addTestSuite(JUnitTestFuzzyReport.class);
		suite.addTestSuite(JUnitTestTransitiveClosureFactory.class);
		return suite;
	}
}
