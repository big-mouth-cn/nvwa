/*
 * 文件名称: RowBounds.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-7-6
 * 修改内容: 
 */
package org.apache.ibatis.session;

/**
 * 
 * @author Allen.Hu / 2012-7-6
 * 针对mybatis不同方言的分页实现中增加“总记录数”
 */
public class RowBounds {

    public final static int NO_ROW_OFFSET = 0;

    public final static int NO_ROW_LIMIT = Integer.MAX_VALUE;

    public final static RowBounds DEFAULT = new RowBounds();

    public final static String COUNT_COLUMN_ALIAS = "ROW_COUNT_ASDFG_ASDFG__";

    // pageNo
    protected int offset;

    // pageSize
    protected int limit;

    // totalCount
    protected int totalCount;

    public RowBounds() {
        this.offset = NO_ROW_OFFSET;
        this.limit = NO_ROW_LIMIT;
    }

    public RowBounds(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
