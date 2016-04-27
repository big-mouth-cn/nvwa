package org.bigmouth.nvwa.codec.tlv.decoders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;


/**
 * object array decode.<br>
 * only support 1-d array.<br>
 * wrapped array.<br>
 * 
 * @author nada
 * 
 */
public class TLVObjectArrayDecoder extends AbstractTLVDecoder<Object[]> {

	private static final String[] DECODER_KEYES = new String[] { "java.lang.Object[]" };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_TAG_LENGTH = 4;

	private static final int DEFAULT_LEN_LENGTH = 4;

	@SuppressWarnings("unchecked")
	private List<byte[]> separateArraySource(byte[] source,Class<?> arrayItemClass) {
		if (null == source)
			throw new NullPointerException("source");

		List<byte[]> result = new ArrayList<byte[]>();

		TLVDecoder intDecoder = decoderProvider.lookupCodec(int.class);
		int index = 0;

		do {
			// tag
			// byte[] tag_bytes = new byte[DEFAULT_TAG_LENGTH];
			// System.arraycopy(source, index, tag_bytes, 0,
			// DEFAULT_LEN_LENGTH);
			// int tag = (Integer) intDecoder.codec(tag_bytes, null);

			index += DEFAULT_TAG_LENGTH;
			
			// len
			byte[] len_bytes = new byte[DEFAULT_LEN_LENGTH];
			try {
			    System.arraycopy(source, index, len_bytes, 0, DEFAULT_LEN_LENGTH);
			}
			catch (ArrayIndexOutOfBoundsException e) {
			    if ( null != illegalTLVContentHandler )
			        illegalTLVContentHandler.handle(source);
			    throw e;
			}

			int len = (Integer) intDecoder.codec(len_bytes, null);
			index += DEFAULT_LEN_LENGTH;

			//TODO:avoid to OM.
			if((source.length - index) < len){
				throw new IllegalTLVLengthException("ArrayItemClass:" + arrayItemClass + ", illegal length:" + len);
			}

			byte[] slice = null;
			try {
			    slice = new byte[len];
			} catch (NegativeArraySizeException e) {
			    if ( null != illegalTLVContentHandler )
                    illegalTLVContentHandler.handle(source);
                throw e;
            }
			
			System.arraycopy(source, index, slice, 0, len);
			result.add(slice);

			index += len;

		} while (index < source.length);

		return result;
	}

	@Override
	public Object[] codec(byte[] source, Object componentType) {
		// additionInfo is componentType of array.
		if (null == source || null == componentType)
			throw new NullPointerException("source is null or componentType is null");

		Class<?> clazz = (Class<?>) componentType;

		List<byte[]> slices = separateArraySource(source,clazz);
		Object[] result = (Object[]) Array.newInstance(clazz, slices.size());

		// get tlv annotation fields.
//		final Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();
//		ReflectUtils.findFields(clazz, new ReflectUtils.FieldFilter() {
//			@Override
//			public boolean accept(final Field filed) {
//				TLVAttribute tlvAttr = filed.getAnnotation(TLVAttribute.class);
//				if (null != tlvAttr && tlvAttr.tag() != 0)
//					fieldMap.put(tlvAttr.tag(), filed);
//				return false;
//			}
//		});

		for (int i = 0; i < slices.size(); i++)
			result[i] = decoderProvider.getObjectDecoder().codec(slices.get(i), clazz);

		return result;
	}

	@Override
	public String[] getCodecKeyes() {
		return DECODER_KEYES;
	}

	@Override
	public TLVDecoderProvider getCodecProvider() {
		return decoderProvider;
	}

	@Override
	public void setCodecProvider(TLVDecoderProvider codecPrivoder) {
		this.decoderProvider = codecPrivoder;
	}

	@Override
	public boolean isObjectDecoder() {
		return false;
	}

}
