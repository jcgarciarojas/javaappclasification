package diagnose.configuration;

import edu.umd.msswe.ece591.diagnose.configuration.JavaClassInfoFactory;
import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaAttributeInfo;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.JavaMethodInfo;
import edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo;
import junit.framework.TestCase;

import java.io.File;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JUnitTestJavaClassInfoFactory extends TestCase {

	private JavaClassInfoFactory factory;
	private String workingDirectory; 
	public void setUp() throws ConfigurationException
	{
		factory = JavaClassInfoFactory.getInstance();
		workingDirectory = JUnitTestApplicationCompiler.getWorkingDirectory();
	}
	public void tearDown()
	{
		factory = null;
		File d = new File(workingDirectory);
		File[] files = d.listFiles();
		if(files != null)
		{
			for(int i=0;i<files.length;i++)
			{
				files[0].delete();
			}
		}
	}
	
	public void testCreateRealClassInfo() throws ConfigurationException
	{
		RealClassInfo info = (RealClassInfo)getJavaClassesList();
		assertTrue(info.getSerializebleClassId() != null && info.getSerializebleClassId().length() > 0 );
		assertTrue("validate class name "+info.getSimpleName(),info.getSimpleName() != null && info.getSimpleName().equals("ChildTest"));
	}
	
	public void testFactorySerialization()throws ConfigurationException
	{
		String dir = JUnitTestApplicationCompiler.getWorkingDirectory();
		RealClassInfo info = getJavaClassesList();
		serializeClassInfo(dir, info); 
		JavaClassInfo info2 = factory.createProxyClassInfo(info.getPackageName(), info.getSimpleName(), info.getSerializebleClassId());
		
		RealClassInfo real = factory.deserializeRealClassInfo(info2);
		deserializeClassInfo(real, info2);
		checkClassInfo(info2);
	}
	
	private void deserializeClassInfo(RealClassInfo real, JavaClassInfo info2) throws ConfigurationException
	{
		assertTrue(real.getSimpleName().equals(info2.getSimpleName()));
		assertTrue(real.getSerializebleClassId().equals(info2.getSerializebleClassId()));
	}
	
	private void checkClassInfo(JavaClassInfo real)
	{
		assertTrue(real.getModifiers().equals("public"));
		assertTrue(real.getParent().equals("TestClass"));
		assertTrue(real.getPath().endsWith("C:\\health\\bie\\Test\\source\\a\\b\\ChildTest.java"));;
		assertTrue(real.getSourceContent().length() > 50);
		
		assertTrue(real.getAttributes().size() == 1);
		
		JavaAttributeInfo a = real.getAttributes().get(0);
		assertTrue(a.getClassName().equals("com.sun.tools.javac.tree.JCTree$JCVariableDecl"));
		assertTrue(a.getInitializer().equals("\"Something\""));
		assertTrue(a.getModifiers().equals("private"));
		assertTrue(a.getName().equals("attributes"));
		assertTrue(a.getType().equals("String"));
		assertTrue(real.getMethods().size()==1);
		
		JavaMethodInfo m = real.getMethods().get(0);
		assertTrue(m.getClassName().equals("class com.sun.tools.javac.tree.JCTree$JCMethodDecl"));
		assertTrue(m.getMethodContent().length()> 10);
		assertTrue(m.getModifiers().equals("public"));
		assertTrue(m.getName().equals("method2"));
		assertTrue(m.getReturnType().equals("String"));
	}

	private void serializeClassInfo(String dir, RealClassInfo info) throws ConfigurationException
	{
		factory.setWorkDirectory(dir);
		factory.serializeRealClassInfo(info);
		
		File f = new File(dir + System.getProperty("file.separator")+info.getSimpleName()+
				factory.getCurrentFileNumber()+".ser");
		assertTrue(f.exists());
	}

	public static RealClassInfo getJavaClassesList() throws ConfigurationException
	{
		RealClassInfo info = JavaClassInfoFactory.getInstance().createRealClassInfo(null, "ChildTest");
		info.setMembers(null);
		info.setModifiers("public");
		info.setParameters(null);
		info.setParent("TestClass");
		info.setPath("C:\\health\\bie\\Test\\source\\a\\b\\ChildTest.java");
		info.setSourceContent(new StringBuffer(" \n"+
		"public class ChildTest extends TestClass {\n"+
		"	\n"+
		"    public ChildTest() {\n"+
		"        super();\n"+
		"    }\n"+
		"    private String attributes = \"Something\";\n"+
		"    private Double attributed = 1.0;\n"+
		"    private Long attributel = 2L;\n"+
		"    \n"+
		"    public String method1() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method2() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method3() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"    public String method4() {\n"+
		"        String attributeMethod = null;\n"+
		"        if (attributeMethod == null) System.out.println(\"null\");\n"+
		"        return \"hello\";\n"+
		"    }\n"+
		"}"));

		JavaMethodInfo m = new JavaMethodInfo();
		m.setClassName("class com.sun.tools.javac.tree.JCTree$JCMethodDecl");
		m.setDefaultName(null);
		m.setExceptions(null);
		m.setMethodContent(new StringBuffer("public String method2() {"+
		"    String attributeMethod = null;"+
		"    if (attributeMethod == null) System.out.println(\"null\");"+
		"    return \"hello\";"+
		"}"));
		m.setModifiers("public");
		m.setName("method2");
		m.setParametersType(null);
		m.setReturnType("String");

		JavaAttributeInfo a = new JavaAttributeInfo();
		a.setClassName("com.sun.tools.javac.tree.JCTree$JCVariableDecl");
		a.setInitializer("\"Something\"");
		a.setModifiers("private");
		a.setName("attributes");
		a.setType("String");
		info.addMethods(m);
		info.addAttribute(a);
		
		return info;
	}
}
