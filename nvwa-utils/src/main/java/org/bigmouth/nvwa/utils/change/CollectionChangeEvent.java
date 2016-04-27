package org.bigmouth.nvwa.utils.change;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bigmouth.nvwa.utils.Identifiable;

import com.google.common.collect.Lists;

public class CollectionChangeEvent<K, V extends Identifiable<K>> extends
		ChangeEvent<CollectionSnapshot<K, V>> {

	private static final long serialVersionUID = 1L;

	private final List<V> addedItems = Lists.newArrayList();
	private final List<V> updatedItems = Lists.newArrayList();
	private final List<V> removedItems = Lists.newArrayList();
	private final List<V> currentAllItems = Lists.newArrayList();

	public CollectionChangeEvent(Object source, CollectionSnapshot<K, V> oldSnapshot,
			CollectionSnapshot<K, V> newSnapshot) {
		super(source, oldSnapshot, newSnapshot);
		init();
	}

	private void init() {
		CollectionSnapshot<K, V> oldSnapshot = getOldSnapshot();
		CollectionSnapshot<K, V> newSnapshot = getNewSnapshot();

		if (null == oldSnapshot) {
			addedItems.addAll(newSnapshot.getData().values());
		} else {// oldSnapshot is not null,newSnapshot is not null.
			Map<K, V> oldData = oldSnapshot.getData();
			Map<K, V> newData = newSnapshot.getData();

			for (Entry<K, V> e : oldData.entrySet()) {
				K k = e.getKey();
				V ov = e.getValue();
				if (!newData.containsKey(k)) {
					removedItems.add(ov);
				} else {
					V nv = newData.get(k);
					if (!((ov.hashCode() == nv.hashCode()) && (ov.equals(nv)))) {
						updatedItems.add(nv);
					}
				}
			}

			for (Entry<K, V> e : newData.entrySet()) {
				K k = e.getKey();
				V v = e.getValue();
				if (!oldData.containsKey(k))
					addedItems.add(v);
			}
		}
		currentAllItems.addAll(newSnapshot.getData().values());
	}

	public List<V> getAddedItems() {
		return Collections.unmodifiableList(addedItems);
	}

	public List<V> getUpdatedItems() {
		return Collections.unmodifiableList(updatedItems);
	}

	public List<V> getRemovedItems() {
		return Collections.unmodifiableList(removedItems);
	}

	public List<V> getCurrentAllItems() {
		return Collections.unmodifiableList(currentAllItems);
	}
}
