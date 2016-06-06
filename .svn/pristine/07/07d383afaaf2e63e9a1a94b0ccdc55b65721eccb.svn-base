package org.bigmouth.nvwa.access.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectService {
	
	public static ByteArrayOutputStream connect(String host) {
		URL url = null;
		try {
			url = new URL(host);
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		}
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			conn.connect();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			is = conn.getInputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while((len = is.read(b)) != -1){
				os.write(b, 0, len);
			}
			os.flush();
			return os;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(null != is)
				try {
					is.close();
				} catch (IOException e) {
				}
			if(null != conn)
				conn.disconnect();
		}
	}
	
}
