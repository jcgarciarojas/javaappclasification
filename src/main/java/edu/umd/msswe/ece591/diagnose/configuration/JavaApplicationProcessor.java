package edu.umd.msswe.ece591.diagnose.configuration;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaApplication;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class JavaApplicationProcessor extends AbstractProcessor {

    private Trees trees;
    private JavaApplication javaApplication;
    private JavaClassInfoFactory classFactory;
    
    /**
     * 
     * @param javaClassesList
     * @param metricsEngine
     * @param rulesEngine
     */
    public JavaApplicationProcessor()
    {
    	javaApplication = new JavaApplication();
    	classFactory = JavaClassInfoFactory.getInstance();
    }

    /**
     * 
     */
    @Override
    public void init(ProcessingEnvironment pe) {
        super.init(pe);
        trees = Trees.instance(pe);
    }

    /**
     * 
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnvironment)  {

    	JavaClassVisitor visitor = new JavaClassVisitor();
		for (Element e : roundEnvironment.getRootElements()) {
			TreePath tp = trees.getPath(e);

			//initializing class
			visitor.initClass();
			// invoke the scanner
			visitor.scan(tp, trees);

			//There can be several classes in one file (private inner class inside of other classes)
			// not recommended but... anyway this case is covered here
			List<RealClassInfo> realClasses = visitor.getClassInfo();
			
			this.synchronizedClasses(realClasses);
			
			javaApplication.addClassObjects(this.getJavaClassInfo(realClasses));
		}
		return true;
    }

	/**
	 * @return the javaClassesList
	 */
	public JavaApplication getJavaApplicationObject() {
		return javaApplication;
	}
	
	private void synchronizedClasses(List<RealClassInfo> realClasses)
	{
		try {
			for (RealClassInfo realClass : realClasses) {
				JavaClassInfoFactory.getInstance()
						.serializeRealClassInfo(realClass);
			}
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * 
	 * @param realClasses
	 * @return
	 */
	private List<JavaClassInfo> getJavaClassInfo(List<RealClassInfo> realClasses)
	{
		List<JavaClassInfo> classes = new ArrayList<JavaClassInfo>();
		
		for (RealClassInfo realClass: realClasses)
		{
			JavaClassInfo classInfo = classFactory.createProxyClassInfo(
					realClass.getPackageName(), realClass.getSimpleName(), 
						realClass.getSerializebleClassId());
			classes.add(classInfo);
		}
		
		return classes;
	}

	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		classFactory.setWorkDirectory(workingDirectory);
	}
    
}
