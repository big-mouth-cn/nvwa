package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.ClassLoaderHolder;
import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;

public class ReviseContextClassLoaderListener<T extends LifeCycleListener> extends
		ReviseContextClassLoaderSupport implements LifeCycleListener {

	private final T listener;

	public ReviseContextClassLoaderListener(ClassLoaderHolder classLoaderHolder, T listener) {
		super(classLoaderHolder);
		if (null == listener)
			throw new NullPointerException("listener");
		if (null == classLoaderHolder)
			throw new NullPointerException("classLoaderHolder");
		this.listener = listener;
	}

	public T getListener() {
		return listener;
	}

	@Override
	public void init() {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().init();
			}
		});
	}

	@Override
	public void destroy() {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().destroy();
			}
		});
	}

	@Override
	public void onCreated(final CreatedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onCreated(event);
			}
		});
	}

	@Override
	public void onDestroyed(final DestroyedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onDestroyed(event);
			}
		});
	}

	@Override
	public void onActived(final ActivedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onActived(event);
			}
		});
	}

	@Override
	public void onDeActived(final DeActivedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onDeActived(event);
			}
		});
	}

	@Override
	public void onStatusChanged(final StatusChangedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onStatusChanged(event);
			}
		});
	}

	@Override
	public void onConfigChanged(final ConfigChangedEvent event) {
		revise(new ReviseCallback() {
			@Override
			public void execute() {
				getListener().onConfigChanged(event);
			}
		});
	}
}
