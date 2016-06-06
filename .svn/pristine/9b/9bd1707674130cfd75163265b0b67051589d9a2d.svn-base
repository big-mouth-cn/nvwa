package org.bigmouth.nvwa.codec;

public interface Codec<T, R, E extends CodecProvider<?>, A> {

	public R codec(T source, A additionInfo);

	public E getCodecProvider();

	public void setCodecProvider(E codecPrivoder);

	public String[] getCodecKeyes();

}
