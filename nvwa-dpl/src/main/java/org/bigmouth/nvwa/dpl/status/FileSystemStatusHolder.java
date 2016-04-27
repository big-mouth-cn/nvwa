package org.bigmouth.nvwa.dpl.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using java serialization mechanism to save PlugIn's status.<br>
 * Attention:Non-thread safe.
 * 
 * @author nada
 * 
 */
public class FileSystemStatusHolder implements StatusHolder {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemStatusHolder.class);

	// TODO:URI or URL?
	private final String depotDir;

	public FileSystemStatusHolder(String depotDir) {
		if (StringUtils.isBlank(depotDir))
			throw new IllegalArgumentException("depotDir:" + depotDir);
		File dir = new File(depotDir);
		if (!dir.exists()) {
		    if (! dir.mkdirs()) {
		        LOGGER.error("depotDir[{}] is not exists.", depotDir);
		    }
		}
		if (!dir.isDirectory()) {
			LOGGER.error("depotDir[{}] is not dir.", depotDir);
		}
		this.depotDir = depotDir;
	}

	@Override
	public void addOrUpdateStatusOf(StatusSource source) throws Exception {
		if (null == source)
			throw new NullPointerException("source");

		String statusFilePath = getStatusFilePath(source);
		writeStatus(source, statusFilePath);
	}

	@Override
	public List<Status> getAllStatus() throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Status getStatusOf(StatusSource source) throws Exception {
		if (null == source)
			throw new NullPointerException("source");

		String statusFilePath = getStatusFilePath(source);
		return readStatus(statusFilePath);
	}

	@Override
	public void removeStatusOf(StatusSource source) throws Exception {
		String statusFilePath = getStatusFilePath(source);
		try {
			FileUtils.forceDelete(new File(statusFilePath));
		} catch (Exception e) {
			LOGGER.error("removeSavedStatusOf:", e);
		}
	}

	@Override
	public void syncStatusOf(StatusSource source) throws Exception {
		if (null == source)
			throw new NullPointerException("statusSource");

		String statusFilePath = getStatusFilePath(source);
		File statusFile = new File(statusFilePath);
		if (statusFile.exists()) {// load it.
			Status status = readStatus(statusFilePath);
			source.setStatus(status);
		} else {// write it.
			writeStatus(source, statusFilePath);
		}
	}

	private void writeStatus(StatusSource source, String statusFilePath) throws Exception {
		ObjectOutputStream out = null;
		try {
			Status status = source.getStatus();
			if (null == status)
				throw new IllegalArgumentException("status");

			out = new ObjectOutputStream(new FileOutputStream(statusFilePath));
			out.writeObject(status);
			out.flush();
		} finally {
			if (null != out)
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.error("in.close:", e);
				}
		}
	}

	private Status readStatus(String statusFilePath) throws Exception {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(statusFilePath));
			return (Status) in.readObject();
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("in.close:", e);
				}
		}
	}

	private String getStatusFilePath(StatusSource source) {
		String key = source.getStatusKey();
		if (null == key || 0 == key.length())
			throw new IllegalArgumentException("key");

		String statusFilePath = depotDir + File.pathSeparator + key;
		return statusFilePath;
	}
}
