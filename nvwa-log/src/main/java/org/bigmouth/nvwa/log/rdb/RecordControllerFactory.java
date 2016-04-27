package org.bigmouth.nvwa.log.rdb;

public interface RecordControllerFactory {

	// TODO:define exception?
	RecordController create(Object logInfo);
}
