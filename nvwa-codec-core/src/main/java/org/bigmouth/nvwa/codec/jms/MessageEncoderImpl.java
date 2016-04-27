package org.bigmouth.nvwa.codec.jms;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.jms.bean.TLVArgumentsBean;
import org.bigmouth.nvwa.codec.jms.bean.TLVMessageBean;
import org.bigmouth.nvwa.codec.jms.bean.TLVSqlBean;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageEncoderImpl implements MessageEncoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoderImpl.class);

	private static final int ARGUMENTS_WARP_TAG = 2;

	private TLVEncoderProvider tlvEncoderProvider;

	public MessageEncoderImpl(TLVEncoderProvider tlvEncoderProvider) {
		this.tlvEncoderProvider = tlvEncoderProvider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] encode(TLVMessageBean bean) {
		if (null == bean)
			throw new NullPointerException();

		TLVSqlBean sqlBean = bean.getSqlBean();
		List<TLVArgumentsBean> argumentsBeanList = bean.getArgumentsBeanList();

		if (!validateSql(sqlBean.getSql()))
			return null;
		if (!validateArgumentsList(argumentsBeanList))
			return null;

		// TLVEncoder stringTlvEncoder =
		// tlvEncoderProvider.lookupCodec(String.class);
		TLVEncoder objTlvEncoder = tlvEncoderProvider.getObjectEncoder();
		TLVEncoder intTlvEncoder = tlvEncoderProvider.lookupCodec(int.class);

		// process sql
		List<byte[]> sql_bytes = (List<byte[]>) objTlvEncoder.codec(sqlBean, null);

		// process arguments
		List<byte[]> arguments_bytes = new ArrayList<byte[]>();
		for (TLVArgumentsBean argumentsBean : argumentsBeanList) {
			List<byte[]> item_bytes = (List<byte[]>) objTlvEncoder.codec(argumentsBean, null);
			arguments_bytes.addAll(item_bytes);
		}

		// wrap arguments
		List<byte[]> wrap_arguments_bytes = new ArrayList<byte[]>();
		List<byte[]> wrap_tag = (List<byte[]>) intTlvEncoder.codec(ARGUMENTS_WARP_TAG, null);
		List<byte[]> wrap_len = (List<byte[]>) intTlvEncoder.codec(ByteUtils
				.totalByteSizeOf(arguments_bytes), null);

		wrap_arguments_bytes.addAll(wrap_tag);
		wrap_arguments_bytes.addAll(wrap_len);
		wrap_arguments_bytes.addAll(arguments_bytes);

		sql_bytes.addAll(wrap_arguments_bytes);
		return ByteUtils.union(sql_bytes);
	}

	private boolean validateArgumentsList(List<TLVArgumentsBean> argumentsList) {
		if (null == argumentsList)
			throw new NullPointerException("argumentsList is null");

		if (0 == argumentsList.size()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("argumentsList size is 0,ignore.");

			return false;
		}
		return true;
	}

	private boolean validateSql(String sql) {
		if (null == sql)
			throw new NullPointerException("sql statement is null.");

		if (sql.toLowerCase().indexOf("insert") == -1) {
			LOGGER.error("sql must be insert statement.");
			return false;
		}
		return true;
	}

}
