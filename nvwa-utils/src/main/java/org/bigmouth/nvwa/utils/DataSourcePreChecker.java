package org.bigmouth.nvwa.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourcePreChecker extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourcePreChecker.class);

	private static final String DEFAULT_FLAG = "default_flag";

	private String flag = DEFAULT_FLAG;

	private DataSource dataSource;

	public DataSourcePreChecker(String flag, DataSource dataSource) {
		this(dataSource);
		this.flag = flag;
	}

	public DataSourcePreChecker(DataSource dataSource) {
		if (null == dataSource)
			throw new NullPointerException("dataSource");

		this.dataSource = dataSource;
	}

	@Override
	public void run() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("begin inspect datasource[{}].", flag);
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			LOGGER.debug("end inspect datasource[{}],datasource[{}] is OK.", flag, flag);
		} catch (SQLException e) {
			LOGGER.error("end inspect datasource[" + flag
					+ "],can not get connection from datasource[" + flag + "]:", e);
		} finally {
			if (null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
