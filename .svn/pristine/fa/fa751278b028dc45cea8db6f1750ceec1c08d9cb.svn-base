package org.bigmouth.nvwa.sap.namecode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DefaultNameCodeMapper implements MutableNameCodeMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNameCodeMapper.class);

	private final AtomicReference<Name2CodeAndCode2Name> name2code_code2name = new AtomicReference<Name2CodeAndCode2Name>();

	@Override
	public PlugInServiceCodePair getCodeOf(PlugInServiceNamePair ns)
			throws NoSuchNameCodeMappingException {
		if (null == ns)
			throw new NullPointerException("PlugInServiceNameSource");
		Name2CodeAndCode2Name _container = name2code_code2name.get();
		if (null == _container)
			throw new IllegalStateException("DefaultNameCodeTransformer has not been initialized.");
		PlugInServiceCodePair ret = _container.name2code.get(new UniqueNameSource(ns));
		if (null == ret) {
			throw new NoSuchNameCodeMappingException("Can not found any codeinfo for ["
					+ ns.getPlugInName() + "][" + ns.getServiceName() + "]. ");
		}
		return ret;
	}

	@Override
	public PlugInServiceNamePair getNameOf(PlugInServiceCodePair cs)
			throws NoSuchNameCodeMappingException {
		if (null == cs)
			throw new NullPointerException("PlugInServiceNameSource");
		Name2CodeAndCode2Name _container = name2code_code2name.get();
		if (null == _container)
			throw new IllegalStateException("DefaultNameCodeTransformer has not been initialized.");
		PlugInServiceNamePair ret = _container.code2name.get(new UniqueCodeSource(cs));
		if (null == ret) {
			throw new NoSuchNameCodeMappingException("Can not found any nameinfo for ["
					+ cs.getPlugInCode() + "][" + cs.getServiceCode() + "].");
		}
		return ret;
	}

	@Override
	public void update(Set<NameCodePair> slist) {
		if (null == slist)
			throw new NullPointerException("slist");

		final Set<NameCodePair> copy = Sets.newHashSet();
		for (NameCodePair ncs : slist) {
			copy.add(createUniqueNameCodeSource(ncs));
		}

		if (0 == copy.size())
			throw new NullPointerException("slist size is 0.");

		Map<PlugInServiceNamePair, PlugInServiceCodePair> name2code = Maps.newHashMap();
		Map<PlugInServiceCodePair, PlugInServiceNamePair> code2name = Maps.newHashMap();
		for (NameCodePair ncs : copy) {
			name2code.put(new UniqueNameSource(ncs), new UniqueCodeSource(ncs));
			code2name.put(new UniqueCodeSource(ncs), new UniqueNameSource(ncs));
		}

		name2code_code2name.set(new Name2CodeAndCode2Name(name2code, code2name));
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("NameCodeMapper has bean updated.");
	}

	// spring setter injection
	public void setMappingData(Set<NameCodePair> slist) {
		update(slist);
	}

	private UniqueNameCodeSource createUniqueNameCodeSource(NameCodePair ncs) {
		return new UniqueNameCodeSource(ncs);
	}

	private static final class Name2CodeAndCode2Name {
		private final Map<PlugInServiceNamePair, PlugInServiceCodePair> name2code;
		private final Map<PlugInServiceCodePair, PlugInServiceNamePair> code2name;

		private Name2CodeAndCode2Name(Map<PlugInServiceNamePair, PlugInServiceCodePair> name2code,
				Map<PlugInServiceCodePair, PlugInServiceNamePair> code2name) {
			this.name2code = name2code;
			this.code2name = code2name;
		}
	}

	private static final class UniqueNameCodeSource implements NameCodePair {

		private final NameCodePair impl;

		private UniqueNameCodeSource(NameCodePair impl) {
			if (null == impl)
				throw new NullPointerException("impl");
			if (0 > impl.getPlugInCode())
				throw new IllegalArgumentException("impl.plugInCode" + impl.getPlugInCode() + ".");
			if (0 > impl.getServiceCode())
				throw new IllegalArgumentException("impl.serviceCode" + impl.getServiceCode() + ".");
			if (StringUtils.isBlank(impl.getPlugInName()))
				throw new IllegalArgumentException("impl.plugInName is blank.");
			if (StringUtils.isBlank(impl.getServiceName()))
				throw new IllegalArgumentException("impl.serviceName is blank.");
			this.impl = impl;
		}

		@Override
		public short getPlugInCode() {
			return impl.getPlugInCode();
		}

		@Override
		public String getPlugInName() {
			return impl.getPlugInName();
		}

		@Override
		public short getServiceCode() {
			return impl.getServiceCode();
		}

		@Override
		public String getServiceName() {
			return impl.getServiceName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getPlugInCode();
			result = prime * result + getServiceCode();
			result = prime * result + ((getPlugInName() == null) ? 0 : getPlugInName().hashCode());
			result = prime * result
					+ ((getServiceName() == null) ? 0 : getServiceName().hashCode());
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
			NameCodePair other = (NameCodePair) obj;
			if (getPlugInCode() != other.getPlugInCode())
				return false;
			if (getServiceCode() != other.getServiceCode())
				return false;
			if (getPlugInName() == null) {
				if (other.getPlugInName() != null)
					return false;
			} else if (!getPlugInName().equals(other.getPlugInName()))
				return false;
			if (getServiceName() == null) {
				if (other.getServiceName() != null)
					return false;
			} else if (!getServiceName().equals(other.getServiceName()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("plugInName", getPlugInName()).append(
					"plugInCode", getPlugInCode()).append("serviceName", getServiceName()).append(
					getServiceCode()).toString();
		}
	}

	private static final class UniqueCodeSource implements PlugInServiceCodePair {

		private final PlugInServiceCodePair impl;

		private UniqueCodeSource(PlugInServiceCodePair impl) {
			if (null == impl)
				throw new NullPointerException("impl");
			if (0 > impl.getPlugInCode())
				throw new IllegalArgumentException("impl.plugInCode" + impl.getPlugInCode() + ".");
			if (0 > impl.getServiceCode())
				throw new IllegalArgumentException("impl.serviceCode" + impl.getServiceCode() + ".");
			this.impl = impl;
		}

		@Override
		public short getPlugInCode() {
			return impl.getPlugInCode();
		}

		@Override
		public short getServiceCode() {
			return impl.getServiceCode();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getPlugInCode();
			result = prime * result + getServiceCode();
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
			UniqueCodeSource other = (UniqueCodeSource) obj;
			if (getPlugInCode() != other.getPlugInCode())
				return false;
			if (getServiceCode() != other.getServiceCode())
				return false;
			return true;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("plugInCode", getPlugInCode()).append(
					getServiceCode()).toString();
		}
	}

	private static final class UniqueNameSource implements PlugInServiceNamePair {

		private final PlugInServiceNamePair impl;

		private UniqueNameSource(PlugInServiceNamePair impl) {
			if (null == impl)
				throw new NullPointerException("impl");
			if (StringUtils.isBlank(impl.getPlugInName()))
				throw new IllegalArgumentException("impl.plugInName is blank.");
			if (StringUtils.isBlank(impl.getServiceName()))
				throw new IllegalArgumentException("impl.serviceName is blank.");
			this.impl = impl;
		}

		@Override
		public String getPlugInName() {
			return impl.getPlugInName();
		}

		@Override
		public String getServiceName() {
			return impl.getServiceName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getPlugInName() == null) ? 0 : getPlugInName().hashCode());
			result = prime * result
					+ ((getServiceName() == null) ? 0 : getServiceName().hashCode());
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
			UniqueNameSource other = (UniqueNameSource) obj;
			if (getPlugInName() == null) {
				if (other.getPlugInName() != null)
					return false;
			} else if (!getPlugInName().equals(other.getPlugInName()))
				return false;
			if (getServiceName() == null) {
				if (other.getServiceName() != null)
					return false;
			} else if (!getServiceName().equals(other.getServiceName()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("plugInName", getPlugInName()).append(
					"serviceName", getServiceName()).toString();
		}
	}
}
