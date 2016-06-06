/*
 * 文件名称: PageInfo.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.page;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.RowBounds;

import com.google.common.collect.Lists;

/**
 * 分页信息对象。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class PageInfo<T> extends RowBounds implements Serializable {

    private static final long serialVersionUID = 8045056491111474700L;
    public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** 当前页 */
    private int pageNo = DEFAULT_PAGE_NO;
    /** 每页记录条数 */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /** 总页数 */
    private int totalPage = 0;

    /**
     * 当前页中存放的记录,类型一般为List.
     */
    protected List<T> result = Lists.newArrayList();

    /** 分页导航源码。按省略号来过滤过长的页码 */
    private String pageSource;

    /** 默认构造函数 */
    public PageInfo() {
        super(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    }

    /** 构造函数 */
    public PageInfo(int pageNo, int pageSize) {
        super(pageNo, pageSize);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * <p>
     * 根据结果集初始化分页HTML标签源码，在前端直接展示即可。
     * </p>
     * 前端调用代码：
     * 
     * <pre>
     * ${page.pageSource}
     * </pre>
     * 
     */
    public void initPageSource() {
        if (null == result || 0 == result.size())
            return;
        StringBuffer pathfind = new StringBuffer();
        pathfind.append("<ul class='pagination'>");
        // pathfind.append("<div class='total_info'>总共 " + totalCount + " 条记录<em>|</em>共 " + totalPage + " 页</div>");
        // 固定显示页码
        int baseNum = 7;
        int betweenNum = 3;
        // 加载页码
        int m = 1;
        int n = (int) this.getTotalPage();
        if (n >= baseNum) {
            if (this.getPageNo() - betweenNum <= 0) {
                m = 1;
                n = baseNum;
            }
            else {
                m = this.getPageNo() - betweenNum;
                n = this.getPageNo() + betweenNum;
                if (n >= this.getTotalPage()) {
                    m = (int) (m - (n - this.getTotalPage()));
                    n = (int) this.getTotalPage();
                }
            }
        }
        // --
        if (isHasPre()) {
            pathfind.append("<li><a index=\"" + this.getPrePage() + "\" href=\"javascript:;\">&laquo;</a></li>");
        }
        else {
            pathfind.append("<li class='disabled'><a href=\"javascript:;\">&laquo;</a></li>");
        }
        // --

        if (m == 2) {
            pathfind.append("<li><a index=\"1\" href=\"javascript:;\"> 1 </a></li>");
        }
        if (m > 2) {
            pathfind.append("<li><a index=\"1\" href=\"javascript:;\"> 1 </a>" + "<span>...</span></li>");
        }
        for (int i = m; i <= n; i++) {
            if (i == n) {
                if (i == this.getPageNo()) {
                    pathfind.append("<li class='active'><a href='javascript:;'>" + i
                            + " <span class='sr-only'>(current)</span></a></li>");
                }
                else {
                    pathfind.append("<li><a index=\"" + i + "\" href=\"javascript:;\">" + i + "</a></li>");
                }
            }
            else {
                if (i == this.getPageNo()) {
                    pathfind.append("<li class='active'><a href='javascript:;'>" + i
                            + " <span class='sr-only'>(current)</span></a></li>");
                }
                else {
                    pathfind.append("<li><a index=\"" + i + "\" href=\"javascript:;\">" + i + "</a></li>");
                }
            }

        }
        if (this.getPageNo() != this.getTotalPage() && this.getTotalPage() > baseNum
                && (this.getTotalPage() - this.getPageNo()) > betweenNum) {
            if (this.getTotalPage() == (this.getPageNo() + betweenNum + 1)) {
                pathfind.append("<li><a index=\"" + this.getTotalPage() + "\" href=\"javascript:;\"> "
                        + this.getTotalPage() + " </a></li>");
            }
            else {
                pathfind.append("<li><span>...</span><a index=\"" + this.getTotalPage() + "\" href=\"javascript:;\"> "
                        + this.getTotalPage() + " </a></li>");
            }
        }
        if (isHasNext()) {
            pathfind.append("<li><a index=\"" + this.getNextPage() + "\" href=\"javascript:;\">&raquo;</a></li>");
        }
        else {
            pathfind.append("<li class='disabled'><a href=\"javascript:;\">&raquo;</a></li>");
        }
        pathfind.append("</ul>");
        pageSource = pathfind.toString();
    }

    public void initPagePathfindding() {
        initPageSource();
        initViewid();
    }

    private void initViewid() {
        try {
            List<T> list = this.result;
            int i = 1;
            for (T entity : list) {
                PropertyUtils.setProperty(entity, "viewid", "" + ((pageNo - 1) * pageSize + (i++)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 是否还有上一页
     * </p>
     * 
     * @return
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPage());
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        }
        else {
            return pageNo;
        }
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        }
        else {
            return pageNo;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("PageInfo{").append("  pageNo=").append(pageNo).append(", pageSize=").append(pageSize)
                .append(", totalCount=").append(totalCount).append('}');
        return sb.toString();
    }

    public int getOffset() {
        return pageNo;
    }

    public int getLimit() {
        return pageSize;
    }

    public void setTotalCount(int totalCount) {
        super.setTotalCount(totalCount);
        reCalcTotalCount(totalCount, pageSize);
    }

    private void reCalcTotalCount(int totalCount, int pageSize) {
        if (pageSize != 0) {
            totalPage = totalCount / pageSize;
            if (totalCount % pageSize > 0) {
                totalPage++;
            }
        }
    }

    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
        }
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
            reCalcTotalCount(totalCount, pageSize);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String getPageSource() {
        return pageSource;
    }

}
