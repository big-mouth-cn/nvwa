package org.bigmouth.nvwa.contentstore.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.contentstore.ContentStore;
import org.bigmouth.nvwa.contentstore.impl.ContentStoreUtils;
import org.bigmouth.nvwa.utils.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileContentStore implements ContentStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileContentStore.class);

	private final String dirPath;

	public static final String SEP = File.separator;

	private Factory<String> DEFAULT_PATH_FACTORY = new Factory<String>() {
		@Override
		public String create() {
			return "";
		}
	};

	private Factory<String> pathFactory = DEFAULT_PATH_FACTORY;

	public FileContentStore(String dirPath) {
		if (StringUtils.isBlank(dirPath))
			throw new IllegalArgumentException("dirPath is blank.");

		this.dirPath = dirPath;
	}

	public FileContentStore(String dirPath, Factory<String> pathFactory) {
		this(dirPath);
		if (null == pathFactory)
			throw new NullPointerException("pathFactory");

		this.pathFactory = pathFactory;
	}

	@Override
	public String store(byte[] content) {
		if (null == content || 0 == content.length)
			throw new IllegalArgumentException("content is null.");

		String key = ContentStoreUtils.content2key(content);
		String path_mid = pathFactory.create();

		String path_dir = dirPath + SEP + path_mid;
		File f0 = new File(path_dir);
		if (!f0.exists()) {
			boolean r = f0.mkdirs();
			if (!r)
				throw new RuntimeException("mkdirs occur error,dir:" + path_dir);
		}
		String path = path_dir + SEP + key;
		String ret = path_mid + SEP + key;

		File f = new File(path);
		if (f.exists()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("content for key [" + ret + "] already exist. just ingore");
			return ret;
		}

		try {
			FileUtils.writeByteArrayToFile(f, content);
			return ret;
		} catch (IOException e) {
			LOGGER.error("FileUtils.writeByteArrayToFile:", e);
			return null;
		}
	}

	@Override
	public byte[] fetch(String key) {
		if (null == key)
			throw new NullPointerException("key");

		String path = getFullPath(key);
		try {
			return FileUtils.readFileToByteArray(new File(path));
		} catch (IOException e) {
			throw new RuntimeException("FileUtils.readFileToByteArray:");
		}
	}

	@Override
	public boolean remove(String key) {
		if (null == key)
			throw new NullPointerException("key");

		String path = getFullPath(key);
		try {
			FileUtils.forceDelete(new File(path));
			return true;
		} catch (IOException e) {
			throw new RuntimeException("FileUtils.forceDelete:");
		}
	}

	@Override
	public boolean contains(String key) {
		if (null == key)
			throw new NullPointerException("key");

		String path = getFullPath(key);
		return new File(path).exists();
	}

	@Override
	public List<String> getKeys() {
		throw new UnsupportedOperationException();
	}

	private String getFullPath(String key) {
		return dirPath + SEP + key;
	}

	public String getDirPath() {
		return dirPath;
	}
}
