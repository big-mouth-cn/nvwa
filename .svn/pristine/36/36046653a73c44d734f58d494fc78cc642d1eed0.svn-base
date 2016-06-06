package org.bigmouth.nvwa.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class JarFileUtils {

	private JarFileUtils() {
	}

	public static byte[] getBytesFromJar(String jarPath, String entryName) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
			ZipEntry zipEntry = jarFile.getEntry(entryName);
			InputStream is = jarFile.getInputStream(zipEntry);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int v = -1;
			while (-1 != (v = is.read()))
				bos.write(v);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("getBytesFromJar:", e);
		} catch (Exception e) {
			throw new RuntimeException("getBytesFromJar:", e);
		} finally {
			if (null != jarFile)
				try {
					jarFile.close();
				} catch (IOException e) {
					// ignore.
				}
		}
	}
}
