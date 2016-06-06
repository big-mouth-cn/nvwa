package org.bigmouth.nvwa.utils;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.commons.collections.Transformer;

public class List2ArrayTransformer implements Transformer {

	private Transformer po2voTransformer;

	public List2ArrayTransformer(Transformer po2voTransformer) {
		this.po2voTransformer = po2voTransformer;
	}

	@Override
	public Object transform(Object list) {
		if (null == list)
			throw new NullPointerException();

		if (!List.class.isAssignableFrom(list.getClass()))
			throw new IllegalArgumentException("input value must be class of List.");

		List<?> _list = (List<?>) list;
		if (_list.size() == 0)
			return null;

		Object[] result = null;

		int count = 0;
		for (Object item : _list) {
			Object res = po2voTransformer.transform(item);
			if (null == result)
				result = (Object[]) Array.newInstance(res.getClass(), _list.size());
			result[count++] = res;
		}

		return result;
	}

}
