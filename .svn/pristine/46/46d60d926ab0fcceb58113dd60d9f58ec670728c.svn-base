package org.bigmouth.nvwa.sap;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;

//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                   protocol identifier(4)                      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|   ptl ver(1)  |   msg type(1) |content type(1)| content ver(1)|
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                       transactionId(16)                       |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|	    status(2)                 |     extented item count(2)    |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

// ExtendedItem(*)

//|                      content length(4)                        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                    application data field(...)                |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

public class DefaultSapResponse extends DefaultSapTransactionMessage implements MutableSapResponse {

	private static final long serialVersionUID = 1L;

	private SapResponseStatus status;
	private List<ExtendedItem> extendedItems = Lists.newArrayList();

	public DefaultSapResponse() {
		this.setMessageType(MessageType.RESPONSE);
	}

	public DefaultSapResponse(SapMessage message) {
		super(message);
		this.setMessageType(MessageType.RESPONSE);
	}

	@Override
	public SapResponseStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(SapResponseStatus status) {
		if (null == status)
			throw new NullPointerException("status");
		this.status = status;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.RESPONSE;
	}

	@Override
	public void addExtendedItem(ExtendedItem item) {
		if (null == item)
			throw new NullPointerException("item");
		extendedItems.add(item);
	}

	@Override
	public void addExtendedItems(List<ExtendedItem> items) {
		if (null == items)
			throw new IllegalArgumentException("items is null.");
		this.extendedItems.addAll(items);
	}

	@Override
	public void clearExtendedItems() {
		extendedItems.clear();
	}

	@Override
	public boolean existsExtendedItems() {
		return extendedItems.size() > 0;
	}

	@Override
	public List<ExtendedItem> getExtendedItems() {
		return Collections.unmodifiableList(extendedItems);
	}

	@Override
	public int getExtendedItemsCount() {
		return extendedItems.size();
	}

	@Override
	public void normalize() {
		super.normalize();
		if (extendedItems.isEmpty())
			return;
		for (ExtendedItem item : extendedItems) {
			if (item.getMergeOffset() > getContentLength()) {
				throw new IllegalExtendedItemException("ExtendedItem mergeOffset:"
						+ item.getMergeOffset() + ",content length:" + getContentLength());
			}
		}
		Collections.sort(extendedItems);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
