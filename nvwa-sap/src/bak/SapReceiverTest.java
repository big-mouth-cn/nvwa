package com.skymobi.market.commons.sap;

import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.channel.utils.Closure;
import com.google.common.collect.Maps;
import com.skymobi.market.commons.sap.ContentRange;
import com.skymobi.market.commons.sap.DefaultSapResponse;
import com.skymobi.market.commons.sap.ExtendedItem;
import com.skymobi.market.commons.sap.ExtendedItemType;
import com.skymobi.market.commons.sap.MutableSapResponse;
import com.skymobi.market.commons.sap.SapMessage;
import com.skymobi.market.commons.sap.SapRequest;
import com.skymobi.market.commons.sap.SapResponse;
import com.skymobi.market.commons.sap.SapResponseStatus;
import com.skymobi.market.commons.sap.codec.SapReqRespCodecFactory;
import com.skymobi.market.commons.sap.namecode.NameCodeMapper;
import com.skymobi.market.commons.transport.MinaReceiver;
import com.skymobi.market.commons.transport.MinaReplier;

public class SapReceiverTest {

	public static void main(String[] args) {

		NameCodeMapper ncMapper = NameCodeMapperTestUtils.getInstance();
		Map<String, IoFilter> filters = Maps.newHashMap();
		filters.put("SapCodec", new ProtocolCodecFilter(new SapReqRespCodecFactory(ncMapper)));

		final MinaReplier replier = new MinaReplier();
		MinaReceiver mr = new MinaReceiver(replier, filters, 12345);

		mr.setReactor(new Closure() {

			@Override
			public void execute(Object message) {
				SapRequest sapRequest = (SapRequest) message;
				System.out.println("receive sapRequest:" + sapRequest);

				// sendback
				// sapMessage.getContent().flip();
				SapResponse sapResponse = createSapResponse(sapRequest);
				System.out.println("send back:" + sapResponse);
				replier.reply(sapResponse);
			}
		});
		mr.init();
	}

	private static SapResponse createSapResponse(SapMessage message) {
		MutableSapResponse sapResponse = new DefaultSapResponse(message);
		sapResponse.setStatus(SapResponseStatus.OK);
		// sapResponse.ad
		// sapResponse.addDownloadItem(new DownloadItem(1234,
		// "md5md5mdmd5md5mdmd5md5mdmd5md5md",
		// DownloadBizType.APK));

		IoBuffer content = IoBuffer.allocate(10);
		for (int i = 20; i < 30; i++) {
			content.put((byte) i);
		}
		sapResponse.setContent(content);

		ExtendedItem item0 = new ExtendedItem.Builder(ExtendedItemType.APK).setULPRange(true)
				.setFormatMark(202).setContentRange(ContentRange.create(501, 301, 20001, 30001))
				.addContentFlag("md0md0mdmd0md0mdmd0md0mdmd0md0md").addContentFlag(
						"md1md1mdmd1md1mdmd1md1mdmd1md1md").build();
		sapResponse.addExtendedItem(item0);

		ExtendedItem item1 = new ExtendedItem.Builder(ExtendedItemType.APK).setULPRange(true)
				.setFormatMark(202).setContentRange(ContentRange.create(501, 301, 20001, 30001))
				.addContentFlag("md0md0mdmd0md0mdmd0md0mdmd0md0md").addContentFlag(
						"md1md1mdmd1md1mdmd1md1mdmd1md1md").build();
		sapResponse.addExtendedItem(item1);

		return sapResponse;
	}
}
