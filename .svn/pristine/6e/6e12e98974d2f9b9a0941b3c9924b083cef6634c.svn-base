package org.bigmouth.nvwa.utils.change;

import java.util.Map;

import org.bigmouth.nvwa.utils.Identifiable;


public abstract class CollectionChangeMonitor<K, V extends Identifiable<K>> extends
		AbstractChangeMonitor<CollectionChangeEvent<K, V>, CollectionSnapshot<K, V>> {

	public CollectionChangeMonitor(Map<String, ChangeListener<CollectionSnapshot<K, V>>> listeners) {
		super(listeners);
	}

	public CollectionChangeMonitor(String threadName, long checkPeriodMillis,
			Map<String, ChangeListener<CollectionSnapshot<K, V>>> listeners) {
		super(threadName, checkPeriodMillis, listeners);
	}

	@Override
	protected CollectionChangeEvent<K, V> createChangeEvent(CollectionSnapshot<K, V> oldSnapshot,
			CollectionSnapshot<K, V> newSnapshot) {
		return new CollectionChangeEvent<K, V>(newSnapshot, oldSnapshot, newSnapshot);
	}
}
