/*
 * 文件名称: MyBatisException.java
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

public class MyBatisException extends Exception {

    private static final long serialVersionUID = -466980864716946654L;

    public MyBatisException() {
    }

    public MyBatisException(String message) {
        super(message);
    }

    public MyBatisException(Throwable cause) {
        super(cause);
    }

    public MyBatisException(String message, Throwable cause) {
        super(message, cause);
    }

}
