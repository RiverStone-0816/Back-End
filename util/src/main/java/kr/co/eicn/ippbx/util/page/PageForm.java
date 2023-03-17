package kr.co.eicn.ippbx.util.page;

import kr.co.eicn.ippbx.util.SortField;
import org.jooq.SortOrder;

/**
 * @author tinywind
 * @since 2016-09-03
 */
public class PageForm extends PageQueryableForm {
    @PageQueryable
    protected Integer page = 1;

    @PageQueryable
    protected Integer limit = 15;

    @PageQueryable
    protected SortField sort;

    @PageQueryable
    protected SortOrder orderDirection;

    public final String getQuery(int toPage) {
        final int originPage = page;
        page = toPage;
        final String query = getQuery();

        page = originPage;
        return query;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return (Math.max(page - 1, 0)) * getLimit();
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public SortField getSort() {
        return sort;
    }

    public void setOrder(SortField sort) {
        this.sort = sort;
    }

    public SortOrder getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(SortOrder orderDirection) {
        this.orderDirection = orderDirection;
    }
}
