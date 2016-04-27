package org.bigmouth.nvwa.access;

import org.bigmouth.nvwa.sap.Identifiable;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.transport.Replier;
import org.bigmouth.nvwa.utils.Closure;
import org.bigmouth.nvwa.utils.Transformer;

public final class SapCallback implements Closure {

	private final Transformer<SapResponse, Identifiable> sap2HttpResponseTransformer;
	private final Replier replier;

	public SapCallback(Transformer<SapResponse, Identifiable> sap2HttpResponseTransformer,
			Replier replier) {
		this.sap2HttpResponseTransformer = sap2HttpResponseTransformer;
		this.replier = replier;
	}

	@Override
	public void execute(Object message) {
		if (!(message instanceof SapResponse)) {
			throw new IllegalArgumentException("message expect SapResponse,but "
					+ message.getClass());
		}

		Identifiable httpResponse = sap2HttpResponseTransformer.transform((SapResponse) message);
		replier.reply(httpResponse);
	}
}
