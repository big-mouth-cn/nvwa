package org.bigmouth.nvwa.mybatis.escape;

/**
 * 转义接口。
 * 不同类型的数据库需要实现该接口。
 * 
 * <pre>
 * 
 * EscapeFactory escapeFactory = SpringContextHolder.getBean(EscapeFactory.class);
 * 
 * String dbType = DataBaseTypes.getDataSourceType(SpringContextHolder.getCurrentDataSource());
 * 
 * Escape escape = escapeFactory.getEscape(dbType);
 * </pre>
 * 
 * @author Allen.Hu / 2013-3-18
 */
public interface Escape {

    char[] ESCAPE_CHAR = { '%', '_', '^' };

    String escape(String value);

    String unescape(String value);

}
