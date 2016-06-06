package org.bigmouth.nvwa.dpl.hotswap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * <p>ByteCode structure:</p>
 * <p>directory:main</p>
 * <pre>
 * originalName               binaryName                value
 * "com/a/Foo.class"  --+-->  "com.a.Foo"     ----->    byte[]
 *                      |                                 ^
 *                      |                                 |
 *                      +-->  "com/a/Foo.class"-----------+
 *                      |                                 |
 *                      |                                 |
 *                      +-->  "main!com/a/Foo.class"------+
 *                      
 * "com/a/Foo.txt"    --+-->  "com/a/Foo.txt" ----->    byte[]
 *                      |                                 ^
 *                      |                                 | 
 *                      +-->  "main!com/a/Foo.txt"--------+
 * </pre>
 * <p>directory:lib</p>
 * <pre>
 *                   originalName               binaryName                  value
 * lib/myjar.jar:    "com/a/Foo.class"  ----->  "com.a.Foo"     ----->      byte[]
 *                                        |                                   ^
 *                                        |                                   |
 *                                        +-->  "com/a/Foo.class"-------------+
 *                                        |                                   |
 *                                        |                                   |
 *                                        +-->  "lib/myjar.jar!com/a/Foo.class"
 *                                        
 *                   "com/a/Foo.txt"    --+-->  "com/a/Foo.txt" ----->      byte[]
 *                                        |                                   ^
 *                                        |                                   | 
 *                                        +-->  "lib/myjar.jar!com/a/Foo.txt"-+
 * </pre>
 * 
 * <p>ProtectionDomain stratege:one PlugIn one ProtectionDomain.</p>
 * <pre>
 * for example:
 * IF 
 *   PlugIn A's path:/home/cgss/plugins/A.jar;
 * THEN
 *   its CodeSource is:URL("file:/home/cgss/plugins/A.jar");
 *   its Certificate is null;
 *   its PermissionCollection is null;
 *   its Principal is null.
 * 
 * </pre>
 * 
 * @author nada
 * 
 */
