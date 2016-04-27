package com.skymobi.market.commons.sap;

import java.util.Set;

import org.bigmouth.nvwa.sap.namecode.DefaultNameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.MutableNameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NameCodePair;

import com.google.common.collect.Sets;

public final class NameCodeMapperTestUtils {

	private static final MutableNameCodeMapper ncMapper;

	private NameCodeMapperTestUtils() {
	}

	static {
		ncMapper = new DefaultNameCodeMapper();

		Set<NameCodePair> pairs = Sets.newHashSet();
		pairs.add(new NameCodePair() {

			@Override
			public short getPlugInCode() {
				return 100;
			}

			@Override
			public String getPlugInName() {
				return "app";
			}

			@Override
			public short getServiceCode() {
				return 1001;
			}

			@Override
			public String getServiceName() {
				return "default";
			}
		});

		pairs.add(new NameCodePair() {

			@Override
			public short getPlugInCode() {
				return 200;
			}

			@Override
			public String getPlugInName() {
				return "applist";
			}

			@Override
			public short getServiceCode() {
				return 2001;
			}

			@Override
			public String getServiceName() {
				return "frame";
			}
		});

		ncMapper.update(pairs);
	}

	public static NameCodeMapper getInstance() {
		return ncMapper;
	}
}
