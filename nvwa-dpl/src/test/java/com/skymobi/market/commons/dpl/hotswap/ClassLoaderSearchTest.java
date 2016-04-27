package com.skymobi.market.commons.dpl.hotswap;

import static junit.framework.Assert.assertEquals;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.hotswap.ResourceFilter;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClassLoaderSearchTest {

	private static PlugInClassLoader pcl = null;

	@BeforeClass
	public static void beforeClass() {
		URL url = ClassLoader.getSystemClassLoader().getResource(
				"com/skymobi/market/commons/dpl/hotswap/skymarket-servicelogic-plugin-app.jar");
		pcl = new PlugInClassLoader(url.getPath());
	}

	@Test
	public void testSearchResources() {
		// Not support pattern
		List<String> ret = pcl.searchResources("/", false);
		assertEquals(0, ret.size());
		ret = pcl.searchResources("/", true);
		assertEquals(0, ret.size());
		ret = pcl.searchResources("/*", false);
		assertEquals(0, ret.size());
		ret = pcl.searchResources("/*", true);
		assertEquals(0, ret.size());

		// Typical scenarios
		// com/ com/* filter
		ret = pcl.searchResources("com/", false);
		assertEquals(0, ret.size());
		ret = pcl.searchResources("com/", true);
		assertEquals(0, ret.size());

		ret = pcl.searchResources("com/*", false);
		assertEquals(2, ret.size());
		Set<String> es = new HashSet<String>();
		es.add("com/skymobi/market/servicelogic/plugin/app/config/pluginContext.xml");
		es.add("com/skymobi/market/servicelogic/plugin/app/config/plugin.properties");
		assertEquals(es, new HashSet<String>(ret));

		ret = pcl.searchResources("com/*", true);
		assertEquals(3, ret.size());
		es = new HashSet<String>();
		es.add("com/skymobi/market/servicelogic/plugin/app/config/pluginContext.xml");
		es.add("com/skymobi/market/servicelogic/plugin/app/config/plugin.properties");
		es.add("com/skymobi/market/servicelogic/plugin/child0.xml");
		assertEquals(es, new HashSet<String>(ret));

		ret = pcl.searchResources("com/*", new ResourceFilter() {

			@Override
			public boolean accept(String name) {
				return name.endsWith(".xml");
			}

		}, true);
		assertEquals(2, ret.size());
		es = new HashSet<String>();
		es.add("com/skymobi/market/servicelogic/plugin/app/config/pluginContext.xml");
		es.add("com/skymobi/market/servicelogic/plugin/child0.xml");
		assertEquals(es, new HashSet<String>(ret));

		// config/ config/*
		ret = pcl.searchResources("config", false);
		assertEquals(1, ret.size());
		assertEquals("config/test-test.xml", ret.get(0));

		ret = pcl.searchResources("config/", false);
		assertEquals(1, ret.size());
		assertEquals("config/test-test.xml", ret.get(0));

		ret = pcl.searchResources("config/*", false);
		assertEquals(1, ret.size());
		assertEquals("config/test-test.xml", ret.get(0));

		ret = pcl.searchResources("config", true);
		assertEquals(2, ret.size());
		es = new HashSet<String>();
		es.add("config/child1.xml");
		es.add("config/test-test.xml");
		assertEquals(es, new HashSet<String>(ret));

		ret = pcl.searchResources("config/", true);
		assertEquals(2, ret.size());
		es = new HashSet<String>();
		es.add("config/child1.xml");
		es.add("config/test-test.xml");
		assertEquals(es, new HashSet<String>(ret));

		ret = pcl.searchResources("config/*", true);
		assertEquals(2, ret.size());
		es = new HashSet<String>();
		es.add("config/child1.xml");
		es.add("config/test-test.xml");
		assertEquals(es, new HashSet<String>(ret));
	}

	public void testSearchClasses() {

	}
}