public class PlugInClassLoader extends ClassLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlugInClassLoader.class);

	private final static String LIB_PREFIX = "lib/";
	private final static String JAR_SUFFIX = ".jar";
	private final static String CLASS_SUFFIX = ".class";
	private final static String MAIN_RESOURCE_PREFIX = "main";
	private final static String INNER_PREFIX_SEP = "!";
	private final static String MAIN_RESOURCE_PREFIX_SEP = MAIN_RESOURCE_PREFIX + INNER_PREFIX_SEP;
	private final static int MAIN_RESOURCE_PREFIX_SEP_LEN = MAIN_RESOURCE_PREFIX_SEP.length();

	private final List<String> subJarNameList = new ArrayList<String>();
	private final Map<String, ByteCode> byteCodeCache = new HashMap<String, ByteCode>();

	// private ConcurrentMap<CodeSource, ProtectionDomain> pdCache = new
	// ConcurrentHashMap<CodeSource, ProtectionDomain>();

	// create it in the constructor
	private ProtectionDomain defaultProtectionDomain = null;

	private final String plugInJarPath;

	public PlugInClassLoader(String jarPath) {
		this(jarPath, ClassLoader.getSystemClassLoader());
	}

	public PlugInClassLoader(String jarPath, ClassLoader classLoader) {
		super(classLoader);
		if (StringUtils.isBlank(jarPath))
			throw new IllegalArgumentException("jarPath is blank.");
		this.plugInJarPath = jarPath;
		init(jarPath);
	}

	public String getPlugInJarPath(){
		return plugInJarPath;
	}

	private synchronized void init(String jarPath) {
		URL plugInURL = null;
		try {
			plugInURL = new URL("file:" + jarPath);
		} catch (MalformedURLException e) {
			throw new PlugInClassLoaderInitException("bad url path:", e);
		}

		defaultProtectionDomain = generateProtectionDomain(plugInURL);

		try {
			loadByteCodes(jarPath);
		} catch (IOException e) {
			throw new PlugInClassLoaderInitException("loadByteCodes:", e);
		}

		subJarNameList.add(MAIN_RESOURCE_PREFIX);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("plugin [" + jarPath + "] load byte code successful.");

	}

	private ProtectionDomain generateProtectionDomain(URL url) {
		CodeSource source = new CodeSource(url, (Certificate[]) null);
		return new ProtectionDomain(source, null, this, null);
	}

	protected synchronized ProtectionDomain getProtectionDomain() {
		return defaultProtectionDomain;
	}

	private void loadByteCodes(String jarPath) throws IOException {
		if (null == jarPath)
			throw new NullPointerException("plugin jar file path must not be null.");

		JarFile pluginFile = null;

		try {
			pluginFile = new JarFile(jarPath);
			Manifest manifest = pluginFile.getManifest();

			if (null == pluginFile)
				throw new NullPointerException("plugin file must not be null.");

			for (Enumeration<JarEntry> e = pluginFile.entries(); e.hasMoreElements();) {
				JarEntry je = e.nextElement();
				if (je.isDirectory())
					continue;

				String entryName = je.getName();
				InputStream is = pluginFile.getInputStream(je);
				if (null == is)
					throw new IOException("Unable to load resource " + entryName);

				if (isJar(entryName)) {
					loadJarByteCodes(is, entryName);
				} else {
					loadSingleByteCodes(is, entryName, manifest, MAIN_RESOURCE_PREFIX);
				}
			}
		} finally {
			if (null != pluginFile)
				pluginFile.close();
		}
	}

	private void loadJarByteCodes(InputStream is, String jarName) throws IOException {
		if (null == is)
			throw new NullPointerException();

		subJarNameList.add(jarName);

		JarInputStream jis = null;
		try {
			jis = new JarInputStream(is);
			Manifest manifest = jis.getManifest();
			for (JarEntry je = jis.getNextJarEntry(); null != je; je = jis.getNextJarEntry()) {
				if (je.isDirectory())
					continue;

				String entryName = je.getName();

				// validate jar format later.
				// if (isJar(entryName)) {
				// throw new RuntimeException("not support nested jar.");
				// } else {
				// loadPathByteCodes(jis,entryName,jarName);
				// }
				loadSingleByteCodes(jis, entryName, manifest, jarName);
			}
		} finally {
			if (null != jis)
				jis.close();
		}
	}

	private void loadSingleByteCodes(InputStream is, String entryName, Manifest manifest,
			String type) throws IOException {
		byte[] data = getByteArray(is);

		if (isClass(entryName)) {
			String classBinaryName = resolveClassName(entryName);
			byteCodeCache.put(classBinaryName, new ByteCode(classBinaryName, entryName, data,
					manifest, null));
		}

		String resourceGlobalBinaryName = resolveResourceName(type, entryName);
		String resourceLocalBinaryName = entryName;

		byteCodeCache.put(resourceGlobalBinaryName, new ByteCode(resourceGlobalBinaryName,
				entryName, data, manifest, null));
		byteCodeCache.put(resourceLocalBinaryName, new ByteCode(resourceLocalBinaryName, entryName,
				data, manifest, null));
	}

	private boolean isJar(String entryName) {
		return entryName.startsWith(LIB_PREFIX) && entryName.endsWith(JAR_SUFFIX);
	}

	private boolean isClass(String originalName) {
		return originalName.endsWith(CLASS_SUFFIX);
	}

	private byte[] getByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(is, baos);
		return baos.toByteArray();
	}

	private String resolveResourceName(String jarName, String resourceName) {
		return jarName + INNER_PREFIX_SEP + resourceName;
	}

	private String resolveClassName(String name) {
		return name.substring(0, name.length() - 6).replace('/', '.');
	}
	
	public List<String> searchResources(String resourcePath) {
		return searchResources(resourcePath, true);
	}

	public List<String> searchResources(String resourcePath, boolean isIncludeLib) {
		return searchResources(resourcePath, ResourceFilter.DEFAULT, isIncludeLib);
	}

	public List<String> searchResources(String resourcePath, ResourceFilter resourceFilter) {
		return searchResources(resourcePath, resourceFilter, true);
	}
	
	/**
	 * 
	 * @param resourcePath
	 *            com/channel/plugin/test/* <br>
	 *            com/channel/plugin/ <br>
	 *            com/channel/plugin <br>
	 *            / <br>
	 *            /* <br>
	 * @return
	 */
	public List<String> searchResources(String resourcePath, ResourceFilter resourceFilter,
			boolean isIncludeLib) {
		if (StringUtils.isBlank(resourcePath))
			throw new IllegalArgumentException("resourcePath");
		if (null == resourceFilter)
			throw new NullPointerException("resourceFilter");

		return doSearchResources(resourcePath.trim(), resourceFilter, isIncludeLib);
	}
	
	private List<String> doSearchResources(String resourcePath, ResourceFilter resourceFilter,
			boolean isIncludeLib) {
		Set<String> ret = Sets.newHashSet();
		Pattern pattern = genResourceMatchPattern(resourcePath, isIncludeLib);
		for (Entry<String, ByteCode> e : this.byteCodeCache.entrySet()) {
			if (e.getValue().isJavaClass())
				continue;
			String rp = e.getKey();
			Matcher m = pattern.matcher(rp);
			if (!m.matches())
				continue;
			if (!isIncludeLib) {
				rp = rp.substring(MAIN_RESOURCE_PREFIX_SEP_LEN);
			} else {
				if (rp.startsWith(MAIN_RESOURCE_PREFIX_SEP))
					rp.substring(MAIN_RESOURCE_PREFIX_SEP_LEN);
			}
			if (resourceFilter.accept(rp))
				ret.add(rp);
		}
		return Lists.newArrayList(ret);
	}
	
