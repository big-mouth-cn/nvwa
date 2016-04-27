package org.bigmouth.nvwa.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public final class ReflectUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtils.class);

	private ReflectUtils() {
	}

	public static interface FieldFilter {
		boolean accept(Field filed);
	}

	public static interface MethodFilter {
		public static final MethodFilter DEFAULT = new MethodFilter() {

			@Override
			public boolean accept(Method method) {
				return true;
			}
		};

		boolean accept(Method method);
	}

	@SuppressWarnings("unchecked")
	public final static void setDynaBean2Request(Object request, Object dynaBean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class clazz = request.getClass();
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				String f_name = f.getName();
				if ("serialVersionUID".equals(f_name))
					continue;
				// String f_type = f.getType().getName();
				String f_value = BeanUtils.getProperty(dynaBean, f_name);
				BeanUtils.setProperty(request, f_name, f_value);
			}
			clazz = clazz.getSuperclass();
		} while (!clazz.getSimpleName().equals("BaseRequest"));
	}

	public static Field findField(Class<?> clazz, final String fieldName) {
		if (null == clazz || null == fieldName || fieldName.equals(""))
			throw new IllegalArgumentException();

		do {
			try {
				Field f = clazz.getDeclaredField(fieldName);
				return f;
			} catch (SecurityException e) {
				LOGGER.error("findField;", e);
				return null;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
				continue;
			}
		} while (!clazz.equals(Object.class));
		return null;
	}

	public static Field[] findAllFields(Class<?> clazz) {
		return findFields(clazz, null);
	}

	public static Field[] findFields(Class<?> clazz, FieldFilter fieldFilter) {
		Field[] result = new Field[0];
		do {
			Field[] fs = clazz.getDeclaredFields();
			if (null != fieldFilter) {
				List<Field> list_fs = new ArrayList<Field>();
				for (Field f : fs) {
					if (fieldFilter.accept(f))
						list_fs.add(f);
				}
				fs = list_fs.toArray(new Field[0]);
			}

			Field[] tmp = new Field[result.length + fs.length];
			System.arraycopy(result, 0, tmp, 0, result.length);
			System.arraycopy(fs, 0, tmp, result.length, fs.length);
			result = tmp;
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));
		return result;
	}

	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
		if (null == clazz || null == annotationClass)
			throw new NullPointerException();

		do {
			T anno = clazz.getAnnotation(annotationClass);
			if (null != anno)
				return anno;
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));
		return null;
	}

	public static <T extends Annotation> List<T> getAnnotations(Class<?> clazz,
			Class<T> annotationClass) {
		if (null == clazz || null == annotationClass)
			throw new NullPointerException();
		List<T> ret = Lists.newArrayList();
		do {
			T anno = clazz.getAnnotation(annotationClass);
			if (null != anno)
				ret.add(anno);
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));

		return ret;
	}

	public static List<Method> findMethods(Class<?> clazz, MethodFilter methodFilter) {
		if (null == clazz)
			throw new NullPointerException("clazz");
		if (null == methodFilter)
			methodFilter = MethodFilter.DEFAULT;
		List<Method> ret = Lists.newArrayList();
		do {
			Method[] ms = clazz.getDeclaredMethods();
			for (Method m : ms) {
				if (methodFilter.accept(m))
					ret.add(m);
			}
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));
		return ret;
	}

	public static void main(String[] args) {
		// LoginRequest request = new LoginRequest();
		// Field[] fields = request.getClass().getDeclaredFields();
		// for (Field f : fields) {
		// System.out.println(f.getName());
		// System.out.println(f.getType().getName());
		// }
		// Class<B> clazz = B.class;
		// Field[] fs = clazz.getFields();
		// for (Field f : fs) {
		// System.out.println(f.getName());
		// }
//		Field[] fs = findAllFields(C.class);
//		for (Field f : fs) {
//			System.out.println(f.getName());
//		}
		
		try{
			Enumeration<URL> urls = ReflectUtils.class.getClassLoader().getResources("com/channel/utils");
			for(;urls.hasMoreElements();){
				URL url = urls.nextElement();
				System.out.println(url);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static class A {
		private String a;

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}
	}

	private static class B extends A {
		private String b;

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}

	}

	private static class C extends B {
		private String c;

		public String getC() {
			return c;
		}

		public void setC(String c) {
			this.c = c;
		}
	}
}
