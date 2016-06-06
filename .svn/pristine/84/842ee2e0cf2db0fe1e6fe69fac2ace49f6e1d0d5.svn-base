package org.bigmouth.nvwa.dpl;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;
import org.bigmouth.nvwa.dpl.event.listener.LifeCycleListener;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenericLifeCycle<C extends Config, L extends LifeCycleListener> implements
		MutableLifeCycle<C, L> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericLifeCycle.class);

	private volatile Status status = new ImmutableStatus(true);
	private volatile C config;
	private final List<L> listeners = new CopyOnWriteArrayList<L>();

	public GenericLifeCycle() {
		super();
	}

	public GenericLifeCycle(C config, Status status) {
		this(config, status, null);
	}

	public GenericLifeCycle(C config, Status status, List<L> listeners) {
		super();
		this.status = status;
		this.config = config;
		if (null != listeners)
			this.listeners.addAll(listeners);
	}

	@Override
	public void init() {
		if (null == listeners) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("listeners is null,ignore.");
		} else {
			for (L listener : listeners) {
				listener.init();
				listener.onCreated(new CreatedEvent(this));
			}
		}
	}

	@Override
	public void destroy() {
		if (null == listeners) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("listeners is null,ignore.");
		} else {
			for (L listener : listeners) {
				listener.onDestroyed(new DestroyedEvent(this));
				listener.destroy();
			}
		}
	}

	@Override
	public C getConfig() {
		return config;
	}

	public void setConfig(C config) {
		if (null == config)
			throw new NullPointerException();

		C oldCfg = this.config;
		C newCfg = config;
		if (!hasChanged(oldCfg, newCfg)) {
			return;
		}
		ConfigChangedEvent event = new ConfigChangedEvent(this, oldCfg, newCfg);
		this.config = config;
		for (L listener : listeners)
			listener.onConfigChanged(event);
	}

	private boolean hasChanged(C oldCfg, C newCfg) {
		if (oldCfg.hashCode() == newCfg.hashCode()) {
			return oldCfg.equals(newCfg);
		} else {
			return false;
		}
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void setStatus(Status status) {
		if (null == status)
			throw new NullPointerException();

		Status oldStatus = this.status;
		Status newStatus = status;

		if (!hasChanged(oldStatus, newStatus)) {
			return;
		}

		StatusChangedEvent event = new StatusChangedEvent(this, oldStatus, newStatus);
		this.status = status;

		if (null == listeners) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("listeners is null,ignore.");
		} else {
			for (L listener : listeners) {
				if (status.isRunning()) {
					listener.onActived(new ActivedEvent(this));
				} else {
					listener.onDeActived(new DeActivedEvent(this));
				}
				listener.onStatusChanged(event);
			}
		}
	}

	private boolean hasChanged(Status oldStatus, Status newStatus) {
		if (oldStatus.hashCode() == newStatus.hashCode()) {
			return oldStatus.equals(newStatus);
		} else {
			return false;
		}
	}

	@Override
	public void addEventListener(L listener) {
		if (null == listener)
			throw new NullPointerException();

		listeners.add(listener);
	}

	@Override
	public Iterator<L> getAllEventListener() {
		if (null == listeners)
			throw new NullPointerException("listeners");

		return listeners.iterator();
	}

	public void setListeners(List<L> listeners) {
		if (null == listeners)
			throw new NullPointerException("listeners");

		this.listeners.clear();
		this.listeners.addAll(listeners);
	}

	@Override
	public void addEventListeners(List<L> listeners) {
		if (null == listeners || 0 == listeners.size())
			throw new IllegalArgumentException("listeners is blank.");
		this.listeners.addAll(listeners);
	}

	@Override
	public void clearEventListeners() {
		this.listeners.clear();
	}
}
