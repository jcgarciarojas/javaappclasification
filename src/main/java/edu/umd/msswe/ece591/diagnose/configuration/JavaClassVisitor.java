package edu.umd.msswe.ece591.diagnose.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import edu.umd.msswe.ece591.diagnose.metrics.JavaAttributeInfo;
import edu.umd.msswe.ece591.diagnose.metrics.JavaMethodInfo;
import edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo;



/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JavaClassVisitor extends TreePathScanner<Object, Trees> {
	
	private List<RealClassInfo> classes;
	private RealClassInfo classInfo; 
	private JavaMethodInfo method; 
	private boolean validClassName = true;

	/**
	 * 
	 *
	 */
	public JavaClassVisitor(){
		
	}
	
	public void initClass()
	{
		classes = new ArrayList<RealClassInfo>();
	}

	/**
	 * 
	 * @return
	 */
	public List<RealClassInfo> getClassInfo()
	{
		return classes;
	}
	
	/**
	 * 
	 */
	public Object visitClass(ClassTree classTree, Trees trees)
	{
		this.validClassName = true;
        TreePath path = getCurrentPath();
        CompilationUnitTree t = path.getCompilationUnit();
        JavaFileObject j = t.getSourceFile();

        String packageName = (t.getPackageName() != null) ? t.getPackageName().toString() : "";
        
        if (classTree.getSimpleName().toString() == null || classTree.getSimpleName().toString().trim().length() <=0)
        	this.validClassName = false;
        
        if (this.validClassName)
        {
			classInfo = JavaClassInfoFactory.getInstance().createRealClassInfo(packageName, classTree.getSimpleName().toString());
			classes.add(classInfo);
			
			classInfo.setPath(j.toString());
			if (classTree.getImplementsClause() != null)
				classInfo.setInterfaceName(classTree.getImplementsClause().toString());
			if (classTree.getExtendsClause() != null)
				classInfo.setParent(classTree.getExtendsClause().toString());
			if(classTree.getModifiers() != null)
				classInfo.setModifiers(classTree.getModifiers().toString());
			if(classTree.getTypeParameters() != null)
				classInfo.setParameters(classTree.getTypeParameters().toString());
			if (classTree.getMembers() != null)
				classInfo.setMembers(classTree.getMembers().toString());
			if(classTree != null)
	        	classInfo.setSourceContent(new StringBuffer(classTree.toString()));
        }
        return super.visitClass(classTree, trees);
	}
	/**
	 * 
	 */
	public Object visitMethod(MethodTree methodTree, Trees trees)
	{

		if (this.validClassName)
		{
			method = new JavaMethodInfo();
			if(methodTree.getClass() != null)
				method.setClassName(methodTree.getClass().toString());
			if(methodTree.getDefaultValue() != null)
				method.setDefaultName(methodTree.getDefaultValue().toString());
			if(methodTree.getThrows() != null)
				method.setExceptions(methodTree.getThrows().toString());
			if(methodTree != null)
				method.setMethodContent(new StringBuffer(methodTree.toString()));
			if(methodTree.getModifiers() != null)
				method.setModifiers(methodTree.getModifiers().toString());
			if(methodTree.getName() != null)
				method.setName(methodTree.getName().toString());
			
			if(methodTree.getTypeParameters() != null)
				method.setParametersType(methodTree.getTypeParameters().toString());
			if(methodTree.getReturnType() != null)
				method.setReturnType(methodTree.getReturnType().toString());
			
			if (method.getName().equalsIgnoreCase("<init>"))
				classInfo.addConstructors(method);
			else
				classInfo.addMethods(method);
		}
		return super.visitMethod(methodTree, trees);
	}
			
	/**
	 * 
	 */
	public Object visitVariable(VariableTree variableTree, Trees trees)
	{
		TreePath path = getCurrentPath();
		
		if (this.validClassName)
		{
			JavaAttributeInfo attribute = new JavaAttributeInfo();
			if(variableTree.getClass() != null)
				attribute.setClassName(variableTree.getClass().toString());
			if(variableTree.getInitializer() != null)
				attribute.setInitializer(variableTree.getInitializer().toString());
			if(variableTree.getModifiers() != null)
				attribute.setModifiers(variableTree.getModifiers().toString());
			if (variableTree.getName() != null)
				attribute.setName(variableTree.getName().toString());
			if(variableTree.getType() != null)
				attribute.setType(variableTree.getType().toString());
			
			//This is a class variable
			if (path.getParentPath().getLeaf().getKind().equals(Kind.CLASS))
				classInfo.addAttribute(attribute);
			
			//This is a parameter for either the constructor or method
			else if (path.getParentPath().getLeaf().getKind().equals(Kind.METHOD))
				method.addParameter(attribute);
			
			//This is a method's variable
			else
				method.addAttribute(attribute);
		}
		
        return super.visitVariable(variableTree, trees);
	}

}