//	private List<String> doSearchResources(String resourcePath, ResourceFilter resourceFilter,
//			boolean isIncludeLib) {
//		final String mainPrefix = MAIN_RESOURCE_PREFIX + "!";
//		final int mainPrefixLen = mainPrefix.length();
//		if (!isIncludeLib)
//			resourcePath = mainPrefix + resourcePath;
//		Set<String> ret = Sets.newHashSet();
//		Pattern pattern = genResourceMatchPattern(resourcePath, isIncludeLib);
//		for (Entry<String, ByteCode> e : this.byteCodeCache.entrySet()) {
//			if (e.getValue().isJavaClass())
//				continue;
//			String rp = e.getKey();
//			Matcher m = pattern.matcher(rp);
//			if (!m.matches())
//				continue;
//			if (!isIncludeLib)
//				rp = rp.substring(mainPrefixLen);
//			if (resourceFilter.accept(rp))
//				ret.add(rp);
//		}
//		return Lists.newArrayList(ret);
//	}

	private Pattern genResourceMatchPattern(String source, boolean isIncludeLib) {
		String regex = "";
		if ("/".equals(source)) {
			// TODO:
		} else if ("/*".equals(source)) {
			// TODO:
		} else if (source.endsWith("/*")) {
			source = source.substring(0, source.length() - 1);
			regex = source
					+ "([a-zA-Z]{1}[a-zA-Z0-9]*/)*([a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]*){1}";
		} else if (source.endsWith("/")) {
			regex = source + "([a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]+){1}";
		} else {
			regex = source + "(/[a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]+){1}";
		}
		if (!isIncludeLib) {
			regex = MAIN_RESOURCE_PREFIX + INNER_PREFIX_SEP + regex;
		} else {
			regex = "(?!" + MAIN_RESOURCE_PREFIX + "\\" + INNER_PREFIX_SEP + ")" + regex;
		}
		return Pattern.compile(regex);
	}
	
