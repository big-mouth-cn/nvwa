package org.bigmouth.nvwa.transport;

import org.bigmouth.nvwa.utils.Closure;

public interface Receiver {

	void init();

	void destroy();

	void setReactor(Closure reactor);
}
