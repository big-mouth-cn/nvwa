package org.bigmouth.nvwa.dpl.status;

/**
 * getKey() rule:
 * status-[systemId]-['plugin']['service']-[plugInCode]-[serviceCode]<br>
 * for example:<br>
 * status-1-plugin-common-login<br>
 * status-2-service-bizPlugin-bizService<br>
 * 
 * @author nada
 * 
 */
public interface StatusSource {

	public static final String SPLIT = "-";

	public String getStatusKey();

	public Status getStatus();

	public void setStatus(Status status);

}