//	private Pattern genResourceMatchPattern(String source, boolean isIncludeLib) {
//		String regex = "";
//		if ("/*".equals(source)) {
//			// TODO:
//		} else if ("/".equals(source)) {
//			// TODO:
//		} else if (source.endsWith("/*")) {
//			source = source.substring(0, source.length() - 1);
//			regex = "([a-zA-Z]{1}[a-zA-Z0-9]*/)*([a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]*){1}";
//		} else if (source.endsWith("/")) {
//			regex = source + "([a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]+){1}";
//		} else {
//			regex = source + "(/[a-zA-Z\\-\\_\\!]{1}[a-zA-Z0-9\\.\\-\\_\\!]+){1}";
//		}
//		if (!isIncludeLib)
//			regex = MAIN_RESOURCE_PREFIX + "!" + regex;
//		else
//			regex = "(?!" + MAIN_RESOURCE_PREFIX + ")" + regex;
//		return Pattern.compile(regex);
//	}

	/**
	 * 
	 * @param pkgPath
	 *            com.channel.plugin.test.* <br/>
	 *            com.channel.plugin <br/>
	 *            .  <br/>
	 *            .* <br/>
	 * @return
	 */
	public List<Class<?>> searchClasses(String pkgPath, ClassFilter classFilter) {
		if (StringUtils.isBlank(pkgPath))
			throw new IllegalArgumentException("pkgName");
		if (null == classFilter)
			throw new NullPointerException("classFilter");
		pkgPath = pkgPath.trim();
		List<Class<?>> ret = Lists.newArrayList();
		Pattern pattern = genClassMatchPattern(pkgPath);
		for (Entry<String, ByteCode> e : this.byteCodeCache.entrySet()) {
			if (!e.getValue().isJavaClass())
				continue;
			String cp = e.getKey();
			Matcher m = pattern.matcher(cp);
			if (!m.matches())
				continue;
			try {
				Class<?> clazz = this.loadClass(cp);
				if (null == clazz)
					continue;
				if (classFilter.accept(clazz))
					ret.add(clazz);
			} catch (ClassNotFoundException e1) {
				throw new RuntimeException("can not load class[" + e.getKey() + "];cause:" + e);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param pkgPath
	 *            com.channel.plugin.test.* <br/>
	 *            com.channel.plugin <br/>
	 * @return
	 */
	public List<Class<?>> searchClasses(String pkgPath) {
		return searchClasses(pkgPath, ClassFilter.DEFAULT);
	}

	/**
	 * com.channel.plugin.test.* =>
	 * com\\.channel\\.plugin\\.test(\\.[a-zA-Z]{1}[
	 * a-zA-Z0-9]*)*(\\.[A-Z]{1}[a-zA-Z0-9]*){1} com.channel.plugin
	 * =>com\\.channel\\.plugin(\\.[A-Z]{1}[a-zA-Z0-9]*){1}
	 */
	private Pattern genClassMatchPattern(String source) {
		if(".".equals(source)){
			return Pattern.compile("([A-Z]{1}[a-zA-Z0-9]*){1}");
		}else if(".*".equals(source)){
			return Pattern.compile("([a-zA-Z]{1}[a-zA-Z0-9]*\\.)*([A-Z]{1}[a-zA-Z0-9]*){1}");
		}else if (source.endsWith(".*")) {
			source = source.substring(0, source.length() - 2);
			source = source.replaceAll("\\.", "\\\\.");
			return Pattern.compile(source
					+ "(\\.[a-zA-Z]{1}[a-zA-Z0-9]*)*(\\.[A-Z]{1}[a-zA-Z0-9]*){1}");
		} else {
			source = source.replaceAll("\\.", "\\\\.");
			return Pattern.compile(source + "(\\.[A-Z]{1}[a-zA-Z0-9]*){1}");
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// Make sure not to load duplicate classes.
		Class<?> clazz = findLoadedClass(name);
		if (null != clazz)
			return clazz;

		ByteCode bc = byteCodeCache.get(name);
		if (null != bc)
			return defineClass(bc);

		throw new ClassNotFoundException(name);
	}

	protected InputStream getByteStream(String resource) {
		InputStream result = null;
		ClassLoader parent = getParent();
		if (parent != null) {
			result = parent.getResourceAsStream(resource);
			if (null != result)
				return result;

		}

		// if(isClass(resource))
		// resource = resolveClassName(resource);

		ByteCode byteCode = byteCodeCache.get(resource);
		if (null == byteCode)
			return null;

		return new ByteArrayInputStream(byteCode.bytes);
	}

	@Override
	protected URL findResource(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("name is blank.");

		name = resolveQueryResouceName(name);

		// TODO:是否需要代理到父类加载器？亦或本地优先？
		URL url = getParent().getResource(name);
		if (null != url)
			return url;

		try {
			return new URL(null, PlugInResourceHandler.getProtocol() + name,
					new PlugInResourceHandler(this));
			// return new URL(Handler.PROTOCOL + ":" + plugInName + "|" +
			// resolve(name));
		} catch (MalformedURLException e) {
			throw new RuntimeException("unable to locate " + name + " cause:", e);
		}
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("name is blank.");

		name = resolveQueryResouceName(name);

		final List<URL> urlList = new ArrayList<URL>();
		for (String jarName : subJarNameList) {
			String resourceGlobalBinaryName = resolveResourceName(jarName, name);
			ByteCode byteCode = byteCodeCache.get(resourceGlobalBinaryName);
			if (null != byteCode) {
				urlList.add(new URL(null, PlugInResourceHandler.getProtocol()
						+ resourceGlobalBinaryName, new PlugInResourceHandler(this)));
			}
		}

		if (0 == urlList.size())
			return super.findResources(name);
		else
			return new Enumeration<URL>() {

				private int idx = 0;

				@Override
				public boolean hasMoreElements() {
					return idx < urlList.size();
				}

				@Override
				public URL nextElement() {
					return urlList.get(idx++);
				}

			};
	}

	// TODO:
	private String resolveQueryResouceName(String name) {
		name = name.replaceAll("\\\\", "/");
		if (name.startsWith("/"))
			name = name.substring(1);
		String[] items = name.split("/");
		List<String> filteRet = new ArrayList<String>();
		for (int i = 0; i < items.length; i++) {
			String item = items[i];
			if (item.equals("."))
				continue;
			if (item.equals(".."))
				i++;
			filteRet.add(item);
		}
		StringBuilder retSb = new StringBuilder(64);
		for (String item : filteRet)
			retSb.append(item).append("/");
		// TODO:
		return retSb.substring(0, retSb.length() - 1);
	}

	private Class<?> defineClass(ByteCode bytecode) throws ClassFormatError {

		// Do it the simple way.
		// byte[] bytes = bytecode.bytes;
		String name = bytecode.binaryName;

		int i = name.lastIndexOf('.');
		if (i != -1) {
			String pkgname = name.substring(0, i);
			// Check if package already loaded.
			Package pkg = getPackage(pkgname);
			Manifest man = bytecode.manifest;
			if (pkg != null) {

				// TODO:verify package if it is sealed.

				// // Package found, so check package sealing.
				// if (pkg.isSealed()) {
				// // Verify that code source URL is the same.
				// if (!pkg.isSealed(pd.getCodeSource().getLocation())) {
				// throw new SecurityException("sealing violation: package " +
				// pkgname + " is sealed");
				// }
				//
				// } else {
				// // Make sure we are not attempting to seal the package
				// // at this code source URL.
				// if ((man != null) && isSealed(pkgname, man)) {
				// throw new
				// SecurityException("sealing violation: can't seal package " +
				// pkgname + ": already loaded");
				// }
				// }
			} else {
				if (man != null) {
					// definePackage(pkgname, man,
					// pd.getCodeSource().getLocation());
					// definePackage(pkgname, man, null);
					// TODO:this location is valid?
					definePackage(pkgname, man, defaultProtectionDomain.getCodeSource()
							.getLocation());
				} else {
					// definePackage(pkgname, null, null, null, null, null,
					// null, null);
					// TODO:this location is valid?
					definePackage(pkgname, null, null, null, null, null, null,
							defaultProtectionDomain.getCodeSource().getLocation());
				}
			}
		}

		return defineClass(bytecode.getBinaryName(), bytecode.getBytes(), defaultProtectionDomain);
	}

	protected Class<?> defineClass(String name, byte[] bytes, ProtectionDomain pd)
			throws ClassFormatError {
		return defineClass(name, bytes, 0, bytes.length, pd);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {

		// AccessController.doPrivileged(new PrivilegedAction<Object>() {
		//
		// @Override
		// public Object run() {
		// if(Thread.currentThread().getContextClassLoader() !=
		// PlugInClassLoader.this)
		// Thread.currentThread().setContextClassLoader(PlugInClassLoader.this);
		// return null;
		// }
		//
		// });

		return super.loadClass(name, resolve);
	}

	protected Package definePackage(String name, Manifest man, URL url)
			throws IllegalArgumentException {
		String path = name.concat("/");
		String specTitle = null, specVersion = null, specVendor = null;
		String implTitle = null, implVersion = null, implVendor = null;
		String sealed = null;
		URL sealBase = null;

		Attributes attr = man.getAttributes(path);
		if (attr != null) {
			specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
			specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
			specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
			implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
			implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
			implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);
			sealed = attr.getValue(Name.SEALED);
		}
		attr = man.getMainAttributes();
		if (attr != null) {
			if (specTitle == null) {
				specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
			}
			if (specVersion == null) {
				specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
			}
			if (specVendor == null) {
				specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
			}
			if (implTitle == null) {
				implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
			}
			if (implVersion == null) {
				implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
			}
			if (implVendor == null) {
				implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);
			}
			if (sealed == null) {
				sealed = attr.getValue(Name.SEALED);
			}
		}
		if (sealed != null) {
			boolean isSealed = Boolean.parseBoolean(sealed);
			if (isSealed) {
				sealBase = url;
			}
		}
		return definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion,
				implVendor, sealBase);
	}

	// TODO:is correct?
	// public void destroy() {
	// try {
	// super.finalize();
	// } catch (Throwable e) {
	// throw new RuntimeException("finalize:", e);
	// }
	// }

	//TODO:Unstable
	@SuppressWarnings("unchecked")
	private void unloadNativeLibs() throws Throwable {
		Field field = PlugInClassLoader.class.getSuperclass().getDeclaredField("nativeLibraries");
		field.setAccessible(true);
		Vector libs = (Vector) field.get(this);
		Iterator it = libs.iterator();
		Object o;
		while (it.hasNext()) {
			o = it.next();
			Method finalize = o.getClass().getDeclaredMethod("finalize", new Class[0]);
			finalize.setAccessible(true);
			finalize.invoke(o, new Object[0]);
		}
	}

	/**
	 * TODO:Reserved.
	 */
	public void freeLibrary() {
		try {
			unloadNativeLibs();
		} catch (Throwable e) {
			throw new RuntimeException("unloadNativeLibs:", e);
		}
	}

	private static final class ByteCode {
		private final String binaryName;
		private final String originalName;
		private final byte[] bytes;
		private final Manifest manifest;
		// ignore
		private String codebase;

		public ByteCode(String binaryName, String originalName, byte[] bytes, Manifest manifest,
				String codebase) {
			this.binaryName = binaryName;
			this.originalName = originalName;
			this.bytes = bytes;
			this.manifest = manifest;
			this.codebase = codebase;
		}

		public String getBinaryName() {
			return binaryName;
		}

		public String getOriginalName() {
			return originalName;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public Manifest getManifest() {
			return manifest;
		}

		public String getCodebase() {
			return codebase;
		}

		public void setCodebase(String codebase) {
			this.codebase = codebase;
		}

		private static final byte[] MAGIC_NUMBER = { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA,
				(byte) 0xBE };

		public boolean isJavaClass() {
			if (bytes.length <= 4)
				return false;
			return (bytes[0] == MAGIC_NUMBER[0]) && (bytes[1] == MAGIC_NUMBER[1])
					&& (bytes[2] == MAGIC_NUMBER[2]) && (bytes[3] == MAGIC_NUMBER[3]);
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
		}

	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		// byte[] buf = new byte[1024];
		// while (true) {
		// int len = in.read(buf);
		// if (len < 0)
		// break;
		// out.write(buf, 0, len);
		// }

		IOUtils.copy(in, out);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("plugInJarPath", plugInJarPath).append("itemCount", byteCodeCache.size())
				.append("libJars", subJarNameList).toString();
	}

	public static void main(String[] args) {
		PlugInClassLoader cl = new PlugInClassLoader("g:/channel-misg-plugin-tp20DetectWiFi.jar");
//		PlugInClassLoader cl = new PlugInClassLoader("g:/skymarket-servicelogic-plugin-app.jar");
//		List<String> ret0 = cl.searchMainResources("/*");
//		List<String> ret0 = cl.searchLibResources("/*");
//		System.out.println(ret0);
		
		List<String> ret0 = cl.searchResources("/com/*", ResourceFilter.DEFAULT, false);
		System.out.println(ret0);
		
//		System.out.println(cl.searchClasses(".*"));
//		System.out.println(cl.searchClasses("com.skymobi.market.servicelogic.plugin.app.*"));
//		System.out.println(cl.searchClasses("com.*"));
		
//		Pattern p = Pattern.compile("(?!main!)([a-zA-Z]{1}[a-zA-Z0-9]*\\.)*([A-Z]{1}[a-zA-Z0-9]*){1}");
//		Matcher m = p.matcher("main!com.skymobi.market.servicelogic.plugin.app.bean.ApkDownloadResponse");

		
		////		Matcher m = p.matcher("ApkDownloadResponse");
//		Matcher m = p.matcher("com/skymobi/market/servicelogic/handler/BaseResponse");
		
//		Pattern p = Pattern.compile("(?!ma).+");
//		Matcher m = p.matcher("maddbc");//(?!pattern)
//		System.out.println(m.matches());
	}

	// public URL getResource(String name) {
	// // Delegate to external first.
	// if (externalClassLoader != null) {
	// URL url = externalClassLoader.getResource(name);
	// if (url != null)
	// return url;
	// }
	// return super.getResource(name);
	// }

	// @Override
	// public InputStream getResourceAsStream(String name) {
	// if (null == name)
	// throw new NullPointerException("resource name must not be null.");
	//
	// //TODO:resolve name?
	//
	// ByteCode bc = byteCodeMap.get(name);
	// if (null != bc && null != bc.getBytes())
	// return new ByteArrayInputStream(bc.getBytes());
	// return null;
	// }

	// public static void main(String[] args) throws Exception {
	// // PluginClassLoader p = new PluginClassLoader();com.skymobi.channel
	// //
	// System.out.println(PluginClassLoader.class.getResourceAsStream("/test/test.txt"));
	// //
	// //
	// // InputStream is =
	// //
	// PluginClassLoader.class.getClassLoader().getResourceAsStream("test/test.txt");
	// // System.out.println(is);
	//
	// // String jarPath = "F:/music-platform.jar";
	// // PluginClassLoader pcl = new PluginClassLoader();
	// // pcl.loadByteCodes(jarPath);
	//
	// }
}
