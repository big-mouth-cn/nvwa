package org.bigmouth.nvwa.dpl.status;

public final class StatusHolderSingleton implements StatusHolderFactory {

	private static final String DEFAULT_DEPOT_DIR = "../status/";
	private static final StatusHolder instance = new FileSystemStatusHolder(DEFAULT_DEPOT_DIR);

	@Override
	public StatusHolder create() {
		return instance;
	}
}
