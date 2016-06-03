/*
 * 文件名称: NullSQLStatementException.java
 * 版权信息: Copyright 2005-2013 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2013-2-1
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.exception;


/**
 * 
 * @author Allen.Hu / 2013-2-1
 */
public class NullSQLStatementException extends MyBatisException {

    private static final long serialVersionUID = -3673466438388628061L;

    public NullSQLStatementException() {
        super();
    }

    public NullSQLStatementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullSQLStatementException(String message) {
        super(message);
    }

    public NullSQLStatementException(Throwable cause) {
        super(cause);
    }

}
