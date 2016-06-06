package org.bigmouth.nvwa.dpl.hotswap;

import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DirChangeListener extends EventListener {

	public static final DirChangeListener ONLY_LOGGING = new DirChangeListener() {

		private final Logger LOGGER = LoggerFactory.getLogger(DirChangeListener.class);

		@Override
		public void onDirChanged(DirChangeEvent event) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("dir has changed,detail info:" + event);
		}
	};

	public void onDirChanged(DirChangeEvent event);
}
