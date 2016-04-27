package org.bigmouth.nvwa.sap;

import java.util.UUID;

public interface Identifiable {

	UUID getIdentification();

	boolean existIdentification();
}
