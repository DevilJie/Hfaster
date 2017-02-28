package hsj.czy.mybatisframe.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页组件 类Pager.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2017年2月28日 下午2:34:58
 */
public class Pager implements Serializable {

    private static final long   serialVersionUID = -2707171505887622174L;

    public static final Integer MAX_PAGE_SIZE    = 500;                   // 每页最大记录数限制

    // 排序方式（递增、递减）
    public enum Order {
        asc, desc
    }

    private int     pageNumber = 1;  // 当前页码

    private int     pageSize   = 20; // 每页记录数

    private String  searchBy;        // 查找字段

    private String  keyword;         // 查找关键字

    private String  orderBy;         // 排序字段

    private Order   order;           // 排序方式

    private int     totalCount;      // 总记录数

    private List<?> result;          // 返回结果

    // 获取总页数
    public int getPageCount() {
        int pageCount = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        return pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = 1;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

}
