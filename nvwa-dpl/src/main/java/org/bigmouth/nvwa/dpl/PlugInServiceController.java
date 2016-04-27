package org.bigmouth.nvwa.dpl;

import java.util.List;

public interface PlugInServiceController {

	List<String> getAllPlugInsDesc();

	/**
	 * Active PlugIn,and active all service which belongs to this PlugIn.
	 * 
	 * @param plugInCode
	 */
	void activePlugIn(String plugInKey);

	void deActivePlugIn(String plugInKey);

	void activeService(String plugInKey, String serviceKey);

	void deActiveService(String plugInKey, String serviceKey);
}
