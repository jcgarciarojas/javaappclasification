package edu.umd.msswe.ece591.diagnose.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import edu.umd.msswe.ece591.diagnose.exception.ConfigurationException;
import edu.umd.msswe.ece591.diagnose.metrics.JavaClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.ProxyClassInfo;
import edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo;

/**
 * 
 * University of Michigan - Dearborn
 * ECE695/591 Master's Project
 * Fall 2010
 * @author Juan Garcia
 * @version 1.0
 */

public class JavaClassInfoFactory {
	
	private static JavaClassInfoFactory instance;
	private static long fileNumber = 1;
	private String workDirectory = "";

	private JavaClassInfoFactory()
	{
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized static JavaClassInfoFactory getInstance()
	{
		if (instance == null)
			instance = new JavaClassInfoFactory();
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	private synchronized static String getFileNumber()
	{
		fileNumber++;
		return "OOD"+(fileNumber);
	}

	/**
	 * 
	 * @return
	 */
	public synchronized String getCurrentFileNumber()
	{
		return "OOD"+(fileNumber);
	}

	/**
	 * 
	 * @param classInfo
	 * @return
	 */
	public RealClassInfo deserializeRealClassInfo(JavaClassInfo classInfo) 
	{
		edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo realClass = null;
		ObjectInputStream in = null;
		try
		{
	    	//Deserialize from a file 
	    	File file = new File(this.workDirectory + System.getProperty("file.separator")+classInfo.getSimpleName()+
	    				classInfo.getSerializebleClassId()+".ser");
	    	in = new ObjectInputStream(new FileInputStream(file)); 
	    	// Deserialize the object 
	    	realClass = (edu.umd.msswe.ece591.diagnose.metrics.RealClassInfo) in.readObject(); 
		} catch(IOException ioe)
		{
			throw new RuntimeException (ioe); 
		} catch(ClassNotFoundException ioe)
		{
			throw new RuntimeException (ioe); 
		} finally
		{
			try {if (in != null) in.close();} catch(IOException ioe)
			{
			}
		}
		return realClass;
	}

	/**
	 * 
	 * @param classInfo
	 * @throws ConfigurationException
	 */
    public void serializeRealClassInfo(RealClassInfo classInfo) throws ConfigurationException
    {
    	ObjectOutput out = null;
    
    	try{
	    	//Serialize to a file 
    		String fileName = this.workDirectory + System.getProperty("file.separator")+classInfo.getSimpleName()+
			classInfo.getSerializebleClassId()+".ser";
	    	out = new ObjectOutputStream(new FileOutputStream(fileName)); 
	    	out.writeObject(classInfo);
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    		throw new ConfigurationException(ioe); 
    	} finally {
    		try{if (out != null)out.close();} catch(IOException ioe)
    		{
    			return;
    		}
    	}
    	
    }
    
    /**
     * 
     * @param className
     * @return
     */
    public RealClassInfo createRealClassInfo(String packageName, String className)
    {
    	return new RealClassInfo(packageName, className, ""+getFileNumber());
    }

    /**
     * 
     * @param className
     * @return
     */
    public ProxyClassInfo createProxyClassInfo(String packageName, String className, String serializationId)
    {
    	return new ProxyClassInfo(packageName, className, serializationId);
    }

    /**
	 * @param workDirectory the workDirectory to set
	 */
	public void setWorkDirectory(String workDirectory) {
		this.workDirectory = workDirectory;
	}
    
}
