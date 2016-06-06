package org.bigmouth.nvwa.cluster;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.Map;

import org.bigmouth.nvwa.cluster.node.DataDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * ServiceLogic Node json data structure: <br>
 * [{access:
 * '172.16.3.42:8888',servicelogic:'172.16.3.44:7777'},{access:'172.16.3.42:8888',servicelogic:'172.16.3.44:
 * 7 7 7 7 ' } ]
 * 
 * @author nada
 * 
 */
public final class ServiceLogicNodeDecetor extends
		DataDetector<Map<InetSocketAddress, InetSocketAddress>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogicNodeDecetor.class);

	@Override
	protected Map<InetSocketAddress, InetSocketAddress> raw2Model(byte[] content) {
		String ncJson = new String(content);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("ServiceLogic Node data update,data:{}", ncJson);

		Map<InetSocketAddress, InetSocketAddress> ret = Maps.newHashMap();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		JsonElement je = null;
		try {
			je = parser.parse(ncJson);
		} catch (JsonSyntaxException e) {
			throw new RuntimeException("Illegal Json,ServiceLogic Node data:", e);
		}
		JsonArray jArray = je.getAsJsonArray();

		Type type = new TypeToken<MappingNode>() {
		}.getType();

		for (int i = 0; i < jArray.size(); i++) {
			JsonElement el = jArray.get(i);
			MappingNode pair = gson.fromJson(el, type);
			ret.put(pair.getAccess(), pair.getServicelogic());
		}

		return ret;
	}

	public final class MappingNode {
		private String access;
		private String servicelogic;

		public InetSocketAddress getAccess() {
			String[] factors = this.access.split(":");
			if (null == factors || 2 != factors.length)
				throw new RuntimeException("Illegal access InetSocketAddress:" + access);
			try {
				return new InetSocketAddress(factors[0], Integer.parseInt(factors[1]));
			} catch (Exception e) {
				throw new RuntimeException("Illegal access InetSocketAddress:" + access);
			}
		}

		public InetSocketAddress getServicelogic() {
			String[] factors = this.servicelogic.split(":");
			if (null == factors || 2 != factors.length)
				throw new RuntimeException("Illegal servicelogic InetSocketAddress:" + access);
			try {
				return new InetSocketAddress(factors[0], Integer.parseInt(factors[1]));
			} catch (Exception e) {
				throw new RuntimeException("Illegal servicelogic InetSocketAddress:" + access);
			}
		}

		public void setAccess(String access) {
			this.access = access;
		}

		public void setServicelogic(String servicelogic) {
			this.servicelogic = servicelogic;
		}
	}
}
