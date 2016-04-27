package org.bigmouth.nvwa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GZipUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GZipUtils.class);

	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMDDhhmmss");

	private static final Random r = new Random();

	private static final String SUFFIX = ".zip";

	private GZipUtils() {
	}

	public static String generateZipName() {
		return sf.format(new Date()) + r.nextInt(100) + SUFFIX;
	}

	public static String generateZipDir() {
		return sf.format(new Date()) + r.nextInt(100);
	}

	public static byte[] compress(byte[] source) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gos = new GZIPOutputStream(bos);
			gos.write(source);
			gos.finish();
			bos.flush();
			gos.flush();
			gos.close();
		} catch (IOException e) {
			LOGGER.error("compress;", e);
			return null;
		}
		return bos.toByteArray();
	}

	public static byte[] uncompress(byte[] source) {
		return null;
	}

	public static void uncompressFileList(String zipFileName, String extPlace) throws IOException {
		try {
			File dir = new File(extPlace);
			if (!dir.exists())
				dir.mkdirs();

			ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
			ZipEntry entry = null;

			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();
				if (entry.isDirectory()) {
					File file = new File(extPlace + "/" + entryName);
					file.mkdirs();
				} else {
					FileOutputStream os = new FileOutputStream(extPlace + "/" + entryName);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						os.write(buf, 0, len);
					}
					os.close();
					in.closeEntry();
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}

	public static void compressFileList(String zipFileName, String zipDir) throws IOException {
		try {

			File dir = new File(zipDir);
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFileName));
			handleDir(dir, zipOut);
			zipOut.close();

		} catch (IOException e) {
			throw e;
		}
	}

	private static void handleDir(File dir, ZipOutputStream zipOut) throws IOException {

		FileInputStream fileIn;
		File[] files = dir.listFiles();
		if (files.length == 0) {
			zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
			zipOut.closeEntry();
		} else {
			for (File f : files) {
				if (f.getName().endsWith(".zip")) {
					continue;
				}
				if (f.isDirectory()) {
					handleDir(f, zipOut);
				} else {

					fileIn = new FileInputStream(f);
					zipOut.putNextEntry(new ZipEntry(f.getName()));
					byte[] buf = new byte[1024];
					int len;
					while ((len = fileIn.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					fileIn.close();
					zipOut.closeEntry();
				}
			}
		}
	}
	
	public static byte[] gzip(byte[] source) {
		if (null == source)
			throw new NullPointerException("source");

		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gos = null;

		try {
			bis = new ByteArrayInputStream(source);
			bos = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(bos);

			byte[] buf = new byte[1024];
			int num;
			while ((num = bis.read(buf)) != -1) {
				gos.write(buf, 0, num);
			}

			gos.finish();
			return bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("gzip:", e);
		} finally {
			if (null != gos)
				try {
					gos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (null != bos)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (null != bis)
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
	
	public static byte[] ungzip(byte[] source){
		throw new UnsupportedOperationException();
	}
	
//	public static byte[] gZip(byte[] data) {
//		byte[] b = null;
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			GZIPOutputStream gzip = new GZIPOutputStream(bos);
//			gzip.write(data);
//			gzip.finish();
//			gzip.close();
//			b = bos.toByteArray();
//			bos.close();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return b;
//	}
//
//	public static byte[] unGZip(byte[] data) {
//		byte[] b = null;
//		try {
//			ByteArrayInputStream bis = new ByteArrayInputStream(data);
//			GZIPInputStream gzip = new GZIPInputStream(bis);
//			byte[] buf = new byte[1024];
//			int num = -1;
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
//				baos.write(buf, 0, num);
//			}
//			b = baos.toByteArray();
//			baos.flush();
//			baos.close();
//			gzip.close();
//			bis.close();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return b;
//	}

	public static void main(String[] args) throws Exception {
		File srcFile = new File("G:/call/index.sky");
		byte[] srcBytes = FileUtils.readFileToByteArray(srcFile);
		
		byte[] targetBytes = gzip(srcBytes);
		File targetFile = new File("G:/call/index.sky5.gzip");
		FileUtils.writeByteArrayToFile(targetFile, targetBytes);

//		uncompressFileList("f:/gzip/haha.zip", "F:/gzip/test1/");
		// compressFileList("f:/gzip/haha","F:/gzip/test/");

		// byte[] source = IOUtils.toByteArray(new
		// FileInputStream("F:/gzip/source.txt"));
		// byte[] target = compress(source);
		// FileOutputStream fos = new FileOutputStream("F:/gzip/target1");
		// fos.write(target);
		// fos.flush();
		// fos.close();
	}

}
