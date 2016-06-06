/*
 * 文件名称: EscapePostgreSQL.java
 * 版权信息: Copyright 2005-2013 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2013-3-18
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.escape;

/**
 * 基于PostgreSQL数据库的转义方言。
 * 
 * <pre>
 *  SELECT * FROM TABLE
 *  WHERE
 *  NALE LIKE '%/%小%' ESCAPE '/';
 * </pre>
 * 
 * @author Allen.Hu / 2013-3-18
 */
public class PostgreSQLEscape implements Escape {

    @Override
    public String escape(String value) {
        StringBuffer sb = new StringBuffer(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            for (char es : Escape.ESCAPE_CHAR) {
                if (c == es) {
                    sb.append("/");
                    sb.append(c);
                }
                else {
                    sb.append(c);
                }
                break;
            }
        }
        return sb.toString();
    }

    @Override
    public String unescape(String value) {
        return null;
    }

}
