package net.rubyeye.xmemcached.extension;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;

/**
 * loading class using current thread context classloader.
 * 
 * @author nada
 * 
 */
@SuppressWarnings("unchecked")
public class ExtObjectInputStream extends ObjectInputStream {

	private static final HashMap primClasses = new HashMap(8, 1.0F);
	static {
		primClasses.put("boolean", boolean.class);
		primClasses.put("byte", byte.class);
		primClasses.put("char", char.class);
		primClasses.put("short", short.class);
		primClasses.put("int", int.class);
		primClasses.put("long", long.class);
		primClasses.put("float", float.class);
		primClasses.put("double", double.class);
		primClasses.put("void", void.class);
	}

	protected ExtObjectInputStream() throws IOException, SecurityException {
		super();
	}

	public ExtObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		String name = desc.getName();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			return Class.forName(name, false, loader);
		} catch (ClassNotFoundException ex) {
			Class<?> cl = (Class<?>) primClasses.get(name);
			if (cl != null) {
				return cl;
			} else {
				throw ex;
			}
		}
		// ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// return Class.forName(desc, false, loader);
	}

}
