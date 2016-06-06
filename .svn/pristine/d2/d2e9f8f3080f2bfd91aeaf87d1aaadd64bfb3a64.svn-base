package org.bigmouth.nvwa.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public final class JVMUtils {

	private JVMUtils() {
	}

	public static String getFirstInvokeClassName() {
		StackTraceElement[] elements = new Exception().getStackTrace();
		if (null == elements || 0 == elements.length) {
			return "";
		}
		return elements[elements.length - 1].getClassName();
	}

	public static String getFirstInvokeClassSimpleName() {
		String className = getFirstInvokeClassName();
		String[] factors = className.split("\\.");
		return factors[factors.length - 1];
	}

	public static String getFirstInvokeMethodName() {
		StackTraceElement[] elements = new Exception().getStackTrace();
		if (null == elements || 0 == elements.length) {
			return "";
		}
		return elements[elements.length - 1].getMethodName();
	}

	public static String getLastInvokeClassName() {
		return getInvokeClassName(2);
	}

	public static String getLastInvokeClassSimpleName() {
		return getInvokeClassSimpleName(2);
	}

	public static String getInvokeClassName(int offset) {
		StackTraceElement[] elements = new Exception().getStackTrace();
		if (null == elements || elements.length <= offset) {
			return "";
		}
		return elements[offset].getClassName();
	}

	public static String getInvokeClassSimpleName(int offset) {
		String className = getInvokeClassName(offset + 1);
		if (StringUtils.isBlank(className)) {
			return "";
		}
		String[] factors = className.split("\\.");
		return factors[factors.length - 1];
	}

	public static String getLastInvokeMethodName() {
		return getInvokeMethodName(2);
	}

	public static String getInvokeMethodName(int offset) {
		StackTraceElement[] elements = new Exception().getStackTrace();
		if (null == elements || elements.length <= offset) {
			return "";
		}
		return elements[1].getMethodName();
	}

	public interface PropertyFilter {
		boolean accept(Pair<String, String> p);
	}

	public static Properties getProperties(PropertyFilter filter) {
		if (null == filter) {
			return System.getProperties();
		}

		Properties ret = new Properties();
		Properties props = System.getProperties();
		for (String pName : props.stringPropertyNames()) {
			String pValue = props.getProperty(pName);
			Pair<String, String> p = Pair.of(pName, pValue);
			if (filter.accept(p)) {
				ret.put(p.getFirst(), p.getSecond());
			}
		}
		return ret;
	}

	public static void setProperties(String[] jvmParameters) throws IndexOutOfBoundsException {
		if (ArrayUtils.isEmpty(jvmParameters))
			return;
		for (String jp : jvmParameters) {
			String[] keyAndValue = jp.split("=");
			System.setProperty(keyAndValue[0], keyAndValue[1]);
		}
	}

	public static boolean appendToClassPath(String name) {

		// 适用于 JDK 1.6
		// from JDK DOC "java.lang.instrument Interface Instrumentation"
		// ...
		// The system class loader supports adding a JAR file to be searched
		// if it implements a method named appendToClassPathForInstrumentation
		// which takes a single parameter of type java.lang.String.
		// The method is not required to have public access. The name of the JAR
		// file
		// is obtained by invoking the getName() method on the jarfile and this
		// is
		// provided as the parameter to the appendtoClassPathForInstrumentation
		// method.
		// ...
		try {
			ClassLoader clsLoader = ClassLoader.getSystemClassLoader();
			Method appendToClassPathMethod = clsLoader.getClass().getDeclaredMethod(
					"appendToClassPathForInstrumentation", String.class);
			if (null != appendToClassPathMethod) {
				appendToClassPathMethod.setAccessible(true);
				appendToClassPathMethod.invoke(clsLoader, name);
			}
			return true;
		} catch (SecurityException e) {
			throw new RuntimeException("appendtoClassPath:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("appendtoClassPath:", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("appendtoClassPath:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("appendtoClassPath:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("appendtoClassPath:", e);
		}
	}

	public static String[] addAllJarsToClassPath(String dirName) {
		List<String> ret = new ArrayList<String>();

		File dir = new File(dirName);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					ret.addAll(Arrays.asList(addAllJarsToClassPath(file.getAbsolutePath())));
				} else {
					String filename = file.getName().toLowerCase();
					if (filename.endsWith(".jar")) {
						if (appendToClassPath(file.getAbsolutePath())) {
							ret.add(file.getAbsolutePath());
						}
					}
				}
			}
		}

		return ret.toArray(new String[0]);
	}

	public static boolean loadSunJDKToolsJar() {
		final String home = System.getProperty("java.home");

		// Normally in ${java.home}/lib in build environments.
		// maybe ${java.home} refence to jdk/jre
		String toolsJarPath = home + File.separator + "lib" + File.separator + "tools.jar";

		if (!new File(toolsJarPath).exists()) {
			toolsJarPath = home + File.separator + ".." + File.separator + "lib" + File.separator
					+ "tools.jar";

			if (!new File(toolsJarPath).exists()) {
				return false;
			}
		}

		return appendToClassPath(toolsJarPath);
	}
}
