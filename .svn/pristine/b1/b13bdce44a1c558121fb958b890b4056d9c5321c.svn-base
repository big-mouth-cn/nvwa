package org.bigmouth.nvwa.sap;

import java.util.List;

public interface MutableSapResponse extends SapResponse, MutableSapTransactionMessage {

	void setStatus(SapResponseStatus status);

	void addExtendedItem(ExtendedItem item);

	void addExtendedItems(List<ExtendedItem> items);

	void clearExtendedItems();
}
