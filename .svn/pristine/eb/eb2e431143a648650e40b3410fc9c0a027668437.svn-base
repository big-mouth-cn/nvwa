package org.bigmouth.nvwa.codec;

import java.util.List;

public interface CodecProvider<E extends Codec<?, ?, ? extends CodecProvider<E>, ?>> {

	public E lookupCodec(Class<?> sourceClazz);

	public List<E> getAllCodecs();

	public void registerCodec(E codec);

	public void removeCodec(String codecKey);

}
