package org.bigmouth.nvwa.cluster;

import java.util.Set;

import org.bigmouth.nvwa.cluster.node.Updatable;
import org.bigmouth.nvwa.sap.namecode.MutableNameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NameCodePair;


public class NameCodeUpdater implements Updatable<Set<NameCodePair>> {

	private MutableNameCodeMapper nameCodeMapper;

	@Override
	public void update(Set<NameCodePair> data) {
		nameCodeMapper.update(data);
	}

	public void setNameCodeMapper(MutableNameCodeMapper nameCodeMapper) {
		this.nameCodeMapper = nameCodeMapper;
	}
}
