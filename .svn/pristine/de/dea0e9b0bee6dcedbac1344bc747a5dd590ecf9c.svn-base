package org.bigmouth.nvwa.utils.change;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.utils.Identifiable;

import com.google.common.collect.Maps;

public final class CollectionSnapshot<K, V extends Identifiable<K>> {
	private final Map<K, V> data = Maps.newHashMap();

	public CollectionSnapshot(List<V> data) {
		if (null == data)
			throw new NullPointerException("data");
		for (V v : data) {
			this.data.put(v.getId(), v);
		}
	}

	public Map<K, V> getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		CollectionSnapshot other = (CollectionSnapshot) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
