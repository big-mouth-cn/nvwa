package org.bigmouth.nvwa.sap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;


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

//|                       client ip(4)                            |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                       access ip(4)                            |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           access port(2)      |            reserved(2)        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|           srcplugin(2)        |            srcservice(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           dstplugin(2)        |            dstservice(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|            ULP request content begin offset(4)                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|            ULP request content end offset(4)                  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|                      content length(4)                        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                    application data field(...)                |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

public class DefaultSapRequest extends DefaultSapTransactionMessage implements MutableSapRequest {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_ULP_BEGIN_OFFSET = 0;
	private static final int DEFAULT_ULP_END_OFFSET = 0;

	private Ip clientIp;
	private IpPort accessAddress;

	private String sourcePlugInName;
	private String sourceServiceName;
	private String targetPlugInName;
	private String targetServiceName;

	private int ulpBeginOffset = DEFAULT_ULP_BEGIN_OFFSET;
	private int ulpEndOffset = DEFAULT_ULP_END_OFFSET;

	private Ip peerIp;
	private IpPort localAddress;

	public DefaultSapRequest() {
		super();
		this.setMessageType(MessageType.REQUEST);
	}

	public DefaultSapRequest(SapMessage message) {
		super(message);
		this.setMessageType(MessageType.REQUEST);
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.REQUEST;
	}

	@Override
	public void setSourcePlugInName(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("sourcePlugInName is blank.");
		this.sourcePlugInName = name;
	}

	@Override
	public void setSourcePlugInServiceNamePair(PlugInServiceNamePair pair) {
		if (null == pair)
			throw new NullPointerException("pair");
		this.sourcePlugInName = pair.getPlugInName();
		this.sourceServiceName = pair.getServiceName();
	}

	@Override
	public void setSourceServiceName(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("sourceServiceName is blank.");
		this.sourceServiceName = name;
	}

	@Override
	public void setTargetPlugInName(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("targetPlugInName is blank.");
		this.targetPlugInName = name;

	}

	@Override
	public void setTargetPlugInServiceNameSource(PlugInServiceNamePair pair) {
		if (null == pair)
			throw new NullPointerException("pair");
		this.targetPlugInName = pair.getPlugInName();
		this.targetServiceName = pair.getServiceName();
	}

	@Override
	public void setTargetServiceName(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("targetServiceName is blank.");
		this.targetServiceName = name;
	}

	@Override
	public String getSourcePlugInName() {
		return this.sourcePlugInName;
	}

	@Override
	public PlugInServiceNamePair getSourcePlugInServiceNamePair() {
		return new PlugInServiceNamePair() {

			@Override
			public String getPlugInName() {
				return getSourcePlugInName();
			}

			@Override
			public String getServiceName() {
				return getSourceServiceName();
			}

		};
	}

	@Override
	public String getSourceServiceName() {
		return this.sourceServiceName;
	}

	@Override
	public String getTargetPlugInName() {
		return this.targetPlugInName;
	}

	@Override
	public PlugInServiceNamePair getTargetPlugInServiceNamePair() {
		return new PlugInServiceNamePair() {

			@Override
			public String getPlugInName() {
				return getTargetPlugInName();
			}

			@Override
			public String getServiceName() {
				return getTargetServiceName();
			}

		};
	}

	@Override
	public String getTargetServiceName() {
		return this.targetServiceName;
	}

	@Override
	public void setULP_beginOffset(int offset) {
		this.ulpBeginOffset = offset;
	}

	@Override
	public void setULP_endOffset(int offset) {
		this.ulpEndOffset = offset;
	}

	@Override
	public int getULP_beginOffset() {
		return ulpBeginOffset;
	}

	@Override
	public int getULP_endOffset() {
		return ulpEndOffset;
	}

	@Override
	public void setAccessAddress(IpPort accessAddress) {
		this.accessAddress = accessAddress;
	}

	@Override
	public void setClientIp(Ip clientIp) {
		this.clientIp = clientIp;
	}

	@Override
	public void setLocalAddress(IpPort localAddress) {
		this.localAddress = localAddress;
	}

	@Override
	public void setPeerIp(Ip peerIp) {
		this.peerIp = peerIp;
	}

	@Override
	public IpPort getAccessAddress() {
		return accessAddress;
	}

	@Override
	public Ip getClientIp() {
		return clientIp;
	}

	@Override
	public IpPort getLocalAddress() {
		return localAddress;
	}

	@Override
	public Ip getPeerIp() {
		return peerIp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
