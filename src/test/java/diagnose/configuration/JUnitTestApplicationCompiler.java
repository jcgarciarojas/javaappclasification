package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.ApplicationCompiler;
import edu.umd.msswe.ece591.diagnose.configuration.JavaApplicationProcessor;
import edu.umd.msswe.ece591.diagnose.configuration.JavaClassInfoFactory;
import edu.umd.msswe.ece591.diagnose.configuration.XmlReader;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.JavaMethodInfo;
import junit.framework.TestCase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestApplicationCompiler extends TestCase {
	
	private static ApplicationCompiler compiler;
	private static String workingDirectory;
	private static List<File> files;

	public void setUp() throws ConfigurationException
	{
		workingDirectory = getWorkingDirectory();
		files = createTestFile(workingDirectory);
		compiler = setUpApplicationCompiler();
	}

	public void tearDown()
	{
		deleteTestFile(workingDirectory);
		compiler = null;
	}

	public void testJavaCompiler_innerClass() throws ConfigurationException
	{
		List<File> files = createInnerTestFile(workingDirectory);
		compiler.process(files, workingDirectory, null);
		JavaApplication ja= compiler.getJavaApplicationObject();
		assertTrue("expected length greater than 0 "+ja.getFiles().size(), ja.getFiles().size() > 0);
		List<JavaClassInfo> classes = ja.getFiles();
		assertTrue(classes.size() == 2);
		assertTrue(new File(workingDirectory+System.getProperty("file.separator")+"ChildTest1.class").exists());
		JavaClassInfo childClass = ja.getClassObject("ChildTest1");
		assertTrue(childClass.getSerializebleClassId() != null);

		List a = childClass.getAttributes();
		assertTrue("validating number of attributes "+a.size(), a.size() ==6);

		List<JavaMethodInfo> m = childClass.getMethods();
		assertTrue(m.size() == 2);

		assertTrue(m.get(0).getAttributes().size() ==2);
		assertTrue(m.get(1).getAttributes().size() ==1);

		JavaClassInfo myClass = ja.getClassObject("MyClass");
		assertTrue(myClass.getSerializebleClassId() != null);
		List<JavaMethodInfo> m1 = myClass.getMethods();

		assertTrue(m1.size() == 1);
		List a1 = myClass.getAttributes();
		assertTrue(a1.size() ==1);
		assertTrue(m1.get(0).getAttributes().size() ==1);
		deleteTestFile(workingDirectory);
	}

	public void testJavaCompiler() throws ConfigurationException
	{
		compiler.process(files, workingDirectory, null);
		JavaApplication ja= compiler.getJavaApplicationObject();
		assertTrue(ja.getFiles().size() > 0);
		List<JavaClassInfo> classes = ja.getFiles();
		assertTrue(classes.size() > 0);
		assertTrue(new File(workingDirectory+System.getProperty("file.separator")+"ChildTest.class").exists());
		JavaClassInfo classInfo = ja.getClassObject("ChildTest");
		assertTrue(classInfo.getSerializebleClassId() != null);
		List m = classInfo.getMethods();
		assertTrue(m.size() == 2);
		List a = classInfo.getAttributes();
		assertTrue(a.size() ==4);
	}

	public static String getWorkingDirectory() throws ConfigurationException
	{
		Properties p = new XmlReader(new File("test_config_system.xml")).readDiagnoseConfig();
		JavaClassInfoFactory.getInstance().setWorkDirectory(p.getProperty("workingDirectory"));
		return p.getProperty("workingDirectory");
	}
	public static ApplicationCompiler setUpApplicationCompiler() throws ConfigurationException
	{
		return  new ApplicationCompiler(new JavaApplicationProcessor());
	}
	public static List<File> createTestFile(String directory) throws ConfigurationException
	{
		File f = new File(directory+System.getProperty("file.separator")+"ChildTest.java");
		if(f.exists())
			f.delete();
		BufferedWriter out = null;
		String newLine= System.getProperty("line.separator"); 
		try {
			out = new BufferedWriter(new FileWriter(f));
			StringBuffer sb = new StringBuffer();
			sb.append("import java.util.List;"+newLine);
			sb.append("/**");
			sb.append(" * comment on class"+newLine);
			sb.append(" * @author juan.garcia"+newLine);
			sb.append("*"+newLine);
			sb.append("*/"+newLine);
			sb.append("public class ChildTest {"+newLine);
			sb.append("	 "+newLine);
			sb.append("	private String attributes = \"Something\";"+newLine);
			sb.append("	private Double attributed = 1d;"+newLine);
			sb.append("	private Long attributel = 2l;"+newLine);
			sb.append("/**"+newLine);
			sb.append("* another comment method 2"+newLine);
			sb.append("* @return"+newLine);
			sb.append("*/"+newLine);
			sb.append("	public String method2()"+newLine);
			sb.append("	{"+newLine);
			sb.append("		String attributeMethod = null;"+newLine);
			sb.append("		if (attributeMethod == null)"+newLine);
			sb.append("			System.out.println(\"null\");"+newLine);
			sb.append("		return \"hello\";"+newLine);
			sb.append("	}"+newLine);
			sb.append(""+newLine);
			sb.append("	private Long attributeAfterMethod;"+newLine);
			sb.append(""+newLine);
			sb.append("/**"+newLine);
			sb.append("* another comment method 2"+newLine);
			sb.append("* @return"+newLine);
			sb.append("*/"+newLine);
			sb.append("	public String method3()"+newLine);
			sb.append("	{"+newLine);
			sb.append("		String attributeMethod = null;"+newLine);
			sb.append("		if (attributeMethod == null)"+newLine);
			sb.append("					System.out.println(\"null\");"+newLine);
			sb.append("		return \"hello\";"+newLine);
			sb.append("	}"+newLine);
			sb.append("}"+newLine);
			out.write(sb.toString());
			out.flush();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		finally{
			try{if(out != null)out.close();} catch(Exception ioe){}
		}
		List<File> l = new ArrayList<File>();
		l.add(new File(directory+"/ChildTest.java"));
		return l;
	}
	public static void deleteTestFile(String directory)
	{
		File d = new File(directory);
		File[] files = d.listFiles();
		if (files != null)
		{
			for(int i=0;i<files.length;i++)
			{
				File f = files[i];
				f.delete();
			}
		}
	}

	public static List<File> createInnerTestFile(String directory) throws ConfigurationException
	{
		String newLine= System.getProperty("line.separator"); 
		StringBuffer sb = new StringBuffer();
		sb.append("import java.util.List;"+newLine);
		sb.append("import java.util.ArrayList;"+newLine);
		sb.append("/**"+newLine);
		sb.append("			/**"+newLine);
		sb.append("			 * comment on class"+newLine);
		sb.append("			 * @author juan.garcia"+newLine);
		sb.append("			 *"+newLine);
		sb.append("			 */"+newLine);
		sb.append("			public class ChildTest1 {"+newLine);
		sb.append("				"+newLine);
		sb.append("				private String attributes = \"Something\";"+newLine);
		sb.append("				private Double attributed = 1d;"+newLine);
		sb.append("				private List attributeList;"+newLine);
		sb.append("				private MyClass myClass;"+newLine);
		sb.append("				private Long attributel = 2l;"+newLine);
		sb.append("				/**"+newLine);
		sb.append("				 * another comment method 2"+newLine); 
		sb.append("				 * @return"+newLine);
		sb.append("				 */"+newLine);
		sb.append("				public String method2()"+newLine);
		sb.append("				{"+newLine);
		sb.append("					ArrayList list  = null;"+newLine);
		sb.append("					String attributeMethod = null;"+newLine);
		sb.append("					if (attributeMethod == null)"+newLine);
		sb.append("						System.out.println(\"null\");"+newLine);
		sb.append("					return \"hello\";"+newLine);
		sb.append("				}"+newLine);
		sb.append("				"+newLine);
		sb.append("				private Long attributeAfterMethod;"+newLine);
		sb.append("				"+newLine);
		sb.append("				/**"+newLine);
		sb.append("				 * another comment method 2"+newLine); 
		sb.append("				 * @return"+newLine);
		sb.append("				 */"+newLine);
		sb.append("				public String method3()"+newLine);
		sb.append("				{"+newLine);
		sb.append("					String attributeMethod = null;"+newLine);
		sb.append("					if (attributeMethod == null)"+newLine);
		sb.append("						System.out.println(\"null\");"+newLine);
		sb.append("					return \"hello\";"+newLine);
		sb.append("				}"+newLine);
		sb.append("				private class MyClass {"+newLine);
		sb.append("					String attributeClass;"+newLine);
		sb.append("					public void myMethod()"+newLine);
		sb.append("					{"+newLine);
		sb.append("						String variable=null;"+newLine);
		sb.append("						return;"+newLine);
		sb.append("					}"+newLine);
		sb.append("				}"+newLine);
		sb.append("			}"+newLine);
		
		return saveTestFile(directory, "ChildTest1.java", sb.toString());

	}
	
	public static List<File> saveTestFile(String directory, String fileName, String content) throws ConfigurationException
	{
		File f = new File(directory+System.getProperty("file.separator")+fileName);
		if(f.exists())
			f.delete();
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(f));
			out.write(content);
			out.flush();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		finally{
			try{if(out != null)out.close();} catch(Exception ioe){}
		}
		List<File> l = new ArrayList<File>();
		l.add(f);
		return l;
		
	}
	
}
