package org.bigmouth.nvwa.log;

public class MultiRecordSupport implements RecordClosure {

	private final RecordClosure impl;

	public MultiRecordSupport(RecordClosure impl) {
		super();
		if (null == impl)
			throw new NullPointerException("impl");
		this.impl = impl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object logInfo) {
		if (null == logInfo)
			throw new NullPointerException("logInfo");
		if (logInfo.getClass().isArray()) {
			for (Object item : (Object[]) logInfo) {
				impl.execute(item);
			}
		} else if (Iterable.class.isAssignableFrom(logInfo.getClass())) {
			for (Object item : (Iterable<Object>) logInfo) {
				impl.execute(item);
			}
		} else {
			impl.execute(logInfo);
		}
	}
}
