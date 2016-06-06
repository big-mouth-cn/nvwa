package org.bigmouth.nvwa.jmx.model;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.apache.commons.beanutils.BeanUtils;

public class GenericModelMBean<T> implements ModelMBean, MBeanRegistration {

	private static final Map<ObjectName, Object> sources = new ConcurrentHashMap<ObjectName, Object>();

	public static Object getSource(ObjectName oname) {
		return sources.get(oname);
	}

	private final T source;
	private final MBeanInfo info;

	private volatile MBeanServer server;
	private volatile ObjectName name;

	public GenericModelMBean(T source) {
		if (null == source)
			throw new IllegalArgumentException("source");
		this.source = source;
		this.info = createModelMBeanInfo(source);
	}

	public final T getSource() {
		return source;
	}

	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		return true;
	}

	protected boolean isAttribute(String attrName, Class<?> attrType) {
		return true;
	}

	private MBeanInfo createModelMBeanInfo(T source) {
		String className = source.getClass().getName();
		String description = "";

		ModelMBeanConstructorInfo[] constructors = new ModelMBeanConstructorInfo[0];
		ModelMBeanNotificationInfo[] notifications = new ModelMBeanNotificationInfo[0];

		List<ModelMBeanAttributeInfo> attributes = new ArrayList<ModelMBeanAttributeInfo>();
		List<ModelMBeanOperationInfo> operations = new ArrayList<ModelMBeanOperationInfo>();

		addAttributes(attributes, source);
		addOperations(operations, source);

		operations.add(new ModelMBeanOperationInfo("unregisterMBean", "unregisterMBean",
				new MBeanParameterInfo[0], void.class.getName(), ModelMBeanOperationInfo.ACTION));

		return new ModelMBeanInfoSupport(className, description,
				attributes.toArray(new ModelMBeanAttributeInfo[attributes.size()]), constructors,
				operations.toArray(new ModelMBeanOperationInfo[operations.size()]), notifications);
	}

	private void addOperations(List<ModelMBeanOperationInfo> operations, Object object) {
		for (Method m : object.getClass().getMethods()) {
			String mname = m.getName();

			// Ignore getters and setters.
			if (mname.startsWith("is") || mname.startsWith("get") || mname.startsWith("set")) {
				continue;
			}

			// Ignore Object methods.
			if (mname.matches("(wait|notify|notifyAll|toString|equals|compareTo|hashCode|clone)")) {
				continue;
			}

			// Ignore other user-defined non-operations.
			if (!isOperation(mname, m.getParameterTypes())) {
				continue;
			}

			List<MBeanParameterInfo> signature = new ArrayList<MBeanParameterInfo>();
			for (Class<?> paramType : m.getParameterTypes()) {
				String paramName = paramType.getName();
				signature.add(new MBeanParameterInfo(paramName, paramName, paramName));
			}

			Class<?> returnType = m.getReturnType();
			operations.add(new ModelMBeanOperationInfo(m.getName(), m.getName(), signature
					.toArray(new MBeanParameterInfo[signature.size()]), returnType.getName(),
					ModelMBeanOperationInfo.ACTION));
		}
	}

	private void addAttributes(List<ModelMBeanAttributeInfo> attributes, Object object) {
		addAttributes(attributes, object, object.getClass());
	}

	private void addAttributes(List<ModelMBeanAttributeInfo> attributes, Object object,
			Class<?> type) {

		PropertyDescriptor[] pdescs;
		try {
			pdescs = Introspector.getBeanInfo(type).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			return;
		}

		for (PropertyDescriptor pdesc : pdescs) {
			// Ignore a write-only property.
			if (pdesc.getReadMethod() == null) {
				continue;
			}

			// Ignore unmanageable property.
			String attrName = pdesc.getName();
			Class<?> attrType = pdesc.getPropertyType();
			if (attrName.equals("class")) {
				continue;
			}

			if (!isAttribute(attrName, attrType)) {
				continue;
			}

			// Ordinary property.
			String fqan = attrName;
			attributes.add(new ModelMBeanAttributeInfo(fqan, attrType.getName(), pdesc
					.getShortDescription(), true, true, false));
		}
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {

		// Handle synthetic operations first.
		if (actionName.equals("unregisterMBean")) {
			try {
				server.unregisterMBean(this.name);
				return null;
			} catch (InstanceNotFoundException e) {
				throw new MBeanException(e);
			}
		}

		try {
			// Find the right method.
			for (Method m : source.getClass().getMethods()) {
				if (!m.getName().equalsIgnoreCase(actionName)) {
					continue;
				}
				return m.invoke(getSource(), params);
			}
			// No methods matched.
			throw new IllegalArgumentException("Failed to find a matching operation: " + name);
		} catch (Exception e) {
			throw new MBeanException(e);
		}
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		T source = getSource();
		try {
			return BeanUtils.getProperty(source, attribute);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("getAttribute:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("getAttribute:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("getAttribute:", e);
		}
	}

	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException,
			InvalidAttributeValueException, MBeanException, ReflectionException {
		String name = attribute.getName();
		Object value = attribute.getValue();
		T source = getSource();// setAttribute

		try {
			BeanUtils.setProperty(source, name, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("setAttribute:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("setAttribute:", e);
		}
	}

	@Override
	public AttributeList getAttributes(String[] names) {
		AttributeList answer = new AttributeList();
		for (int i = 0; i < names.length; i++) {
			try {
				answer.add(new Attribute(names[i], getAttribute(names[i])));
			} catch (Exception e) {
				e.printStackTrace();
				// Ignore.
			}
		}
		return answer;
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		// Prepare and return our response, eating all exceptions
		String names[] = new String[attributes.size()];
		int n = 0;
		Iterator<Object> items = attributes.iterator();
		while (items.hasNext()) {
			Attribute item = (Attribute) items.next();
			names[n++] = item.getName();
			try {
				setAttribute(item);
			} catch (Exception e) {
				// Ignore all exceptions
			}
		}

		return getAttributes(names);
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return info;
	}

	@Override
	public void addAttributeChangeNotificationListener(NotificationListener listener,
			String attributeName, Object handback) throws MBeanException,
			RuntimeOperationsException, IllegalArgumentException {
		// Do nothing
	}

	@Override
	public void removeAttributeChangeNotificationListener(NotificationListener listener,
			String attributeName) throws MBeanException, RuntimeOperationsException,
			ListenerNotFoundException {
		// Do nothing
	}

	@Override
	public void addNotificationListener(NotificationListener listener, NotificationFilter filter,
			Object handback) throws IllegalArgumentException {
		// Do nothing
	}

	@Override
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		// Do nothing
	}

	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		return new MBeanNotificationInfo[0];
	}

	@Override
	public void setModelMBeanInfo(ModelMBeanInfo inModelMBeanInfo) throws MBeanException,
			RuntimeOperationsException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void setManagedResource(Object mr, String mr_type) throws MBeanException,
			RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void sendNotification(Notification ntfyObj) throws MBeanException,
			RuntimeOperationsException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void sendNotification(String ntfyText) throws MBeanException, RuntimeOperationsException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void sendAttributeChangeNotification(AttributeChangeNotification notification)
			throws MBeanException, RuntimeOperationsException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void sendAttributeChangeNotification(Attribute oldValue, Attribute newValue)
			throws MBeanException, RuntimeOperationsException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void load() throws MBeanException, RuntimeOperationsException, InstanceNotFoundException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public void store() throws MBeanException, RuntimeOperationsException,
			InstanceNotFoundException {
		throw new RuntimeOperationsException(new UnsupportedOperationException());
	}

	@Override
	public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
		this.server = server;
		this.name = name;
		return name;
	}

	@Override
	public void postRegister(Boolean registrationDone) {
		if (registrationDone) {
			sources.put(name, source);
		}
	}

	@Override
	public void preDeregister() throws Exception {
		// Do nothing
	}

	@Override
	public void postDeregister() {
		sources.remove(name);
		this.server = null;
		this.name = null;
	}
}
