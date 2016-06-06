package org.bigmouth.nvwa.utils.jms;

/**
 * using java default serialize mechanism.
 * 
 * @author nada
 * 
 */
public class DefaultMessageBeanFactory implements MessageBeanFactory {

	@Override
	public MessageBean createMessageBean(String sql, Object[][] argumentsList) {
		return new DefaultMessageBean(sql, argumentsList);
	}

}
