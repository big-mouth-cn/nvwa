package org.bigmouth.nvwa.cluster;

import java.lang.reflect.Type;
import java.util.Set;

import org.bigmouth.nvwa.cluster.node.DataDetector;
import org.bigmouth.nvwa.sap.namecode.DefaultNameCodePair;
import org.bigmouth.nvwa.sap.namecode.NameCodePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Name Code json data structure:<br>
 * 
 * [ {plugInName:
 * 'testPlugIn0',plugInCode:'123',serviceName:'testService0',serviceCode:'456'},
 * {plugInName:
 * 'testPlugIn1',plugInCode:'124',serviceName:'testService1',serviceCode:'4567'}
 * ]
 * 
 * @author nada
 * 
 */
public final class NameCodeDetector extends DataDetector<Set<NameCodePair>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(NameCodeDetector.class);

	@Override
	protected Set<NameCodePair> raw2Model(byte[] content) {
		String ncJson = new String(content);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Name Code data update,data:{}", ncJson);

		Set<NameCodePair> ret = Sets.newHashSet();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		JsonElement je = null;
		try {
			je = parser.parse(ncJson);
		} catch (JsonSyntaxException e) {
			throw new RuntimeException("Illegal Json,Name Code data:", e);
		}
		JsonArray jArray = je.getAsJsonArray();

		Type type = new TypeToken<DefaultNameCodePair>() {
		}.getType();

		for (int i = 0; i < jArray.size(); i++) {
			JsonElement el = jArray.get(i);
			NameCodePair pair = gson.fromJson(el, type);
			ret.add(pair);
		}
		return ret;
	}
}
