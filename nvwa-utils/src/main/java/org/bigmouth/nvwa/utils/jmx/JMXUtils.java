package org.bigmouth.nvwa.utils.jmx;

/**
 * 
 */

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @author hp
 *
 */
public class JMXUtils {
	public static Object invokeOverRmi(String host, int port, String objectName, String methodName, 
			Object[] args, String[] signature ) throws Exception {
		JMXConnector 			jmxc = null;
		MBeanServerConnection 	mbsc = null;
	    
	    try
	    {
		    //	JMX连接子服务端的地址
		    JMXServiceURL url = 
		    	new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" +host+ ":" + port + "/jmxrmi");
		    
		    //	创建到服务端的连接
		    jmxc = JMXConnectorFactory.connect(url, null);
		    
	    	//	获得代表远程MBean server的一个MBeanServerConnection 对象
		    mbsc = jmxc.getMBeanServerConnection();
		    
		    ObjectName on = new ObjectName(objectName);
		    return	mbsc.invoke(on, methodName, args, signature);
	    }
	    finally {
	    	if ( null != jmxc ) {
	    		jmxc.close();
	    		jmxc = null;
	    	}
	    }

	}
	
	public static Object invokeOverJmxmp(String host, int port, String objectName, String methodName, 
			Object[] args, String[] signature ) throws Exception {
		JMXConnector 			jmxc = null;
		MBeanServerConnection 	mbsc = null;
	    
	    try
	    {
		    //	JMX连接子服务端的地址
		    JMXServiceURL url = 
		    	new JMXServiceURL("service:jmx:jmxmp://" +host+ ":" + port);
		    
		    //	创建到服务端的连接
		    jmxc = JMXConnectorFactory.connect(url, null);
		    
	    	//	获得代表远程MBean server的一个MBeanServerConnection 对象
		    mbsc = jmxc.getMBeanServerConnection();
		    
		    ObjectName on = new ObjectName(objectName);
		    return	mbsc.invoke(on, methodName, args, signature);
	    }
	    finally {
	    	if ( null != jmxc ) {
	    		jmxc.close();
	    		jmxc = null;
	    	}
	    }

	}
}
